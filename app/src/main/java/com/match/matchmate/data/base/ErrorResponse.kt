package com.match.matchmate.data.base

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("stack")
    val stack: String=""
)