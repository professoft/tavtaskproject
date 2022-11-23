package com.professoft.tavtask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.databinding.FlightsItemBinding
import com.professoft.tavtask.models.FlightDataModel

class FlightsAdapter(private var FlightsConverterList: List<FlightDataModel>) :
    RecyclerView.Adapter<FlightsAdapter.FlightsViewHolder>() {
    inner class FlightsViewHolder(val itemBinding: FlightsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(FlightsItemModel: FlightDataModel) {
            itemBinding.flightsItem = FlightsItemModel
        }
    }

    override fun getItemCount(): Int {
        return FlightsConverterList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = FlightsItemBinding.inflate(inflater, parent, false)
        return FlightsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FlightsViewHolder, position: Int) {
        holder.bind(FlightsConverterList[position])
        if (FlightsConverterList[position].flight.isDeparture) {
            if (FlightsConverterList[position].departure.gate.isNullOrEmpty()) {
                holder.itemBinding.gate.isVisible = false
            }
        } else {
            if (FlightsConverterList[position].arrival.gate.isNullOrEmpty()) {
                holder.itemBinding.gate.isVisible = false
            }
        }
    }
}