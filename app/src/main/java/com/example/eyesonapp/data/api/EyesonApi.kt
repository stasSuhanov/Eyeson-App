package com.example.eyesonapp.data.api
import retrofit2.http.POST
import retrofit2.http.Query

const val ENDPOINT_ROOMS = "/rooms"
const val ARG_USERNAME = "user[name]"

interface EyesonApi {

    @POST(ENDPOINT_ROOMS)
    suspend fun getLinks(@Query(ARG_USERNAME) user: String): RoomResponse
}