package com.example.flame.data.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
 import com.example.flame.data.model.Message
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel(){
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    fun addMessage(content: String, isMe: Boolean, lastIntQueue: Boolean) {
        val message = Message(
        message = content,
        time =System.currentTimeMillis().toString(),
        isMe = isMe
        )
        _messages.add(message)
    }

    fun loadMessages() {
        // This function could be used to load initial messages from a repository or database
        viewModelScope.launch {
            // Simulate loading messages
            val loadedMessages = listOf<Message>(
                Message("Hi there!", "9:00 AM", true),
                Message("Hello!", "9:02 AM", false),
                Message("How are you?", "9:05 AM", true),
                Message("I'm doing well, thanks!", "9:07 AM", false),
                Message("What have you been up to? You've been gone lately", "9:10 AM", true),
                Message("Just working on some stuff.", "9:12 AM", false),
                Message("That sounds busy!", "9:15 AM", true),
                Message("And tiring!", "9:15 AM", true)
            )
            _messages.addAll(loadedMessages)
        }
    }

    private fun generateId(): String {
        return _messages.size.toString()
    }
}