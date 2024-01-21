package com.pabloruiz.ratestransactions.data.transactions

import com.pabloruiz.ratestransactions.data.transactions.remote.TransactionsRemoteImpl
import com.pabloruiz.ratestransactions.data.transactions.remote.model.RemoteTransactionResponse
import com.pabloruiz.ratestransactions.domain.TransactionsRepository
import com.pabloruiz.ratestransactions.model.Transaction

class TransactionsDataImpl(
    private val transactionRemoteImpl: TransactionsRemoteImpl
) : TransactionsRepository {

    private fun RemoteTransactionResponse.toTransaction(): Transaction = Transaction(
        sku = this.sku,
        amount = this.amount,
        currency = this.currency
    )

    override suspend fun getTransactionsInfo(): List<Transaction> {

        return try {
            val response = transactionRemoteImpl.getTransactionsInfo()
            if (response.isSuccessful) {
                response.body()?.map { it.toTransaction() } ?: emptyList()
            } else {
                throw Exception("Error fetching transactions: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw Exception("Error fetching transactions: ${e.localizedMessage}", e)
        }
    }
}