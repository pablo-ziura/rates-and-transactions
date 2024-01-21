package com.pabloruiz.ratestransactions.data.transactions.remote

class TransactionsRemoteImpl(private val transactionsService: TransactionsService) {
    suspend fun getTransactionsInfo() = transactionsService.getTransactionsInfo()
}