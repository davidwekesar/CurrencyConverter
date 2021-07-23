package com.android.currencyconverter.data.network

data class CurrencyResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)

data class Currency(val code: String, val name: String)