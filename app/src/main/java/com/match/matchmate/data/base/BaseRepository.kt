package com.match.matchmate.data.base

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import saathi.core.service.InternetChecker
import java.net.SocketTimeoutException
import javax.inject.Inject

open class BaseRepository @Inject constructor(
    private val internetChecker: InternetChecker
) {
    fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> BaseResponse<T>
    ): Flow<BaseUiState<T?>> = flow {
        if (!internetChecker.isInternetAvailable) {
            emit(
                BaseUiState.Error(
                    ErrorResponse(
                        code = "1008",
                        message = "No internet connection"
                    )
                )
            )
            return@flow
        }
        emit(BaseUiState.Loading)
        val response = apiCall()
        if (response.success) {
            val data = response.data
            emit(BaseUiState.Success(data))
        } else {

            val error = response.error
            try {
                emit(BaseUiState.Error(error))
            } catch (e: Exception) {
                emit(BaseUiState.Error(ErrorResponse(message = "Failed to parse error message")))
            }
        }
    }.catch { e ->
        e.printStackTrace()
        try {
            when (e) {
                is HttpException -> {
                    val body = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(body, BaseResponse::class.java)
                    emit(BaseUiState.Error(errorResponse.error))
                }

                is SocketTimeoutException -> {
                    emit(BaseUiState.Error(ErrorResponse(message = "Connection timed out")))
                }

                else -> {
                    emit(BaseUiState.Error(ErrorResponse(message = e.message.toString())))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(BaseUiState.Error(ErrorResponse(message = e.message.toString())))
        }
    }.flowOn(dispatcher)

    fun <T : Any> safeApiCallWithBaseResponse(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> BaseResponse<T>
    ): Flow<BaseUiState<BaseResponse<T>?>> = flow {

        if (!internetChecker.isInternetAvailable) {
            emit(BaseUiState.Error(ErrorResponse(message = "No internet connection")))
            return@flow
        }
        emit(BaseUiState.Loading)
        val response = apiCall()
        if (response.success) {
            emit(BaseUiState.Success(response))
        } else {
            val error = response.error
            try {

                emit(BaseUiState.Error(error))
            } catch (e: Exception) {
                emit(BaseUiState.Error(ErrorResponse(message = "Failed to parse error message")))
            }
        }
    }.catch { e ->
        try {
            if (e is HttpException) {
                val body = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(body, BaseResponse::class.java)
                emit(BaseUiState.Error(errorResponse.error))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(BaseUiState.Error(ErrorResponse(message = e.message.toString())))
        }
    }.flowOn(dispatcher)
}