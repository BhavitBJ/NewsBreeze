package com.bhavitbj.newsbreeze.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient private constructor() {
    init {
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
    }

    val api: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    companion object {
        private lateinit var retrofit: Retrofit
        private var mInstance: ApiClient? = null

        @get:Synchronized
        val instance: ApiClient?
            get() {
                if (mInstance == null) {
                    mInstance = ApiClient()
                }
                return mInstance
            }
    }
}