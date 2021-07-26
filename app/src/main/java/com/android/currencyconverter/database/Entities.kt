package com.android.currencyconverter.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.currencyconverter.domain.Currency

@Entity(tableName = "currencies")
data class DatabaseCurrency(
    val code: String,
    val name: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)

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