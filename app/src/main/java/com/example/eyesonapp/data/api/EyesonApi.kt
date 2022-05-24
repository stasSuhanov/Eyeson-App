package com.example.eyesonapp.data.api
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface EyesonApi {

    @POST("/rooms")
    fun getLinks(@Header("Authorization") api_key: String, @Query("user[name]") user: String): Single<Room>
}