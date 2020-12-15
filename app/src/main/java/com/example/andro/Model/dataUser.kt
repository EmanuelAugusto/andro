package com.example.andro.Model

import com.google.gson.annotations.SerializedName

data class responseUser (    @SerializedName("msg")
                             val msg:  String,
                             @SerializedName("code")
                             val code:  String,
                             @SerializedName("dataUser") val dataUser : DataUser)


data class DataUser (	@SerializedName("userId") val userId : String,
                         @SerializedName("user") val user : CreateClient)