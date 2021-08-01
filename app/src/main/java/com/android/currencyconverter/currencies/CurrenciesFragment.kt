package com.android.currencyconverter.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.currencyconverter.R
import com.android.currencyconverter.databinding.FragmentCurrenciesBinding
import com.android.currencyconverter.repositories.CurrenciesRepository
import com.android.currencyconverter.viewmodels.SharedViewModel
import timber.log.Timber

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrenciesViewModel by viewModels {
        CurrenciesViewModelFactory(application = requireActivity().application)
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
                adapter = CurrencyAdapter(currencies, CurrencyOnClickListener { currency ->
                    sharedViewModel.select(currency)
                    navigateToSelectedCurrencyFragment()
                })
                addItemDecoration(dividerItemDecoration)
            }
        })

        return binding.root
    }

    private fun navigateToSelectedCurrencyFragment() {
        val action = R.id.action_currenciesFragment_to_selectedCurrencyFragment
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}