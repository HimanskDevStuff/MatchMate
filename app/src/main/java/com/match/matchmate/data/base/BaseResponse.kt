package com.match.matchmate.data.base

import com.google.gson.annotations.SerializedName
import com.match.matchmate.data.base.ErrorResponse

data class BaseResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("version")
    val version: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("data")
    val `data`: T,
    @SerializedName("error")
    val error: ErrorResponse
    )