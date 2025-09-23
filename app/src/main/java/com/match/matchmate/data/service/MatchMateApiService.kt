package com.match.matchmate.data.service

import com.match.matchmate.domain.model.Matchmate
import retrofit2.http.GET

interface MatchMateApiService {

    @GET("api/?page=1&result=20")
    suspend fun getAllUsers(): List<Matchmate>
}