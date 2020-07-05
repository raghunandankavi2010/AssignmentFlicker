package com.example.flcikersample.api

import com.example.flcikersample.data.models.FlickerItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerService {

    @GET("services/rest")
    suspend fun searchFlickerImage(
        @Query("method") method: String,
        @Query("api_key") api_key: String,
        @Query("text") query: String,
        @Query("format") jsonFormat: String,
        @Query("nojsoncallback") nojsoncallback: Int,
        @Query("per_page") perPage: Int,
        @Query("page") page_size: Int
    ): Response<FlickerItem>

}