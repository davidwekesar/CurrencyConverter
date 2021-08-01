package com.android.currencyconverter.data.network.datatransferobjects

import com.android.currencyconverter.database.DatabaseExchangeRate
import com.android.currencyconverter.domain.ExchangeRate

data class NetworkExchangeRatesContainer(
    val success: Boolean,
    val timestamp: Int? = null,
    val quotes: Map<String, Double>? = null,
    val error: NetworkExchangeRatesError? = null
)

data class NetworkExchangeRatesError(val code: Int, val info: String)

data class NetworkExchangeRate(val currencyPair: String, val exchangeRate: Double)

fun List<NetworkExchangeRate>.asDatabaseExchangeRateModel(currencyCode: String): List<DatabaseExchangeRate> {
    return map { networkExchangeRate ->
        DatabaseExchangeRate(
            currencyPair = networkExchangeRate.currencyPair,
            exchangeRate = networkExchangeRate.exchangeRate,
            currencyCode = currencyCode
        )
    }
}

fun List<NetworkExchangeRate>.asDomainModel(): List<ExchangeRate> {
    return map { networkExchangeRate ->
        with(networkExchangeRate) {
            ExchangeRate(currencyPair = currencyPair, exchangeRate = exchangeRate)
        }
    }
}