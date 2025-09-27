package com.match.matchmate.data.service

import com.match.matchmate.data.model.MatchMateDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchMateApiService {

    @GET("api/")
    suspend fun getAllUsers(
        @Query("page" ) page: Int,
        @Query("results") results: Int
    ): Response<MatchMateDto>
}