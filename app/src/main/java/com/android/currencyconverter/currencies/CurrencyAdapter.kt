package com.android.currencyconverter.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyconverter.data.network.Currency
import com.android.currencyconverter.databinding.ListItemCurrencyBinding

class CurrencyAdapter(
    private val currencies: List<Currency>,
    private val onClickListener: CurrencyOnClickListener
) : RecyclerView.Adapter<CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemCurrencyBinding.inflate(layoutInflater, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(currencies[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }
}

class CurrencyViewHolder(binding: ListItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {

    private val textCurrencyCode = binding.textCurrencyCode
    private val textCurrencyName = binding.textCurrencyName

    fun bind(currency: Currency, onClickListener: CurrencyOnClickListener) {
        textCurrencyCode.text = currency.code
        textCurrencyName.text = currency.name
        itemView.setOnClickListener {
            onClickListener.onClick(currency)
        }
    }
}

class CurrencyOnClickListener(val clickListener: (currency: Currency) -> Unit) {
    fun onClick(currency: Currency) = clickListener(currency)
}