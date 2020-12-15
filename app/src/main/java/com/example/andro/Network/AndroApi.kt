package com.example.andro.Network

import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Model.*
import com.example.andro.getToken
import retrofit2.Call
import retrofit2.http.*

interface AndroApi{
    val context: AppCompatActivity

    @GET("allfood")
    fun getFood(@Header("Authorization") token: String):Call<ArrayList<Food>>

    @GET("loja")
    fun getLoja():Call<ArrayList<Loja>>

    @POST("searchsalesman")
    fun getLojaSearch(@Body nameSalesman: Owner):Call<ArrayList<Owner>>

    @GET("food/{id}")
    fun getOneFood(@Path(value="id")id:String):Call<Food>


    @GET("salesman/{id}")
    fun getProfileSalesman(@Path(value="id")id:String):Call<Owner>

    @GET("foodsbysalesman/{id}")
    fun getFoodsBySalesman(@Path(value="id")id:String):Call<ArrayList<Food>>

    @POST("createClient")
    fun createUserClient(@Body client: CreateClient):Call<responseAndro>

    @POST("loginClient")
    fun loginClient(@Body client: requestLogin):Call<responseUserLogin>

    @GET("payments")
    fun getPayments(@Header("Authorization") token: String):Call<PaymentsResponse>

    @GET("meClient")
    fun getMe(@Header("Authorization") token: String):Call<responseUser>

    @POST("payment")
    fun payFood(@Header("Authorization") token: String, @Body pay: userPaymentFood):Call<PaymentsResponse>

    @PUT("editProfileClient")
    fun editProfileClient(@Header("Authorization") token: String, @Body pay: CreateClient):Call<responseUser>
}