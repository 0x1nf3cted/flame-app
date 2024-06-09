package com.example.flame.data.model


data class LoginCredentials(
    var username: String = "",
    var password: String = "",
) {
    fun isNotEmpty(): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}


data class SignupCredentials(
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var public_key: String = "",
) {
    fun isNotEmpty(): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}
