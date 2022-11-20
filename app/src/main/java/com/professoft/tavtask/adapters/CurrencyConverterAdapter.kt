package com.professoft.tavtask.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.databinding.CurrencyConverterItemBinding
import com.professoft.tavtask.utils.CurrencyConverterItemModel

class CurrencyConverterAdapter (
): RecyclerView.Adapter<CurrencyViewHolder>() {
    var currency = mutableListOf<CurrencyConverterItemModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setCurrencyList(currency: List<CurrencyConverterItemModel>) {

        this.currency = currency.toMutableList()
        notifyDataSetChanged()
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(CurrencyConverterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.apply {
            bind(currency[position])
        }

    }

    override fun getItemCount(): Int {
        return currency.size
    }

    companion object {
    }
}
class CurrencyViewHolder(val binding: CurrencyConverterItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(currency: CurrencyConverterItemModel) {
        binding.currencyConverterItem= currency

    }


}