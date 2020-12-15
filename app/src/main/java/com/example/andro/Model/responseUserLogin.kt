package com.example.andro.Model

import com.google.gson.annotations.SerializedName

data class responseUserLogin (
    @SerializedName("msg")
    val msg: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("dataUser")
    val dataUser: DataUser
)