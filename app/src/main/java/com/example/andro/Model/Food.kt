package com.example.andro.Model

import  com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("description") val description : String,
    @SerializedName("erased") val erased : Int,
    @SerializedName("owner") val owner : List<Owner>,
    @SerializedName("status") val status : List<String>,
    @SerializedName("_id") val _id : String,
    @SerializedName("idSalesman") val idSalesman : String,
    @SerializedName("nome") val nome : String,
    @SerializedName("imagem") val imagem : String,
    @SerializedName("loja") val loja : String,
    @SerializedName("preco") val preco : Double,
    @SerializedName("dataCriacao") val dataCriacao : String,
    @SerializedName("__v") val __v : Int)