package com.match.matchmate.data.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("code")
    val code: String = "",
    @SerialName("message")
    val message: String? = null,
    @SerialName("stack")
    val stack: String=""
)