package com.android.currencyconverter.selectedcurrency

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.currencyconverter.R
import com.android.currencyconverter.databinding.FragmentSelectedCurrencyBinding
import com.android.currencyconverter.viewmodels.SharedViewModel
import timber.log.Timber

class SelectedCurrencyFragment : Fragment() {

    private var _binding: FragmentSelectedCurrencyBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedCurrencyBinding.inflate(inflater, container, false)

        sharedViewModel.selectedCurrency.observe(viewLifecycleOwner, { currency ->
            binding.textCurrencyName.text =
                getString(R.string.selected_currency_format, currency.code, currency.name)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}