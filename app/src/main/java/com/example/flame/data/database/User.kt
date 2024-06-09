package com.example.flame.data.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val username: String,
    val private_key: String,
    @PrimaryKey() val id: String,
)
