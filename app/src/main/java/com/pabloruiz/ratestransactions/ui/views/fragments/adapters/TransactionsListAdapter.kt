package com.pabloruiz.ratestransactions.ui.views.fragments.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pabloruiz.ratestransactions.databinding.RowTransactionItemBinding
import com.pabloruiz.ratestransactions.model.Transaction

class TransactionsListAdapter :
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

    override fun onBindViewHolder(
        holder: TransactionsListViewHolder,
        position: Int
    ) {
        val item = transactionList[position]

        holder.transaction.text = item.sku + " " + item.amount + " " + item.currency
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