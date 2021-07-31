package com.android.currencyconverter.data.network

import com.android.currencyconverter.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "http://apilayer.net/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CurrencyApiService {

    @GET("api/list?access_key=${BuildConfig.CL_KEY}")
    suspend fun getAllCurrencies(): NetworkCurrencyContainer

    @GET("api/live?access_key=${BuildConfig.CL_KEY}")
    suspend fun getAllExchangeRates(): NetworkExchangeRatesContainer
}

object CurrencyApi {
    val currencyService: CurrencyApiService by lazy {
        retrofit.create(CurrencyApiService::class.java)
    }
}