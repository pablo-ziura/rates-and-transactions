package com.pabloruiz.ratestransactions.ui.views.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloruiz.ratestransactions.domain.RatesRepository
import com.pabloruiz.ratestransactions.domain.TransactionsRepository
import com.pabloruiz.ratestransactions.model.Rate
import com.pabloruiz.ratestransactions.model.Transaction
import com.pabloruiz.ratestransactions.ui.model.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias GetRatesInfoState = ResourceState<List<Rate>>
typealias GetTransactionsInfoState = ResourceState<List<Transaction>>

class MenuViewModel(
    private val ratesRepository: RatesRepository,
    private val transactionsRepository: TransactionsRepository
) : ViewModel() {

    private val _getRatesInfoLiveData = MutableLiveData<GetRatesInfoState>()
    val getRatesInfoLiveData: LiveData<GetRatesInfoState> get() = _getRatesInfoLiveData

    private val _getTransactionsInfoLiveData = MutableLiveData<GetTransactionsInfoState>()
    val getTransactionsInfoLiveData: LiveData<GetTransactionsInfoState> get() = _getTransactionsInfoLiveData

    private var exchangeRatesMatrix: Array<DoubleArray>? = null
    private var matrixIndices: Map<String, Int>? = null

    fun getRatesInfo() {
        _getRatesInfoLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rates = ratesRepository.getRatesInfo()
                val (matrix, indices) = calculateExchangeRates(rates)
                exchangeRatesMatrix = matrix
                matrixIndices = indices
                withContext(Dispatchers.Main) {
                    _getRatesInfoLiveData.value = ResourceState.Success(rates)
                }
            } catch (e: Exception) {
                _getRatesInfoLiveData.postValue(
                    ResourceState.Error(
                        e.localizedMessage ?: "Unknown error"
                    )
                )
            }
        }
    }

    // Using Floyd-Warshall Algorithm
    private fun calculateExchangeRates(rates: List<Rate>): Pair<Array<DoubleArray>, Map<String, Int>> {
        val currencies = rates.flatMap { listOf(it.from, it.to) }.toSet()
        val indices = currencies.withIndex().associate { it.value to it.index }
        val numCurrencies = currencies.size
        val matrix = Array(numCurrencies) { DoubleArray(numCurrencies) { Double.MAX_VALUE } }

        for (i in matrix.indices) {
            matrix[i][i] = 1.0
        }

        for (rate in rates) {
            val rateValue = rate.rate.toDoubleOrNull() ?: continue
            val fromIndex = indices[rate.from]!!
            val toIndex = indices[rate.to]!!
            matrix[fromIndex][toIndex] = rateValue
            matrix[toIndex][fromIndex] = 1 / rateValue
        }

        for (k in matrix.indices) {
            for (i in matrix.indices) {
                for (j in matrix.indices) {
                    if (matrix[i][j] > matrix[i][k] * matrix[k][j]) {
                        matrix[i][j] = matrix[i][k] * matrix[k][j]
                    }
                }
            }
        }
        return Pair(matrix, indices)
    }

    fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val fromIndex = matrixIndices?.get(fromCurrency)
        val toIndex = matrixIndices?.get(toCurrency)
        if (fromIndex != null && toIndex != null) {
            val rate = exchangeRatesMatrix?.get(fromIndex)?.get(toIndex)
            if (rate != null && rate != Double.MAX_VALUE) {
                return amount * rate
            }
        }
        return 0.0
    }

    fun getTransactionsInfo() {
        _getTransactionsInfoLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val transactions = transactionsRepository.getTransactionsInfo()
                withContext(Dispatchers.Main) {
                    _getTransactionsInfoLiveData.value = ResourceState.Success(transactions)
                }
            } catch (e: Exception) {
                _getTransactionsInfoLiveData.postValue(
                    ResourceState.Error(
                        e.localizedMessage ?: "Unknown error"
                    )
                )
            }
        }
    }
}