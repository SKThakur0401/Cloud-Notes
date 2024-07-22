package com.example.a15_notes_app.data.model

import com.google.gson.annotations.SerializedName

data class UserAndToken(
    @SerializedName("token")
    val token: String,

    @SerializedName("user")
    val user: User
)