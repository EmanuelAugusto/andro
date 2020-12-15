package com.example.andro.Model

import com.google.gson.annotations.SerializedName

data class Owner(	@SerializedName("erased") val erased : Int,
                     @SerializedName("_id") val _id : String,
                     @SerializedName("nameSalesman") val nameSalesman : String,
                     @SerializedName("image") val image : String,
                     @SerializedName("email") val email : String,
                     @SerializedName("typeProfile") val typeProfile : String,
                     @SerializedName("cpforCnpj") val cpforCnpj : String,
                     @SerializedName("address") val address : String,
                     @SerializedName("__v") val __v : Int
)