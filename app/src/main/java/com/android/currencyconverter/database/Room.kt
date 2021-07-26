package com.android.currencyconverter.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies")
    fun getAllCurrencies(): LiveData<List<DatabaseCurrency>>

    @Query("SELECT * FROM currencies")
    fun getAllCurrenciesTest(): List<DatabaseCurrency>

    @Insert
    fun insertAll(currencies: List<DatabaseCurrency>)
}

@Database(entities = [DatabaseCurrency::class], version = 1)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao
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