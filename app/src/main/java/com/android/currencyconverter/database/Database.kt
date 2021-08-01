package com.android.currencyconverter.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(entities = [DatabaseCurrency::class, DatabaseExchangeRate::class], version = 1)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao
    abstract val exchangeRateDao: ExchangeRateDao
}

private lateinit var INSTANCE: CurrenciesDatabase

fun getDatabase(context: Context): CurrenciesDatabase {
    synchronized(CurrenciesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                CurrenciesDatabase::class.java,
                "currencies-database"
            ).build()
        }
    }
    return INSTANCE
}