package com.pabloruiz.ratestransactions.data.rates.remote

class RatesRemoteImpl(private val ratesService: RatesService) {
    suspend fun getRatesInfo() = ratesService.getRatesInfo()
}