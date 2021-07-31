package com.android.currencyconverter.selectedcurrency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyconverter.databinding.ListItemExchangeRateBinding
import com.android.currencyconverter.domain.ExchangeRate

class ExchangeRateAdapter(
    private val exchangeRates: List<ExchangeRate>
) : RecyclerView.Adapter<ExchangeRateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemExchangeRateBinding.inflate(layoutInflater, parent, false)
        return ExchangeRateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        holder.bind(exchangeRates[position])
    }

    override fun getItemCount(): Int {
        return exchangeRates.size
    }
}

class ExchangeRateViewHolder(
    binding: ListItemExchangeRateBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val textCurrencyPair = binding.textCurrencyPair
    private val textExchangeRate = binding.textExchangeRate

    fun bind(exchangeRate: ExchangeRate) {
        textCurrencyPair.text = exchangeRate.currencyPair
        textExchangeRate.text = exchangeRate.exchangeRate.toString()
    }
}