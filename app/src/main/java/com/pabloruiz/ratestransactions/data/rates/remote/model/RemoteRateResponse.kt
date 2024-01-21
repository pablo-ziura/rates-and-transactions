package com.pabloruiz.ratestransactions.data.rates.remote.model

data class RemoteRateResponse(
    val from: String,
    val to: String,
    val rate: String
)
