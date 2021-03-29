package com.ahmetgezici.populartvshow.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {

        private const val BaseUrl = "https://api.themoviedb.org/3/"
        private var retrofit: Retrofit? = null

        const val apiKey = "31764e19904c47ea051d1b1506a0c809"

        const val ImageUrl_original = "https://image.tmdb.org/t/p/original"
        const val ImageUrl_w500 = "https://image.tmdb.org/t/p/w500"


        fun getClient(): Retrofit? {

            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient())
                    .build()

            }

            return retrofit
        }
    }
}