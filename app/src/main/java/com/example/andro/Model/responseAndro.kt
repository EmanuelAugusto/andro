package com.example.andro.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class responseAndro (
    @Expose
    @SerializedName("msg")
    val msg: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("dataUser")
    val dataUser: DataUser)