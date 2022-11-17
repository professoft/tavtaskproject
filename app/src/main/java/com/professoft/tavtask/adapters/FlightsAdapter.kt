package com.professoft.tavtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.utils.FlightItemsModel

class FlightsAdapter (
    private var flightList: ArrayList<FlightItemsModel>,
) : RecyclerView.Adapter<FlightsAdapter.FlightsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FlightsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.flights_item,
            parent, false
        )
        return FlightsViewHolder(itemView)
    }

    fun filterList(filterlist: ArrayList<FlightItemsModel>) {
        flightList = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FlightsAdapter.FlightsViewHolder, position: Int) {
        holder.flightDate.text = flightList.get(position).flightDate
        holder.flightNumber.text = flightList.get(position).flightNumber
        holder.destination.text = flightList.get(position).destination
        holder.checkInInformation.text = flightList.get(position).checkInInformation
        holder.status.text = flightList.get(position).status
        holder.airlineNameWithIcon.setImageResource(flightList.get(position).airlineNameWithIcon)

    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    class FlightsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flightDate: TextView = itemView.findViewById(R.id.flightDate)
        val flightNumber: TextView = itemView.findViewById(R.id.flightNumber)
        val destination: TextView = itemView.findViewById(R.id.destination)
        val checkInInformation: TextView = itemView.findViewById(R.id.checkInInformation)
        val status: TextView = itemView.findViewById(R.id.status)
        val airlineNameWithIcon: ImageView = itemView.findViewById(R.id.airlineNameWithIcon)
    }
}