package com.pabloruiz.ratestransactions.di

import com.pabloruiz.ratestransactions.data.networkClient.ApiClient
import com.pabloruiz.ratestransactions.data.rates.RatesDataImpl
import com.pabloruiz.ratestransactions.data.rates.remote.RatesRemoteImpl
import com.pabloruiz.ratestransactions.data.rates.remote.RatesService
import com.pabloruiz.ratestransactions.data.transactions.TransactionsDataImpl
import com.pabloruiz.ratestransactions.data.transactions.remote.TransactionsRemoteImpl
import com.pabloruiz.ratestransactions.data.transactions.remote.TransactionsService
import com.pabloruiz.ratestransactions.domain.RatesRepository
import com.pabloruiz.ratestransactions.domain.TransactionsRepository
import com.pabloruiz.ratestransactions.ui.views.fragments.MenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {
    single { ApiClient }
    single<RatesService> { get<ApiClient>().retrofit.create(RatesService::class.java) }
    single<TransactionsService> { get<ApiClient>().retrofit.create(TransactionsService::class.java) }
}

val applicationModule = module {

    factory {
        RatesRemoteImpl(get())
    }


    factory<RatesRepository> {
        RatesDataImpl(get())
    }

    factory {
        TransactionsRemoteImpl(get())
    }


    factory<TransactionsRepository> {
        TransactionsDataImpl(get())
    }


    viewModel {
        MenuViewModel(get(), get())
    }
}