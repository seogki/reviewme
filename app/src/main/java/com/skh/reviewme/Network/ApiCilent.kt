package com.skh.reviewme.Network


/**
 * Created by Seogki on 2018. 6. 26..
 */
//open class ApiCilent() {
//
//    //회사용
//    var BASE_URL: String = "http://192.168.1.18:6327/"
//
//    private var apiInterface: ApiInterface
//
//    companion object {
//        fun getInstance(): ApiCilent {
//            return ApiCilent()
//        }
//    }
//
//    fun getService(): ApiInterface {
//        return apiInterface
//    }
//
//    init {
//
//
//        val intercepter = HttpLoggingInterceptor()
//
//        if (BuildConfig.DEBUG) {
//            //debug
//            intercepter.level = HttpLoggingInterceptor.Level.BODY
//
//        } else {
//            //release
//            intercepter.level = HttpLoggingInterceptor.Level.NONE
//
//        }
//        intercepter.level = HttpLoggingInterceptor.Level.BODY
//        val client = OkHttpClient
//                .Builder()
//                .addInterceptor(intercepter)
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
//                .build()
//        val retrofit = Retrofit
//                .Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client)
//                .build()
//
//
//        apiInterface = retrofit.create(ApiInterface::class.java)
//    }
//
//}