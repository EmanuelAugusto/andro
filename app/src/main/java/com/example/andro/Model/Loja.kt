package com.example.andro.Model

import  com.google.gson.annotations.SerializedName

data class Loja (
    @SerializedName("_id")
    val _id:String,
    @SerializedName("lojaNome")
    val lojaNome:String,
    @SerializedName("logo")
    val logo:String,
    @SerializedName("endereco")
    val endereco:String,
    @SerializedName("description")
    val description:String)