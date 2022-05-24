package com.example.eyesonapp.data.api

data class Room(
    val links: Links
)

data class Links(
    val gui: String,
    val guest_join: String,
)