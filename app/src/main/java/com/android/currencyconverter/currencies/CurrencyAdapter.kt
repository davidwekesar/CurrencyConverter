package com.android.currencyconverter.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyconverter.databinding.ListItemCurrencyBinding

class CurrencyAdapter(
    private val currencies: List<String>
) : RecyclerView.Adapter<CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemCurrencyBinding.inflate(layoutInflater, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount(): Int {
        return currencies.size
    }
}

class CurrencyViewHolder(binding: ListItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {

    private val textCurrency = binding.textCurrency

    fun bind(currency: String) {
        textCurrency.text = currency
    }
}