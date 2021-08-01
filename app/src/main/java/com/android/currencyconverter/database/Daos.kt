package com.android.currencyconverter.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies")
    fun getAllCurrencies(): LiveData<List<DatabaseCurrency>>

    @Query("SELECT * FROM currencies")
    fun getAllCurrenciesTest(): List<DatabaseCurrency>

    @Insert
    fun insertAll(currencies: List<DatabaseCurrency>)
}

@Dao
interface ExchangeRateDao {

    @Transaction
    @Query("SELECT * FROM currencies")
    fun getCurrenciesAndRates(): LiveData<List<CurrencyAndExchangeRate>>

    @Query("SELECT * FROM exchange_rates WHERE currencyCode LIKE :currencyCode")
    fun getSelectedExchangeRates(currencyCode: String): LiveData<List<DatabaseExchangeRate>>

    @Insert
    fun insertAll(exchangeRates: List<DatabaseExchangeRate>)
}