package com.android.currencyconverter.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.currencyconverter.data.network.NetworkCurrency
import com.android.currencyconverter.data.network.asDatabaseModel
import com.android.currencyconverter.domain.Currency
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CurrenciesDatabaseTest {
    private lateinit var currencyDao: CurrencyDao
    private lateinit var database: CurrenciesDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CurrenciesDatabase::class.java).build()
        currencyDao = database.currencyDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadCurrencies() {
        val currencyList = mutableListOf(
            NetworkCurrency("AED", "United Arab Emirates Dirham"),
            NetworkCurrency("AFN", "Afghan Afghani"),
            NetworkCurrency("ALL", "Armenian Dram")
        )
        val domainList: List<Currency> = transformToDomainList(currencyList)
        val databaseList: List<DatabaseCurrency> = currencyList.asDatabaseModel()
        currencyDao.insertAll(databaseList)
        val domainListFromDb: List<Currency> = currencyDao.getAllCurrenciesTest().asDomainModel()
        assertThat(domainListFromDb, equalTo(domainList))
    }

    private fun transformToDomainList(list: List<NetworkCurrency>): List<Currency> {
        return list.map { Currency(code = it.code, name = it.name) }
    }
}