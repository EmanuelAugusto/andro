package com.example.andro.Model

import com.example.andro.Fragments.salesman
import com.google.gson.annotations.SerializedName

data class PaymentsResponse(
    @SerializedName("msg")
    val msg:  String,
    @SerializedName("code")
    val code:  String,
    @SerializedName("payment")
    val payment: ArrayList<Payments>
)

data class Payments (
    @SerializedName("idFood")
    val idFood : List<foodPayment>,
    @SerializedName("idClient")
    val user : List<foodUser>,
    /*,
    @SerializedName("foodUser")
    val salesman: Owner,*/
    @SerializedName("status")
    val status:  List<String>,
    @SerializedName("_id")
    val id:  String,
    @SerializedName("price")
    val price:  Float,
    @SerializedName("quantity")
    val quantity:  Int,
    @SerializedName("dataCreated")
    val dataCreated:  String
)