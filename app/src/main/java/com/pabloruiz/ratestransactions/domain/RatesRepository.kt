package com.pabloruiz.ratestransactions.domain

import com.pabloruiz.ratestransactions.model.Rate

interface RatesRepository {
    suspend fun getRatesInfo(): List<Rate>
}