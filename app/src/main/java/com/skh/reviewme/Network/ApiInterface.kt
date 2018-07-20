package com.skh.reviewme.Network

import com.google.gson.JsonObject
import com.skh.reviewme.Community.model.CommunityInnerCommentModels
import com.skh.reviewme.Community.model.CommunityInnerModel
import com.skh.reviewme.Community.model.CommunityModels
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

    /**
     *  유저 로그인
     */

    @POST("api/isUserOn")
    fun isUserOn(@Query("UserId") userid: String): Call<JsonObject>

    @POST("api/RegisterAccount")
    fun registerAccount(@Query("UserId") userid: String
                        , @Query("Nick") nickname: String
                        , @Query("Email") email: String
                        , @Query("Age") age: String
                        , @Query("Gender") gender: String
                        , @Query("isKakao") isKakao: String)
            : Call<JsonObject>


    @POST("api/RegisterAccountImage")
    @Multipart
    fun registerAccountImage(@Part("UserId") userid: RequestBody
                             , @Part("Nick") nickname: RequestBody
                             , @Part("Email") email: RequestBody
                             , @Part("Age") age: RequestBody
                             , @Part("Gender") gender: RequestBody
                             , @Part("isKakao") isKakao: RequestBody
                             , @Part file: MultipartBody.Part): Call<JsonObject>


    /**
     *  리뷰 메인
     */

    @POST("api/GetReviewItem2")
    fun GetReviewItem2(): Call<ReviewFragmentModels>

    @POST("api/ScrollGetReviewItem2")
    fun ScrollGetReviewItem2(@Query("ReviewId") reviewid: String): Call<ReviewFragmentModels>

    @POST("api/GetSearchedReviewItem2")
    fun GetSearchedReviewItem2(@Query("UserId") userid: String,
                               @Query("SearchText") searchtext: String): Call<ReviewFragmentModels>

    @POST("api/GetScrollSearchedReviewItem2")
    fun GetScrollSearchedReviewItem2(@Query("UserId") userid: String,
                                     @Query("SearchText") searchtext: String,
                                     @Query("ReviewId") reviewid: String): Call<ReviewFragmentModels>

    @POST("api/SetReviewPhotos")
    @Multipart
    fun SetReviewPhotos(@Part("UserId") userid: RequestBody
                        , @Part("review_titles") titles: RequestBody
                        , @Part("review_texts") texts: RequestBody
                        , @Part file: MultipartBody.Part): Call<JSONObject>

    /**
     *  커뮤니티 메인
     */
    @POST("api/SetCommunityPhotos")
    @Multipart
    fun setCommunityPhotos(@Part("UserId") userid: RequestBody
                           , @Part("UserNick") usernick: RequestBody
                           , @Part("QuestionTitle") questiontitle: RequestBody
                           , @Part("QuestionText") questiontext: RequestBody
                           , @Part file: ArrayList<MultipartBody.Part>): Call<JsonObject>


    @POST("api/GetCommunityItem")
    fun GetCommunityItem(): Call<CommunityModels>

    @POST("api/ScrollGetCommunityItem")
    fun ScrollGetCommunityItem(@Query("CommunityId") communityid: String): Call<CommunityModels>

    @POST("api/GetSearchedCommunityItem")
    fun GetSearchedCommunityItem(@Query("UserId") userid: String,
                                 @Query("SearchText") searchtext: String): Call<CommunityModels>

    @POST("api/GetScrollSearchedCommunityItem")
    fun GetScrollSearchedCommunityItem(@Query("UserId") userid: String,
                                       @Query("SearchText") searchtext: String,
                                       @Query("CommunityId") communityid: String): Call<CommunityModels>

    @POST("api/GetInnerCommunityItem")
    fun GetInnerCommunityItem(@Query("CommunityId") communityid: String): Call<CommunityInnerModel>

    @POST("api/GetInnerCommunityComment")
    fun GetInnerCommunityComment(@Query("CommunityId") communityid: String): Call<CommunityInnerCommentModels>

    @POST("api/SetInnerCommunityComment")
    fun SetInnerCommunityComment(@Query("UserId") userid: String
                                 , @Query("CommunityId") communityid: String
                                 , @Query("UserNick") username: String
                                 , @Query("Comment") comment: String
                                 , @Query("UserImage") userimage: String): Call<JsonObject>
}