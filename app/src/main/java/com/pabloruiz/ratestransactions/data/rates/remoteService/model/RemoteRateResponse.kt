package com.pabloruiz.ratestransactions.data.rates.remoteService.model

data class RemoteRateResponse(
    val from: String,
    val to: String,
    val rate: String
)
