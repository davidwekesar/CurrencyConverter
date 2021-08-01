package com.android.currencyconverter.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.android.currencyconverter.domain.Currency
import com.android.currencyconverter.domain.ExchangeRate

@Entity(tableName = "currencies")
data class DatabaseCurrency(
    @PrimaryKey
    val code: String,
    val name: String
)

@Entity(tableName = "exchange_rates")
data class DatabaseExchangeRate(
    val currencyPair: String,
    val exchangeRate: Double,
    val currencyCode: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)

data class CurrencyAndExchangeRate(
    @Embedded
    val databaseCurrency: DatabaseCurrency,
    @Relation(
        parentColumn = "code",
        entityColumn = "currencyCode"
    )
    val databaseExchangeRate: List<DatabaseExchangeRate>
)

fun List<DatabaseExchangeRate>.asDomainExchangeRateModel(): List<ExchangeRate> {
    return map { databaseExchangeRate ->
        ExchangeRate(databaseExchangeRate.currencyPair, databaseExchangeRate.exchangeRate)
    }
}

/**
 * Map DatabaseCurrencies to domain Currency
 */
fun List<DatabaseCurrency>.asDomainModel(): List<Currency> {
    return map { databaseCurrency ->
        with(databaseCurrency) {
            Currency(code = code, name = name)
        }
    }
}