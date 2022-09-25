package com.bhavitbj.newsbreeze.api

import com.bhavitbj.newsbreeze.model.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("top-headlines")
    fun getNewsData(
        @Query("country") country: String?,
        @Query("apiKey") apikey: String?
    ): Call<NewsData?>?
}