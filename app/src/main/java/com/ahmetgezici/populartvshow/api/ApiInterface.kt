package com.ahmetgezici.populartvshow.api

import com.ahmetgezici.populartvshow.model.populartv.PopularTV
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("tv/popular?")
    fun getPopularTV(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Single<PopularTV>

    @GET("tv/{id}?")
    fun getDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    )


}