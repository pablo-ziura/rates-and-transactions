package com.pabloruiz.ratestransactions.data.rates.remoteService

import com.pabloruiz.ratestransactions.data.networkClient.ApiClient

class RatesRemoteImpl(private val apiClient: ApiClient) {
    private val ratesService = apiClient.retrofit.create(RatesService::class.java)

    suspend fun getRatesInfo() = ratesService.getRatesInfo()
}