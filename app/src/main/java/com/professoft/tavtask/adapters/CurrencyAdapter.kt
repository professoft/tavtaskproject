package com.professoft.tavtask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.databinding.CurrencyConverterItemBinding
import com.professoft.tavtask.utils.CurrencyConverterItemModel

class CurrencyAdapter(private var currencyConverterList: List<CurrencyConverterItemModel>) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    inner class CurrencyViewHolder(val itemBinding: CurrencyConverterItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(currencyConverterItemModel: CurrencyConverterItemModel) {
            itemBinding.currencyConverterItem = currencyConverterItemModel
        }
    }

    override fun getItemCount(): Int {
        return  currencyConverterList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val itemBinding = CurrencyConverterItemBinding.inflate(inflater,parent,false)
        return CurrencyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(currencyConverterList[position])

    }

}