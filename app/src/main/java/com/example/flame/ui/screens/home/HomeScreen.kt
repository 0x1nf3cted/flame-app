package com.example.flame.ui.screens.home

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flame.Chat
import com.example.flame.R
import com.example.flame.ui.composables.CircleImageView



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComposant(){
    val badgeSize = 10.dp // Size of the badge
    val badgePadding = 1.dp // Padding around the text inside the badge
    val badgeTextSize = 6.sp // Text size inside the badge
    val notificationCount = 2
    val context: Context = LocalContext.current.applicationContext
    TopAppBar(title = { Text(text = "Flame") },
        navigationIcon = {
            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "User profile")
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
                IconButton(onClick = { Toast.makeText(context, "Notification", Toast.LENGTH_SHORT ).show() }) {
                    Icon(painter = painterResource(id = R.drawable.icon), contentDescription = "Notification",tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .size(badgeSize)
                        .offset(x = (-20).dp, y = (20).dp)
                        .background(Color.Red, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Draw the notification count inside the circle
                    if (notificationCount > 0) {
                        val badgeTextColor = Color.White
                        val text = if (notificationCount > 99) "99+" else notificationCount.toString()

                        Text(
                            text = text,
                            textAlign = TextAlign.Center,
                            color = badgeTextColor,
                            fontSize = badgeTextSize,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(badgePadding)
                        )
                    }
                }
                IconButton(onClick = { Toast.makeText(context, "Search", Toast.LENGTH_SHORT ).show() }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search",tint = Color.White
                    )
                }
            }

        }
    )
}



@Composable
fun ChatRoomList(navController: NavController, rooms: List<String>){
    if(rooms.isEmpty()){
        Text(text = "No chat rooms",
            textAlign = TextAlign.Center,
        )
    }else{
        Column(
            Modifier.background(Color.Black)
        ) {
            Text(text = "Recent messages", modifier = Modifier.padding(16.dp, 0.dp,0.dp, 10.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(items = rooms) { room ->
                    ChatRoom(navController, room)
                }
            }
        }

    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoom(navController: NavController, name: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        shape = RectangleShape,
        onClick = { navController.navigate(Chat) }
    ) {
        CardContent(name)
    }
}


@Composable
private fun CardContent(name: String) {
    val hasNotification by rememberSaveable { mutableStateOf(false) }
    val borderColor = Color.Gray
    val borderWidth = 2f
    Row(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .drawBehind {
                val borderHeight = borderWidth / density
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height - borderHeight / 2),
                    end = Offset(
                        size.width,
                        size.height - borderHeight / 2
                    ),
                    strokeWidth = borderHeight
                )
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleImageView(50.dp)
        Column(
            modifier = Modifier
                .weight(0.2f)
                .padding(12.dp, 0.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = "Hello",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "4m", color = Color.Gray, fontSize = 8.sp)
            Spacer(modifier = Modifier.height(16.dp))
            if(hasNotification){
                NotificationBadge(2)
            }
        }

    }
}





@Composable
fun NotificationBadge(notificationCount: Int) {
    val badgeSize = 16.dp // Size of the badge
    val badgePadding = 1.dp // Padding around the text inside the badge
    val badgeTextSize = 10.sp // Text size inside the badge

    Box(contentAlignment = Alignment.Center) {
        // Red circle background
        Box(
            modifier = Modifier
                .size(badgeSize)
                .background(Color.Red, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Draw the notification count inside the circle
            if (notificationCount > 0) {
                val badgeTextColor = Color.White
                val text = if (notificationCount > 99) "99+" else notificationCount.toString()

                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    color = badgeTextColor,
                    fontSize = badgeTextSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(badgePadding)
                )
            }
        }
    }
}




@Composable
fun HomeScreen(navController: NavController) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(false) }
    val rooms: List<String> = listOf("Pablo", "Jack", "Moussa", "Tayeb", "John", "Joe", "Ron", "Jack", "Moussa", "Tayeb", "John", "Jack", "Moussa", "Tayeb", "John")
    Column() {
        TopBarComposant()
        ChatRoomList(navController, rooms)
    }
}