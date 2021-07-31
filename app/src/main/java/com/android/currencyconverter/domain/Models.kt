package com.android.currencyconverter.domain

data class Currency(val code: String, val name: String)

data class ExchangeRate(val currencyPair: String, val exchangeRate: Double)