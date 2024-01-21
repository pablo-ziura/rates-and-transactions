package com.pabloruiz.ratestransactions

import android.app.Application
import com.pabloruiz.ratestransactions.di.applicationModule
import com.pabloruiz.ratestransactions.di.baseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RatesAndTransactionsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger()
            androidContext(this@RatesAndTransactionsApp)
            modules(listOf(baseModule, applicationModule)).allowOverride(true)
        }
    }

}