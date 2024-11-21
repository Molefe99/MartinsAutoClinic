package com.example.martinsautoclinic


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesAdapter(private val quotes: List<Quote>) :
    RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customerName: TextView = view.findViewById(R.id.customerName)
        val requestedService: TextView = view.findViewById(R.id.requestedService)
        val estimatedCost: TextView = view.findViewById(R.id.estimatedCost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quote_item, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]
        holder.customerName.text = "Customer: ${quote.customerName}"
        holder.requestedService.text = "Requested Service: ${quote.requestedService}"
        holder.estimatedCost.text = "Estimated Cost: ${quote.estimatedCost}"
    }

    override fun getItemCount() = quotes.size
}
