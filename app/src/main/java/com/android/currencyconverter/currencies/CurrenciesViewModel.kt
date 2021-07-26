package com.android.currencyconverter.currencies

import android.app.Application
import androidx.lifecycle.*
import com.android.currencyconverter.database.getDatabase
import com.android.currencyconverter.domain.Currency
import com.android.currencyconverter.repositories.CurrenciesRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CurrenciesViewModel(application: Application) : ViewModel() {

    private val currenciesRepository = CurrenciesRepository(getDatabase(application))

    val currencies: LiveData<List<Currency>> = currenciesRepository.currencies

    init {
        getDataFromRepository()
    }

    private fun getDataFromRepository() {
        viewModelScope.launch {
            try {
                currenciesRepository.refreshCurrencies()
            } catch (e: Exception) {
                Timber.e("Failure: ${e.message}")
            }
        }
    }
}

class CurrenciesViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrenciesViewModel::class.java)) {
            return CurrenciesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}