package com.android.currencyconverter.currencies

import androidx.lifecycle.*
import com.android.currencyconverter.data.network.Currency
import com.android.currencyconverter.repositories.CurrenciesRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CurrenciesViewModel(private val repository: CurrenciesRepository) : ViewModel() {

    private val _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>> get() = _currencies

    init {
        getAllCurrencies()
    }

    private fun getAllCurrencies() {
        viewModelScope.launch {
            try {
                val response = repository.getAllCurrencies()
                val mutableList = mutableListOf<Currency>()
                for (currencyItem in response.currencies) {
                    val currency = Currency(currencyItem.key, currencyItem.value)
                    mutableList.add(currency)
                }
                _currencies.value = mutableList
                Timber.d("List: $mutableList")
            } catch (e: Exception) {
                Timber.e("Failure: ${e.message}")
            }
        }
    }
}

class CurrenciesViewModelFactory(
    private val repository: CurrenciesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrenciesViewModel::class.java)) {
            return CurrenciesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}