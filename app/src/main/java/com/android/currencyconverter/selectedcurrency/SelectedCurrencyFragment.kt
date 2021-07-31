package com.android.currencyconverter.selectedcurrency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.android.currencyconverter.R
import com.android.currencyconverter.databinding.FragmentSelectedCurrencyBinding
import com.android.currencyconverter.viewmodels.SharedViewModel

class SelectedCurrencyFragment : Fragment() {

    private var _binding: FragmentSelectedCurrencyBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val selectedCurrencyViewModel: SelectedCurrencyViewModel by viewModels {
        val currencyCode = sharedViewModel.selectedNetworkCurrency.value?.let { currency ->
            val code = currency.code
            code
        }
        SelectedCurrencyViewModelFactory(requireActivity().application, currencyCode!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedCurrencyBinding.inflate(inflater, container, false)

        sharedViewModel.selectedNetworkCurrency.observe(viewLifecycleOwner, { currency ->
            binding.textCurrencyName.text =
                getString(R.string.selected_currency_format, currency.code, currency.name)
        })

        selectedCurrencyViewModel.exchangeRates.observe(viewLifecycleOwner, {
            binding.exchangeRatesRecyclerView.adapter = ExchangeRateAdapter(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}