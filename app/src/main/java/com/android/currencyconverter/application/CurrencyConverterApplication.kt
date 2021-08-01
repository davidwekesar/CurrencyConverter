package com.android.currencyconverter.application

import android.app.Application
import android.os.Build
import androidx.work.*
import com.android.currencyconverter.BuildConfig
import com.android.currencyconverter.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CurrencyConverterApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }
            .build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        Timber.d("Periodic Work request for sync is scheduled")
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}