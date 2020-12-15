package com.example.andro.Model

import com.google.gson.annotations.SerializedName

data class CreateClient (	@SerializedName("nameclient") val nameclient : String,
                             @SerializedName("image") val image : String,
                             @SerializedName("email") val email : String,
                             @SerializedName("typeProfile") val typeProfile : String,
                             @SerializedName("password") val password : String,
                             @SerializedName("cpf") val cpf : String,
                             @SerializedName("address") val address : String)