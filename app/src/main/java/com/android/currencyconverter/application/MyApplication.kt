package com.android.currencyconverter.application

import android.app.Application
import com.android.currencyconverter.BuildConfig
import timber.log.Timber

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}