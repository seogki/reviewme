package com.skh.reviewme.Network

import com.skh.reviewme.Main.model.ReviewFragmentModel
import com.skh.reviewme.Main.model.ReviewFragmentModels
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Seogki on 2018. 6. 26..
 */
interface ApiInterface {


    @POST("api/RegisterAccount")
    fun registerAccount(@Query("Nick") nickname: String
                        , @Query("Email") email: String
                        , @Query("Age") age: String
                        , @Query("Gender") gender: String
                        , @Query("isKakao") isKakao: String)
            : Call<JSONObject>


    @POST("api/GetReviewItem")
    fun GetReviewItem():Call<ReviewFragmentModel>

    @POST("api/GetReviewItem2")
    fun GetReviewItem2():Call<ReviewFragmentModels>
}