package com.android.currencyconverter.data.network

import com.android.currencyconverter.database.DatabaseCurrency
import com.android.currencyconverter.domain.ExchangeRate

data class NetworkCurrencyContainer(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)

data class NetworkCurrency(val code: String, val name: String)

data class NetworkExchangeRatesContainer(
    val success: Boolean,
    val timestamp: Int? = null,
    val quotes: Map<String, Double>? = null,
    val error: NetworkExchangeRatesError? = null
)

data class NetworkExchangeRatesError(val code: Int, val info: String)

data class NetworkExchangeRate(val currencyPair: String, val exchangeRate: Double)

fun List<NetworkCurrency>.asDatabaseModel(): List<DatabaseCurrency> {
    return map { networkCurrency ->
        with(networkCurrency) {
            DatabaseCurrency(code = code, name = name)
        }
    }
}

fun List<NetworkExchangeRate>.asDomainModel(): List<ExchangeRate> {
    return map { networkExchangeRate ->
        with(networkExchangeRate) {
            ExchangeRate(currencyPair = currencyPair, exchangeRate = exchangeRate)
        }
    }
}