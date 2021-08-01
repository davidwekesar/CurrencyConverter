package com.android.currencyconverter.utils

import com.android.currencyconverter.data.network.datatransferobjects.NetworkCurrency
import com.android.currencyconverter.data.network.datatransferobjects.NetworkExchangeRate

class ListGenerator {

    fun generateNetworkCurrencyList(map: Map<String, String>): List<NetworkCurrency> {
        val listOfCurrencies = mutableListOf<NetworkCurrency>()
        for (item in map) {
            val currency = NetworkCurrency(item.key, item.value)
            listOfCurrencies.add(currency)
        }
        return listOfCurrencies
    }

    fun generateNetworkExchangeRateList(map: Map<String, Double>): List<NetworkExchangeRate> {
        val listOfCurrencies = mutableListOf<NetworkExchangeRate>()
        for (item in map) {
            val currency = NetworkExchangeRate(item.key, item.value)
            listOfCurrencies.add(currency)
        }
        return listOfCurrencies
    }
}