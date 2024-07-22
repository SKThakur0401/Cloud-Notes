package com.example.a15_notes_app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("__v")
    val __v: String,

    @SerializedName("_id")
    val _id: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("username")
    val username: String
)