package com.pabloruiz.ratestransactions.ui.views.fragments

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pabloruiz.ratestransactions.databinding.FragmentMenuBinding
import com.pabloruiz.ratestransactions.model.Rate
import com.pabloruiz.ratestransactions.model.Transaction
import com.pabloruiz.ratestransactions.ui.model.ResourceState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val menuViewModel: MenuViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        menuViewModel.getRatesInfo()
        menuViewModel.getTransactionsInfo()

        menuViewModel.getRatesInfoLiveData.observe(viewLifecycleOwner) { state ->
            handleGetRatesInfoState(state)
        }

        menuViewModel.getTransactionsInfoLiveData.observe(viewLifecycleOwner) { state ->
            handleGetTransactionsInfoState(state)
        }
    }

    private fun handleGetRatesInfoState(state: GetRatesInfoState) {
        when (state) {
            is ResourceState.Loading -> showLoading()
            is ResourceState.Success -> {
                hideLoading()
                populateCurrencySpinner(state.result)
            }

            is ResourceState.Error -> {
                hideLoading()
                showError(state.error)
            }

            is ResourceState.None -> hideLoading()
        }
    }

    private fun handleGetTransactionsInfoState(state: GetTransactionsInfoState) {
        when (state) {
            is ResourceState.Loading -> showLoading()
            is ResourceState.Success -> {
                hideLoading()
                populateProductSpinner(state.result)
            }

            is ResourceState.Error -> {
                hideLoading()
                showError(state.error)
            }

            is ResourceState.None -> hideLoading()
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun populateCurrencySpinner(listOfRates: List<Rate>) {
        val rates = listOfRates.map { it.from }.toSet().toList()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            rates
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = adapter
    }

    private fun populateProductSpinner(listOfTransactions: List<Transaction>) {
        val rates = listOfTransactions.map { it.sku }.toSet().toList()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            rates
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerProduct.adapter = adapter
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}