package com.android.currencyconverter.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.android.currencyconverter.data.network.*
import com.android.currencyconverter.database.CurrenciesDatabase
import com.android.currencyconverter.database.asDomainModel
import com.android.currencyconverter.domain.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CurrenciesRepository(private val database: CurrenciesDatabase) {

    val currencies: LiveData<List<Currency>> =
        Transformations.map(database.currencyDao.getAllCurrencies()) {
            it.asDomainModel()
        }

    suspend fun refreshCurrencies() {
        withContext(Dispatchers.IO) {
            val currencyContainer = CurrencyApi.currencyService.getAllCurrencies()
            val listOfCurrencies = mutableListOf<NetworkCurrency>()
            for (item in currencyContainer.currencies) {
                val currency = NetworkCurrency(item.key, item.value)
                listOfCurrencies.add(currency)
            }
            database.currencyDao.insertAll(listOfCurrencies.asDatabaseModel())
        }
    }

    suspend fun getAllExchangeRates(): List<NetworkExchangeRate> {
        return withContext(Dispatchers.IO) {
            val exchangeRatesContainer = CurrencyApi.currencyService
                .getAllExchangeRates()
            val networkExchangeRates = mutableListOf<NetworkExchangeRate>()
            for (item in exchangeRatesContainer.quotes!!) {
                val networkExchangeRate = NetworkExchangeRate(
                    currencyPair = item.key, exchangeRate = item.value
                )
                networkExchangeRates.add(networkExchangeRate)
            }
            networkExchangeRates
        }
    }
}