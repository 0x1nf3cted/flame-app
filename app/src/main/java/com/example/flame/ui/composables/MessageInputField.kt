package com.example.flame.ui.composables


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flame.data.viewModel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputField(navController: NavController, viewModel: ChatViewModel) {
    var message: String = remember { mutableStateOf("") }.value

    TextField(
        value = message,
        onValueChange = {
            message = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .wrapContentHeight()
            .navigationBarsPadding()
            .imePadding(),
        textStyle = TextStyle(
            color = Color(0xFFCCCCCC),
            fontSize = 16.sp,
        ),
        placeholder = {
            Text(
                text = "Type a message...",
                style = TextStyle(
                    color = Color(0xFFCCCCCC),
                    fontSize = 16.sp,
                )
            )
        },
        leadingIcon = {
            IconButton(
                onClick = { navController.navigate("camera") },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFFCCCCCC)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { viewModel.addMessage(content = message, isMe = true, lastIntQueue = true) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFFCCCCCC)
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFF2B2B2B),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}