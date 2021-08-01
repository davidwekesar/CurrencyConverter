package com.android.currencyconverter.selectedcurrency

import android.app.Application
import androidx.lifecycle.*
import com.android.currencyconverter.database.CurrencyAndExchangeRate
import com.android.currencyconverter.database.DatabaseExchangeRate
import com.android.currencyconverter.database.getDatabase
import com.android.currencyconverter.repositories.CurrenciesRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectedCurrencyViewModel(
    application: Application,
    val currencyCode: String
) : ViewModel() {

    private val repository: CurrenciesRepository = CurrenciesRepository(getDatabase(application))

    val exchangeRates: LiveData<List<CurrencyAndExchangeRate>> = repository.currencyAndExchangeRates

    val timestamp: Int = repository.timestamp
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