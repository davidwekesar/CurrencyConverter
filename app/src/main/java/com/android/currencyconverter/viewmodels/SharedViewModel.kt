package com.android.currencyconverter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.currencyconverter.data.network.Currency

class SharedViewModel: ViewModel() {

    private val _selectedCurrency = MutableLiveData<Currency>()
    val selectedCurrency: LiveData<Currency> get() = _selectedCurrency

    fun select(currency: Currency) {
        _selectedCurrency.value = currency
    }
}