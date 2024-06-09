package com.example.flame.ui.screens.chatroom

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


import com.example.flame.data.model.listOfMessages
import com.example.flame.data.viewModel.ChatViewModel
import com.example.flame.ui.composables.CircleImageView
import com.example.flame.ui.composables.MessageBox
import com.example.flame.ui.composables.MessageInputField
import com.example.flame.ui.screens.chatroom.ui.theme.FlameTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBarComposant(modifier: Modifier){
    val badgeSize = 10.dp // Size of the badge
    val badgePadding = 1.dp // Padding around the text inside the badge
    val badgeTextSize = 6.sp // Text size inside the badge
    val notificationCount = 2
    val context: Context = LocalContext.current.applicationContext
    TopAppBar(title = { Text(text = "Pablo", fontSize = 18.sp) }, scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        navigationIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back to the rooms lists")
                Spacer(modifier = modifier)
                IconButton(onClick = {  }) {
                    CircleImageView(40.dp)
                }
            }

        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),

        actions = {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { Toast.makeText(context, "Search", Toast.LENGTH_SHORT ).show() }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings",tint = Color.White
                    )
                }
            }

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val viewModel = ChatViewModel()

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        topBar = {
            ChatBarComposant(Modifier.padding(4.dp))

        },

        containerColor = Color.Transparent,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth().wrapContentHeight()

                    .weight(1f),
                reverseLayout = true,
            ) {
                items(listOfMessages) { message ->
                    MessageBox(message = message)
                }
            }

            MessageInputField(navController = navController, viewModel= viewModel)
        }
    }
}