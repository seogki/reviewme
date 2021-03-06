package com.skh.reviewme.Network

import com.skh.reviewme.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Seogki on 2018. 6. 26..
 */
open class ApiCilentRx() {

    //회사용
    companion object {

        fun create(): ApiInterface {

            val interceptor = HttpLoggingInterceptor()

            if (BuildConfig.DEBUG) {
                //debug
                interceptor.level = HttpLoggingInterceptor.Level.BODY

            } else {
                //release
                interceptor.level = HttpLoggingInterceptor.Level.NONE

            }

            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient
                    .Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()

            val retrofit = Retrofit
                    .Builder()
                    .baseUrl("http://192.168.1.18:6327/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()


            return retrofit.create(ApiInterface::class.java)
        }
    }

}