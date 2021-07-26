package com.android.currencyconverter.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.android.currencyconverter.data.network.CurrencyApi
import com.android.currencyconverter.data.network.NetworkCurrency
import com.android.currencyconverter.data.network.asDatabaseModel
import com.android.currencyconverter.database.CurrenciesDatabase
import com.android.currencyconverter.database.DatabaseCurrency
import com.android.currencyconverter.database.asDomainModel
import com.android.currencyconverter.domain.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}