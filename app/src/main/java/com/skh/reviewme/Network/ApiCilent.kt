package com.skh.reviewme.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



/**
 * Created by Seogki on 2018. 6. 26..
 */
open class ApiCilent() {


    var BASE_URL: String = "http://192.168.1.21:6327/"

    private var apiInterface: ApiInterface
    companion object {
        fun getInstance(): ApiCilent{
            return ApiCilent()
        }
    }

    fun getService(): ApiInterface {
        return apiInterface
    }

    init {


        val intercepter = HttpLoggingInterceptor()
        intercepter.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(intercepter).build()
        val retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


        apiInterface = retrofit.create(ApiInterface::class.java)
    }

}