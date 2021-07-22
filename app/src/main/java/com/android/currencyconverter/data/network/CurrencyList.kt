package com.android.currencyconverter.data.network

data class CurrencyList(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)