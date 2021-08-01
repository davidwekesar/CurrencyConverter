package com.android.currencyconverter.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.currencyconverter.data.network.*
import com.android.currencyconverter.data.network.datatransferobjects.NetworkCurrency
import com.android.currencyconverter.data.network.datatransferobjects.NetworkExchangeRate
import com.android.currencyconverter.data.network.datatransferobjects.asDatabaseExchangeRateModel
import com.android.currencyconverter.data.network.datatransferobjects.asDatabaseModel
import com.android.currencyconverter.database.CurrenciesDatabase
import com.android.currencyconverter.database.CurrencyAndExchangeRate
import com.android.currencyconverter.database.DatabaseExchangeRate
import com.android.currencyconverter.database.asDomainModel
import com.android.currencyconverter.domain.Currency
import com.android.currencyconverter.utils.ListGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrenciesRepository(private val database: CurrenciesDatabase) {

    val currencies: LiveData<List<Currency>> =
        Transformations.map(database.currencyDao.getAllCurrencies()) {
            it.asDomainModel()
        }

    val currencyAndExchangeRates: LiveData<List<CurrencyAndExchangeRate>> = database.exchangeRateDao.getCurrenciesAndRates()

    private val _timestamp = MutableLiveData<Int>()
    val timestamp: LiveData<Int> get() = _timestamp

    suspend fun refreshCurrenciesAndExchangeRates() {
        withContext(Dispatchers.IO) {
            Timber.d("called refreshCurrenciesAndExchangeRates method")
            val listOfCurrencies: List<NetworkCurrency> = getAllCurrencies()
            val listOfRates: List<NetworkExchangeRate> = getAllExchangeRates()

            database.currencyDao.insertAll(listOfCurrencies.asDatabaseModel())

            var listOfConvertedRates: List<NetworkExchangeRate>
            for (networkCurrency in listOfCurrencies) {
                val networkExchangeRate = listOfRates.find { networkExchangeRate ->
                    networkExchangeRate.currencyPair.contains(networkCurrency.code)
                }
                listOfConvertedRates = listOfRates.map {
                    val newExchangeRate = it.exchangeRate / networkExchangeRate!!.exchangeRate
                    val currencyName = it.currencyPair.split("USD")[1]
                    val currencyPair = "${networkCurrency.code} / $currencyName"
                    val decimalFormat = DecimalFormat("#.######")
                    decimalFormat.roundingMode = RoundingMode.CEILING
                    val roundedExchangeRate: Double =
                        decimalFormat.format(newExchangeRate).toDouble()
                    NetworkExchangeRate(currencyPair, roundedExchangeRate)
                }
                val databaseExchangeRates: List<DatabaseExchangeRate> =
                    listOfConvertedRates.asDatabaseExchangeRateModel(networkCurrency.code)
                Timber.d("New List of Rates: $listOfConvertedRates")
                database.exchangeRateDao.insertAll(databaseExchangeRates)
            }
        }
    }

    suspend fun getSelectedExchangeRates(currencyCode: String): LiveData<List<DatabaseExchangeRate>> {
        return withContext(Dispatchers.IO) {
            database.exchangeRateDao.getSelectedExchangeRates(currencyCode)
        }
    }

    private suspend fun getAllCurrencies(): List<NetworkCurrency> {
        return withContext(Dispatchers.IO) {
            val currencyContainer = CurrencyApi.currencyService.getAllCurrencies()
            val listOfCurrencies =
                ListGenerator().generateNetworkCurrencyList(currencyContainer.currencies)
            listOfCurrencies
        }
    }

    private suspend fun getAllExchangeRates(): List<NetworkExchangeRate> {
        return withContext(Dispatchers.IO) {
            delay(3000)
            val exchangeRatesContainer = CurrencyApi.currencyService
                .getAllExchangeRates()
            exchangeRatesContainer.timestamp?.let {
                _timestamp.postValue(it)
                Timber.d("Timestamp: $it")
            }
            Timber.e("Error code: ${exchangeRatesContainer.error?.code}, Error info: ${exchangeRatesContainer.error?.info}")
            val networkExchangeRates =
                ListGenerator().generateNetworkExchangeRateList(exchangeRatesContainer.quotes!!)
            networkExchangeRates
        }
    }
}