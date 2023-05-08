package com.dicoding.intermediate.submissionstoryapp.data.api

import com.dicoding.intermediate.submissionstoryapp.data.response.AddStoryResponse
import com.dicoding.intermediate.submissionstoryapp.data.response.LoginResponse
import com.dicoding.intermediate.submissionstoryapp.data.response.RegisterResponse
import com.dicoding.intermediate.submissionstoryapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    fun getListStory(
        @Header("Authorization") bearer: String?
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun postNewStory(
        @Header("Authorization") bearer: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
    ): Call<AddStoryResponse>

    @FormUrlEncoded
    @POST("register")
    fun doRegister(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun doLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>
}