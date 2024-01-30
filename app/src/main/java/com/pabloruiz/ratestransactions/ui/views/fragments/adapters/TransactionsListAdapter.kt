package com.pabloruiz.ratestransactions.ui.views.fragments.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pabloruiz.ratestransactions.R
import com.pabloruiz.ratestransactions.databinding.RowTransactionItemBinding
import com.pabloruiz.ratestransactions.model.Transaction
import com.pabloruiz.ratestransactions.ui.views.fragments.MenuViewModel

class TransactionsListAdapter(private val viewModel: MenuViewModel) :
    RecyclerView.Adapter<TransactionsListAdapter.TransactionsListViewHolder>() {

    private var transactionList: List<Transaction> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionsListViewHolder {
        val binding =
            RowTransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionsListViewHolder, position: Int) {
        val item = transactionList[position]
        val amount = item.amount.toDoubleOrNull() ?: 0.0
        val convertedAmount = viewModel.convertCurrency(
            amount.toString(),
            item.currency,
            viewModel.selectedCurrency ?: "USD"
        )

        val transactionInfo = holder.itemView.context.getString(
            R.string.transaction_info,
            item.sku,
            amount,
            item.currency,
            convertedAmount,
            viewModel.selectedCurrency
        )

        holder.transaction.text = transactionInfo
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Transaction>) {
        transactionList = list
        notifyDataSetChanged()
    }

    inner class TransactionsListViewHolder(binding: RowTransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val transaction = binding.tvTransactionItem
    }
}