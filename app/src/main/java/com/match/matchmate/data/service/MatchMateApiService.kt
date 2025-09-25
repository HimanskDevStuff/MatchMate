package com.match.matchmate.data.service

import com.match.matchmate.data.model.MatchMateDto
import retrofit2.Response
import retrofit2.http.GET

interface MatchMateApiService {

    @GET("api/?page=1&results=10")
    suspend fun getAllUsers(): Response<MatchMateDto>
}