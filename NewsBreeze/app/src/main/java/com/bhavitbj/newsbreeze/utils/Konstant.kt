package com.bhavitbj.newsbreeze.utils

object Konstant {

    const val KEY_SOURCE_NAME ="SOURCE_NAME"
    const val KEY_AUTHOR ="AUTHOR"
    const val KEY_TITLE = "TITLE"
    const val KEY_DESCRIPTION = "DESCRIPTION"
    const val KEY_IMAGE_URL = "IMAGE_URL"
    const val KEY_CONTENT_URL = "CONTENT_URL"
    const val KEY_CONTENT = "CONTENT"
    const val KEY_DATE ="DATE"


    fun escapingString(str: String): String {
        return str.replace("'", "''")
    }
}