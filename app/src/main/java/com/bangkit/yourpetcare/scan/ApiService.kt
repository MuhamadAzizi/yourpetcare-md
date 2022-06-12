package com.bangkit.yourpetcare.scan

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface ApiService {

    @Multipart
    @POST("/predict")
    fun predict(
        @Part file: MultipartBody.Part
    ): Call<ScanResponse>

    companion object {
        operator fun invoke(): ApiService {
            val logging = HttpLoggingInterceptor()
            val httpClient = OkHttpClient.Builder()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            httpClient.readTimeout(60, TimeUnit.SECONDS)

            return Retrofit.Builder()
                .baseUrl("https://yourpetcare-safmcqat4a-uc.a.run.app")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(ApiService::class.java)
        }
    }

}