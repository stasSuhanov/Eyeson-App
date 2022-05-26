package com.example.eyesonapp.data.api

import com.google.gson.annotations.SerializedName

data class RoomResponse(
    @SerializedName("access_key") val accessKey: String? = null,
    @SerializedName("room") val room: Room? = null,
)

data class Room(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("guest_token") val guestToken: String? = null,
)