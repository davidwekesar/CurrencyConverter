package com.android.currencyconverter.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.currencyconverter.database.getDatabase
import com.android.currencyconverter.repositories.CurrenciesRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.android.currencyconverter.work.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = CurrenciesRepository(database)

        try {
            repository.refreshCurrenciesAndExchangeRates()
            Timber.d("Worker request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
}