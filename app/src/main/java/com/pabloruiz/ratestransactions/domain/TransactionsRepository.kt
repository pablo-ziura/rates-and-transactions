package com.pabloruiz.ratestransactions.domain

import com.pabloruiz.ratestransactions.model.Transaction

interface TransactionsRepository {
    suspend fun getTransactionsInfo(): List<Transaction>

}