package com.match.matchmate.data.base

sealed class BaseUiState<out T> {
    object Loading : BaseUiState<Nothing>()
    data class Success<out T>(val data: T?) : BaseUiState<T>()
    data class Error(val error: ErrorResponse) : BaseUiState<Nothing>()
}