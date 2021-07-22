package com.android.currencyconverter.repositories

import com.android.currencyconverter.data.network.CurrencyApi
import com.android.currencyconverter.data.network.CurrencyApiService

class CurrenciesRepository {
    private val currencyService: CurrencyApiService = CurrencyApi.currencyService

    suspend fun getAllCurrencies() = currencyService.getAllCurrencies()
}