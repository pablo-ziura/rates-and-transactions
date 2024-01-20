package com.pabloruiz.ratestransactions.data.networkClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        private const val BASE_URL = "https://sga.adivet.com/"
    }
}