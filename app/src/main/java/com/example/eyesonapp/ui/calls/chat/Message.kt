package com.example.eyesonapp.ui.calls.chat

data class Message(
    val userName: String,
    val message: String,
    val formattedDate: String,
    val isMine: Boolean,
)