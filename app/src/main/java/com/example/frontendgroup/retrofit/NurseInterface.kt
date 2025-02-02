package com.example.frontendgroup.retrofit

import com.example.frontendgroup.stricturedata.Nurse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NurseInterface {

    @GET("nurses/all")
    suspend fun getAllNurses(): Response<List<Nurse>>
    @GET("nurses/find/{id}")
    suspend fun getNurseId(@Path("id") id: Int): Nurse
    @FormUrlEncoded
    @POST("nurses/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String): Response<Nurse>
    @FormUrlEncoded
    @POST("nurses/create")
    suspend fun register(@Field("username") username: String, @Field("password") password: String): Response<ResponseBody>
}