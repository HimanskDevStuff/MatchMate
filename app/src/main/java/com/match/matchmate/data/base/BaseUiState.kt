package com.match.matchmate.data.base

sealed class BaseUiState<out T : Any?> {

    object Loading : BaseUiState<Nothing>()
    data class Success<out T : Any>(val data: T) : BaseUiState<T>()
    data class Error(val errorResponse: ErrorResponse) : BaseUiState<Nothing>()
}