package com.android.currencyconverter.selectedcurrency

import android.app.Application
import androidx.lifecycle.*
import com.android.currencyconverter.data.network.NetworkExchangeRate
import com.android.currencyconverter.data.network.asDomainModel
import com.android.currencyconverter.database.getDatabase
import com.android.currencyconverter.domain.ExchangeRate
import com.android.currencyconverter.repositories.CurrenciesRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat

class SelectedCurrencyViewModel(application: Application, private val currencyCode: String) :
    ViewModel() {

    private val repository: CurrenciesRepository = CurrenciesRepository(getDatabase(application))

    private val _exchangeRates = MutableLiveData<List<ExchangeRate>>()
    val exchangeRates: LiveData<List<ExchangeRate>> get() = _exchangeRates

    init {
        getSelectedCurrencyExchangeRate()
    }

    private fun getSelectedCurrencyExchangeRate() {
        viewModelScope.launch {
            try {
                val exchangeRates: List<NetworkExchangeRate> = repository.getAllExchangeRates()
                val selectedCurrency: NetworkExchangeRate? = exchangeRates.find {
                    it.currencyPair.contains(currencyCode)
                }
                val convertedExchangeRates = exchangeRates.map {
                    val newExchangeRate = it.exchangeRate / selectedCurrency!!.exchangeRate
                    val currencyName = it.currencyPair.split("USD")[1]
                    val currencyPair = "$currencyCode$currencyName"
                    val decimalFormat = DecimalFormat("#.#####")
                    decimalFormat.roundingMode = RoundingMode.CEILING
                    val roundedExchangeRate: Double = decimalFormat.format(newExchangeRate).toDouble()
                    NetworkExchangeRate(currencyPair, roundedExchangeRate)
                }
                _exchangeRates.value = convertedExchangeRates.asDomainModel()

                Timber.d("List: ${convertedExchangeRates.asDomainModel()}")
            } catch (e: Exception) {
                Timber.e("Failed to get Selected Exchange Rate: ${e.message}")
            }
        }
    }
}

class SelectedCurrencyViewModelFactory(
    private val application: Application,
    private val currencyCode: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectedCurrencyViewModel::class.java)) {
            return SelectedCurrencyViewModel(application, currencyCode) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}