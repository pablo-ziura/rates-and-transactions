package com.pabloruiz.ratestransactions

import android.app.Application
import com.pabloruiz.ratestransactions.di.ratesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.logging.Logger

class RatesAndTransactionsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger()
            androidContext(this@RatesAndTransactionsApp)
            modules(ratesModule)
        }
    }

}