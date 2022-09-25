package com.bhavitbj.newsbreeze.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class NewsData {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("totalResults")
    @Expose
    var totalResults: Int? = null

    @SerializedName("articles")
    @Expose
    var articles: List<Article>? = null
}