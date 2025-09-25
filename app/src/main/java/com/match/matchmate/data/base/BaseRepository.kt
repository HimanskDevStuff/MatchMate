package com.match.matchmate.data.base

import com.match.matchmate.data.model.MatchMateDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import saathi.core.service.InternetChecker
import java.net.SocketTimeoutException
import javax.inject.Inject

open class BaseRepository @Inject constructor(
    private val internetChecker: InternetChecker
) {
    fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> Response<T>
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
        if (response.isSuccessful) {
            emit(BaseUiState.Success(response.body()))
        } else {
            val errorBody = response.errorBody()?.string()
            emit(
                BaseUiState.Error(
                    ErrorResponse(
                        code = response.code().toString(),
                        message = errorBody ?: "Unknown error"
                    )
                )
            )
        }
    }.catch { e ->
        e.printStackTrace()
        val errorResponse = when (e) {
            is HttpException -> {
                val body = e.response()?.errorBody()?.string()
                ErrorResponse(message = body ?: "Http exception")
            }
            is SocketTimeoutException -> ErrorResponse(message = "Connection timed out")
            else -> ErrorResponse(message = e.message ?: "Unknown error")
        }
        emit(BaseUiState.Error(errorResponse))
    }.flowOn(dispatcher)

}