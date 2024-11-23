package com.example.contentprovidercontactsdaleshpr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contentprovidercontactsdalesh.R

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val amountTextView: TextView = view.findViewById(R.id.amountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_content_provider_items, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.descriptionTextView.text = transaction.description
        holder.amountTextView.text = "Amount: $${transaction.amount}"
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}