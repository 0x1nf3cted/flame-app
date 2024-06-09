package com.example.flame.data.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4



import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)class KeyPairGenerationTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun testGenerateKeyPair() {
        val keyPair = KeyPairGeneration().generateKeyPair()
        // Ensure that key pair is not null
        assert(keyPair != null)
    }

    @Test
    fun testGenerateRoomKey() {
        val roomKey = KeyPairGeneration().generateRoomKey()
        // Ensure that room key is not null
        assert(roomKey != null)
    }

    @Test
    fun testEncryptDecryptRoomKey() {
        val keyPairGeneration = KeyPairGeneration()
        val roomKey = keyPairGeneration.generateRoomKey()
        val publicKey = keyPairGeneration.generateKeyPair().public

        val encryptedRoomKey = keyPairGeneration.encryptRoomKey(roomKey, publicKey)
        val decryptedRoomKey = keyPairGeneration.decryptRoomKey(context, encryptedRoomKey)

        // Ensure that decrypted room key matches the original room key
        assertEquals(roomKey, decryptedRoomKey)
    }

    @Test
    fun testEncryptDecryptMessage() {
        val keyPairGeneration = KeyPairGeneration()
        val roomKey = keyPairGeneration.generateRoomKey()
        val message = "Test message"
        val roomId = "testRoomId"

        val encryptedMessage = keyPairGeneration.encryptMessage(context, message, roomId)
        val decryptedMessage = keyPairGeneration.decryptMessage(context, encryptedMessage, roomKey.encoded)

        // Ensure that decrypted message matches the original message
        assertEquals(message, decryptedMessage)
    }
}
