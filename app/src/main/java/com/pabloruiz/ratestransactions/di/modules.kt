package com.pabloruiz.ratestransactions.di

import com.pabloruiz.ratestransactions.data.networkClient.ApiClient
import com.pabloruiz.ratestransactions.data.rates.RatesDataImpl
import com.pabloruiz.ratestransactions.data.rates.remoteService.RatesRemoteImpl
import com.pabloruiz.ratestransactions.ui.views.fragments.MenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ratesModule = module {

    single {
        ApiClient()
    }

    factory {
        RatesRemoteImpl(get())
    }

    factory {
        RatesDataImpl(get())
    }

    viewModel {
        MenuViewModel(get())
    }

}


