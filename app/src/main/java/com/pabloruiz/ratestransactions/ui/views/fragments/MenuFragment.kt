package com.pabloruiz.ratestransactions.ui.views.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pabloruiz.ratestransactions.databinding.FragmentMenuBinding
import com.pabloruiz.ratestransactions.model.Rate
import com.pabloruiz.ratestransactions.ui.model.ResourceState

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val menuViewModel: MenuViewModel by activityViewModels()

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
        menuViewModel.getRatesInfoLiveData.observe(viewLifecycleOwner) { state ->
            handleGetRatesInfoState(state)
        }
    }

    private fun handleGetRatesInfoState(state: GetRatesInfoState) {
        when (state) {
            is ResourceState.Loading -> showLoading()
            is ResourceState.Success -> {
                hideLoading()
                populateSpinner(state.result)
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

    private fun populateSpinner(rates: List<Rate>) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, rates.map { it.from })
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = adapter
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}