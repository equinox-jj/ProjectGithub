package com.projectgithub.data.source.remote.network

import com.projectgithub.common.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(10L, TimeUnit.SECONDS)
        .connectTimeout(10L, TimeUnit.SECONDS)
        .build()

    private val gsonConverter = GsonConverterFactory.create()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverter)
        .build()

    val apiServices: ApiServices = retrofitBuilder.create(ApiServices::class.java)

}