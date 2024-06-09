package com.example.flame

import android.annotation.SuppressLint

import android.os.Bundle
import android.view.WindowManager

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flame.data.viewModel.MainViewModel
import com.example.flame.ui.screens.auth.ConfirmationScreen
import com.example.flame.ui.screens.auth.LoginScreen
import com.example.flame.ui.screens.auth.SignupScreen

import com.example.flame.ui.screens.chatroom.ChatScreen
import com.example.flame.ui.screens.home.HomeScreen
import com.example.flame.ui.theme.FlameTheme
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            val navController = rememberNavController()

            FlameTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color(0xFF1C1B1B)
                ) {
                    NavHost(navController = navController, startDestination = Home) {

                        composable<Signup> {
                            SignupScreen(navController = navController)
                        }
                        composable<Confirmation> {
                            ConfirmationScreen(navController = navController)
                        }
                        composable<Login> {
                            LoginScreen(navController = navController)
                        }
                        composable<Home>  {
                            HomeScreen(navController = navController)
                        }
                        composable<Chat> {
                            ChatScreen(navController = navController)
                        }
                    }
                }

            }
        }
    }
}


@Serializable
object Home

@Serializable
object Login

@Serializable
object Signup

@Serializable
object Confirmation


@Serializable
object Chat




