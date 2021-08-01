package com.android.currencyconverter.selectedcurrency

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyconverter.R
import com.android.currencyconverter.database.CurrencyAndExchangeRate
import com.android.currencyconverter.database.DatabaseExchangeRate
import com.android.currencyconverter.databinding.ListItemExchangeRateBinding
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class ExchangeRateAdapter(
    private val currencyAndExchangeRates: List<CurrencyAndExchangeRate>,
    private val context: Context
) : RecyclerView.Adapter<ExchangeRateViewHolder>() {

    private var amount: Int = 1

    fun convertEnteredAmount(inputAmount: Int) {
        amount = inputAmount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemExchangeRateBinding.inflate(layoutInflater, parent, false)
        return ExchangeRateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        holder.bind(currencyAndExchangeRates[0].databaseExchangeRate[position], amount, context)
    }

    override fun getItemCount(): Int {
        return currencyAndExchangeRates.size
    }
}

class ExchangeRateViewHolder(
    binding: ListItemExchangeRateBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val textCurrencyPair = binding.textCurrencyPair
    private val textExchangeRate = binding.textExchangeRate
    private val textConvertedAmount = binding.textConvertedAmount

    fun bind(exchangeRate: DatabaseExchangeRate, amount: Int, context: Context) {
        textCurrencyPair.text = exchangeRate.currencyPair
        textExchangeRate.text = exchangeRate.exchangeRate.toString()
        textConvertedAmount.text = getConvertedAmount(amount, exchangeRate.exchangeRate, context)
    }

    private fun getConvertedAmount(amount: Int, exchangeRate: Double, context: Context): String {
        val convertedAmount: Double = amount * exchangeRate
        val roundedAmount: Double =
            BigDecimal(convertedAmount).setScale(2, RoundingMode.CEILING).toDouble()
        return context.getString(R.string.converted_amount_format, roundedAmount)
    }
}