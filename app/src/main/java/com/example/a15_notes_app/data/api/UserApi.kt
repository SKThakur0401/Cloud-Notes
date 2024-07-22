package com.example.a15_notes_app.data.api

import com.example.a15_notes_app.data.model.UserAndToken
import com.example.a15_notes_app.data.model.UserLoginData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("user/signup")
    suspend fun signup(@Body data:UserLoginData): Response<UserAndToken>

    @POST("user/signin")
    suspend fun signin(@Body data:UserLoginData): Response<UserAndToken>

}