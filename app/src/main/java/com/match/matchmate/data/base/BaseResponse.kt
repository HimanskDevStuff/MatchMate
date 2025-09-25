package com.match.matchmate.data.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("version")
    val version: String,
    @SerialName("date")
    val date: String,
    @SerialName("data")
    val `data`: T,
    @SerialName("error")
    val error: ErrorResponse
)