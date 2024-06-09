package com.example.flame.data.util

import android.content.Context
import android.util.Base64
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemReader
import org.bouncycastle.util.io.pem.PemWriter
import java.io.File
import java.io.InputStreamReader
import java.io.StringWriter
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Utility class for generating and managing cryptographic keys.
 */
class KeyPairGeneration(private val context: Context) {

    private val privateKeyFileName: String = "private_key.pem"
    private val publicKeyFileName: String = "public_key.pem"
    private val pinCode: Int = 123456
    private val salt: ByteArray = ByteArray(16).also { SecureRandom().nextBytes(it) }
    private val iterationCount = 65536
    private val keyLength = 256
    /**
     * Generate the private/public key pair.
     *
     * @return A new RSA key pair.
     */
    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }

    /**
     * Generate an encryption key for each chat Room which is called RoomKey.
     *
     * @return A new AES secret key.
     */
    fun generateRoomKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    /**
     * Encrypts a RoomKey using the public key of the recipient of the message.
     *
     * @param roomKey The AES key to encrypt.
     * @param publicKey The RSA public key to use for encryption.
     * @return The encrypted room key as a byte array.
     */
    fun encryptRoomKey(roomKey: SecretKey, publicKey: PublicKey): ByteArray {
        val encryptedRoomKey = Cipher.getInstance("RSA")
        encryptedRoomKey.init(Cipher.ENCRYPT_MODE, publicKey)
        return encryptedRoomKey.doFinal(roomKey.encoded)
    }

    /**
     * Encrypts a message using an encrypted RoomKey.
     *
     * @param message The message to encrypt.
     * @param roomId The room id of the chat message.
     * @return The encrypted message as a byte array.
     */
    fun encryptMessage(message: String, roomId: String): ByteArray {
        val roomKey = generateRoomKey()
        /*val publicKey = loadPublicKey()
        val encrypted = encryptRoomKey(roomKey, publicKey)*/

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12) // Generate a 12-byte IV
        SecureRandom().nextBytes(iv)
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, roomKey, gcmSpec)
        val encryptedMessage = cipher.doFinal(message.toByteArray())
        return iv + encryptedMessage // Prepend IV to the encrypted message
    }

    /**
     * Decrypts the room key using the user's private key.
     *
     * @param encryptedRoomKey The encrypted room key.
     * @return The decrypted AES secret key.
     */
    fun decryptRoomKey(encryptedRoomKey: ByteArray): SecretKey {
        val privateKey: PrivateKey = loadPrivateKey()
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedSessionKeyBytes = cipher.doFinal(encryptedRoomKey)
        return SecretKeySpec(decryptedSessionKeyBytes, 0, decryptedSessionKeyBytes.size, "AES")
    }

    /**
     * Decrypts an encrypted message using an AES room key.
     *
     * @param encryptedMessage The encrypted message as a byte array.
     * @param roomKey The AES room key to use for decryption.
     * @return The decrypted message as a string.
     */
    fun decryptMessage(encryptedMessage: ByteArray, roomKey: ByteArray): String {
        val decryptedRoomKey = decryptRoomKey(roomKey)
        val iv = encryptedMessage.sliceArray(0..11) // Extract the IV
        val cipherText = encryptedMessage.sliceArray(12 until encryptedMessage.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, decryptedRoomKey, gcmSpec)
        val decryptedMessage = cipher.doFinal(cipherText)
        return String(decryptedMessage)
    }

    /**
     * Create a secret key out of the pin code.
     * @param pinCode The pincode of the user.
     */
    private fun deriveKeyFromPin(pinCode: Int): SecretKey {
        val pinCodeString = pinCode.toString()
        val keySpec = PBEKeySpec(pinCodeString.toCharArray(), salt, iterationCount, keyLength)
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = keyFactory.generateSecret(keySpec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * Converts a public key to an encrypted PEM format and saves it to a file.
     * @param privateKey The public key to encrypt.
     * @param roomKey The AES key to use for encryption.
     */
    fun convertPrivateKeyToEncryptedPemFormatAndStore(privateKey: PrivateKey) {
        val encryptionPinCodeKey = deriveKeyFromPin(this.pinCode)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, encryptionPinCodeKey)
        val encryptedData = cipher.doFinal(privateKey.encoded)
        val pemObject = PemObject("RSA PRIVATE KEY", encryptedData)
        StringWriter().use { stringWriter ->
            PemWriter(stringWriter).use { pemWriter ->
                pemWriter.writeObject(pemObject)
            }
            File(context.filesDir, this.privateKeyFileName).writeText(stringWriter.toString())
        }
    }

    /**
     * Converts a public key to an encrypted PEM format and saves it to a file.
     * @param publicKey The public key to encrypt.
     * @param roomKey The AES key to use for encryption.
     */
    fun convertPublicKeyToPemFormatAndStore(publicKey: PublicKey) {
        val pemObject = PemObject("RSA PUBLIC KEY", publicKey.encoded)
        StringWriter().use { stringWriter ->
            PemWriter(stringWriter).use { pemWriter ->
                pemWriter.writeObject(pemObject)
            }
            File(context.filesDir, this.publicKeyFileName).writeText(stringWriter.toString())
        }
    }

    fun loadPublicKey(): PublicKey {
        val inputStream = context.openFileInput(this.publicKeyFileName)
        val keyText = InputStreamReader(inputStream).use {
            it.readText()
                .replace("-----BEGIN RSA PUBLIC KEY-----\n", "")
                .replace("-----END RSA PUBLIC KEY-----", "")
        }
        val encoded = Base64.decode(keyText, Base64.DEFAULT)
        return KeyFactory.getInstance("RSA")
            .generatePublic(PKCS8EncodedKeySpec(encoded))
    }

    /**
     * it will basically retrieve the encrypted private key file, then decrypt it.
     *
     */

    fun loadPrivateKey(): PrivateKey {
        val encryptionKey = deriveKeyFromPin(this.pinCode)
        val inputStream = context.openFileInput(this.privateKeyFileName)
        val pemObject = PemReader(InputStreamReader(inputStream)).use { pemReader ->
            pemReader.readPemObject()
        }
        val encryptedData = pemObject.content
        val iv = encryptedData.sliceArray(0 until 12)
        val encryptedKey = encryptedData.sliceArray(12 until encryptedData.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, gcmSpec)
        val decodedKey = cipher.doFinal(encryptedKey)
        return KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(decodedKey))
    }
    /**
     * will store the Room key in the keystore.
     *
     * @param roomKey The AES key to store.
     * @param roomId The ID of the room associated with the key.
     * @param fileName The name of the file (not used in current implementation).

    fun storeSymmetricKey(roomKey: SecretKey, roomId: String, fileName: String) {
    val keyAlias = "roomKey$roomId"
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    // Store the provided roomKey in the KeyStore if it doesn't already exist
    if (!keyStore.containsAlias(keyAlias)) {
    val keyStoreEntry = KeyStore.SecretKeyEntry(roomKey)
    keyStore.setEntry(keyAlias, keyStoreEntry, null)
    }
    }

    /**
     * will get the appropriate Room key from the keystore.
     *
     * @param roomId The ID of the room associated with the key.
     * @return The AES room key, or null if not found.
    */
    fun getRoomKey(roomId: String): SecretKey? {
    val keyAlias = "roomKey$roomId"
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    return keyStore.getKey(keyAlias, null) as? SecretKey
    }    */
}
