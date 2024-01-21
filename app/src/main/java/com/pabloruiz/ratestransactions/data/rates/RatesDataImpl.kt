package com.pabloruiz.ratestransactions.data.rates

import com.pabloruiz.ratestransactions.data.rates.remote.RatesRemoteImpl
import com.pabloruiz.ratestransactions.data.rates.remote.model.RemoteRateResponse
import com.pabloruiz.ratestransactions.domain.RatesRepository
import com.pabloruiz.ratestransactions.model.Rate

class RatesDataImpl(
    private val ratesRemoteImpl: RatesRemoteImpl,
) : RatesRepository {

    private fun RemoteRateResponse.toRate(): Rate = Rate(
        from = this.from,
        to = this.to,
        rate = this.rate
    )

    override suspend fun getRatesInfo(): List<Rate> {

        return try {
            val response = ratesRemoteImpl.getRatesInfo()
            if (response.isSuccessful) {
                response.body()?.map { it.toRate() } ?: emptyList()
            } else {
                throw Exception("Error fetching rates: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw Exception("Error fetching rates: ${e.localizedMessage}", e)
        }
    }

}