package com.professoft.tavtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.utils.CurrencyConverterItemsModel

class CurrencyConverterAdapter (
    private var flightList: ArrayList<CurrencyConverterItemsModel>,
) : RecyclerView.Adapter<CurrencyConverterAdapter.CurrencyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.currency_converter_item,
            parent, false
        )
        return CurrencyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyConverterAdapter.CurrencyViewHolder, position: Int) {
        holder.currencyType.text = flightList.get(position).currencyType
        holder.bid.text = flightList.get(position).bid
        holder.ask.text = flightList.get(position).ask
    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyType: TextView = itemView.findViewById(R.id.currencyType)
        val bid: TextView = itemView.findViewById(R.id.bid)
        val ask: TextView = itemView.findViewById(R.id.ask)

    }
}