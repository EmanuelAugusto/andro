package com.example.andro.Model

import com.google.gson.annotations.SerializedName

data class userPaymentFood(

    @SerializedName("cardNumberClient") val cardNumberClient: String,
    @SerializedName("nameHolder") val nameHolder: String,
    @SerializedName("expDate") val expDate: String,
    @SerializedName("idFood") val idFood: String,
    @SerializedName("idSalesman") val idSalesman: String,
    @SerializedName("price") val price: Int,
    @SerializedName("quantity") val quantity: Int
)