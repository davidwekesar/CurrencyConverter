package com.android.currencyconverter.currencies

import androidx.lifecycle.*
import com.android.currencyconverter.data.network.CurrencyList
import com.android.currencyconverter.repositories.CurrenciesRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CurrenciesViewModel(private val repository: CurrenciesRepository) : ViewModel() {

    private val _currencyList = MutableLiveData<CurrencyList>()
    val currencyList: LiveData<CurrencyList> get() = _currencyList

    private val _currencies = MutableLiveData<List<String>>()
    val currencies: LiveData<List<String>> get() = _currencies

    init {
        getAllCurrencies()
    }

    private fun getAllCurrencies() {
        viewModelScope.launch {
            try {
                val response = repository.getAllCurrencies()
                _currencyList.value = response
                val mutableList = mutableListOf<String>()
                for (currency in response.currencies) {
                    mutableList.add(currency.value)
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