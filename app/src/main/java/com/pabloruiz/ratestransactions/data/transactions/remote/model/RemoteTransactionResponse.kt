package com.pabloruiz.ratestransactions.data.transactions.remote.model

data class RemoteTransactionResponse(
    val sku: String,
    val amount: String,
    val currency: String
)
