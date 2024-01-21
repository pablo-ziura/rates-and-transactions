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

    fun getRatesInfo() {
        _getRatesInfoLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rates = ratesRepository.getRatesInfo()
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