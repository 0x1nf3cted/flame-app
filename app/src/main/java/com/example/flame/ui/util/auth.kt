package com.example.flame.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.navigation.NavController
import com.example.flame.Chat
import com.example.flame.MainActivity
import com.example.flame.data.model.LoginCredentials

fun checkCredentials(creds: LoginCredentials, navController: NavController, context: Context): Boolean {
    // should later send a request for the server to check for credentials
    return if (creds.isNotEmpty() && creds.username == "a") {
        navController.navigate(Chat)
        true
    } else {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        false
    }
}