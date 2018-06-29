package com.skh.reviewme.Network

import com.skh.reviewme.Main.model.ReviewFragmentModels
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Created by Seogki on 2018. 6. 26..
 */
interface ApiInterface {

    @POST("api/isUserOn")
    fun isUserOn(@Query("UserId") userid: String): Call<JSONObject>

    @POST("api/RegisterAccount")
    fun registerAccount(@Query("UserId") userid: String
                        , @Query("Nick") nickname: String
                        , @Query("Email") email: String
                        , @Query("Age") age: String
                        , @Query("Gender") gender: String
                        , @Query("isKakao") isKakao: String)
            : Call<JSONObject>

    @POST("api/GetReviewItem2")
    fun GetReviewItem2(): Call<ReviewFragmentModels>

    @POST("api/SetReviewPhotos")
    @Multipart
    fun SetReviewPhotos(@Part("review_titles") titles: RequestBody
                        , @Part("review_texts") texts: RequestBody
                        , @Part file: MultipartBody.Part): Call<JSONObject>
}