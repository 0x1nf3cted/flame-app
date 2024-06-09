package com.example.flame.data.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _logged = MutableStateFlow(false)
    val logged = _logged.asStateFlow()


    fun changeAuthState(){
        _logged.value = !_logged.value
    }
}