package com.android.currencyconverter.selectedcurrency

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.currencyconverter.R
import com.android.currencyconverter.databinding.FragmentSelectedCurrencyBinding
import com.android.currencyconverter.viewmodels.SharedViewModel
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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

        updateRecyclerviewData()

        selectedCurrencyViewModel.timestamp.observe(viewLifecycleOwner, {
            Timber.d("Timestamp: $it")
            val date = Date(it.toLong())
            val formattedDate = SimpleDateFormat("MMM d, yyyy, HH:mm:ss z", Locale.getDefault()).format(date)
            binding.textTimestamp.text = formattedDate
        })

        binding.convertButton.setOnClickListener {
            getAmountInput()
        }

        return binding.root
    }

    private fun getAmountInput() {
        val amountEditText = binding.amountEditText
        val amountTextInputLayout = binding.amountInputLayout
        if (amountEditText.isTextFieldEmpty()) {
            amountTextInputLayout.error = getString(R.string.error_msg_amount)
        } else {
            val inputAmount = amountEditText.text.toString().trim()
            updateRecyclerviewData(inputAmount.toInt())
            amountEditText.error = null
        }
    }

    private fun updateRecyclerviewData(amount: Int = 1) {
        selectedCurrencyViewModel.exchangeRates.observe(viewLifecycleOwner, {
            val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            binding.exchangeRatesRecyclerView.apply {
                val exchangeRateAdapter = ExchangeRateAdapter(
                    it,
                    requireContext()
                )
                exchangeRateAdapter.convertEnteredAmount(amount)
                adapter = exchangeRateAdapter
                addItemDecoration(dividerItemDecoration)
            }
        })
    }

    private fun TextInputEditText.isTextFieldEmpty(): Boolean {
        return TextUtils.isEmpty(text?.trim())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}