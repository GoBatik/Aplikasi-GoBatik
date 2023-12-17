package com.example.gobatik.retrofit

import com.example.gobatik.response.ImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ServiceApi {
    @Multipart
    @POST("v1/predict")
    suspend fun postImage(
        @Part file: MultipartBody.Part
    ): ImageResponse
}