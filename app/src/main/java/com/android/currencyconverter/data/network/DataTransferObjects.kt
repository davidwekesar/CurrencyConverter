package com.android.currencyconverter.data.network

import com.android.currencyconverter.database.DatabaseCurrency

data class NetworkCurrencyContainer(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)

data class NetworkCurrency(val code: String, val name: String)

fun List<NetworkCurrency>.asDatabaseModel(): List<DatabaseCurrency> {
    return map { networkCurrency ->
        with(networkCurrency) {
            DatabaseCurrency(code = code, name = name)
        }
    }
}