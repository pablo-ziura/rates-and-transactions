package com.pabloruiz.ratestransactions.data.transactions.remote

import com.pabloruiz.ratestransactions.data.transactions.remote.model.RemoteTransactionResponse
import retrofit2.Response
import retrofit2.http.GET

interface TransactionsService {
    @GET("transactions.json")
    suspend fun getTransactionsInfo(): Response<List<RemoteTransactionResponse>>
}