package com.pabloruiz.ratestransactions.ui.views.fragments

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pabloruiz.ratestransactions.databinding.FragmentMenuBinding
import com.pabloruiz.ratestransactions.model.Rate
import com.pabloruiz.ratestransactions.model.Transaction
import com.pabloruiz.ratestransactions.ui.model.ResourceState
import com.pabloruiz.ratestransactions.ui.views.fragments.adapters.TransactionsListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionListAdapter: TransactionsListAdapter

    private val menuViewModel: MenuViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        transactionListAdapter = TransactionsListAdapter(menuViewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecyclerView()
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
                transactionListAdapter.submitList(state.result)
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
        setupCurrencySpinnerListener()
    }

    private fun populateProductSpinner(listOfTransactions: List<Transaction>) {
        val products = listOfTransactions.map { it.sku }.toSet().toList()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            products
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerProduct.adapter = adapter
        setupProductSpinnerListener(listOfTransactions)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun initRecyclerView() {
        binding.rvTransactionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactionsList.adapter = transactionListAdapter
    }

    private fun setupCurrencySpinnerListener() {
        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedCurrency = parent.getItemAtPosition(position) as String
                    menuViewModel.selectedCurrency = selectedCurrency
                    transactionListAdapter.notifyDataSetChanged()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Opcional: manejar la no selección
                }
            }
    }

    private fun setupProductSpinnerListener(listOfTransactions: List<Transaction>) {
        binding.spinnerProduct.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedProduct = parent.getItemAtPosition(position) as String
                    menuViewModel.selectedProduct = selectedProduct
                    val filteredTransactions =
                        menuViewModel.filterTransactionsByProduct(listOfTransactions)
                    transactionListAdapter.submitList(filteredTransactions)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Opcional: manejar la no selección
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
