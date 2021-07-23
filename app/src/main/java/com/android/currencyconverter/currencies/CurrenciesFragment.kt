package com.android.currencyconverter.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.currencyconverter.databinding.FragmentCurrenciesBinding
import com.android.currencyconverter.repositories.CurrenciesRepository

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!
    private val repository: CurrenciesRepository by lazy {
        CurrenciesRepository()
    }
    private val viewModel: CurrenciesViewModel by viewModels {
        CurrenciesViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)

        viewModel.currencies.observe(viewLifecycleOwner, { currencies ->
            val dividerItemDecoration = DividerItemDecoration(
                requireContext(), LinearLayoutManager.VERTICAL
            )
            binding.currenciesRecyclerView.apply {
                adapter = CurrencyAdapter(currencies)
                addItemDecoration(dividerItemDecoration)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}