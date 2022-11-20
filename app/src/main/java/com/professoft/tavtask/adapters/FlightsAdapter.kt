package com.professoft.tavtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.utils.FlightsResponse

class FlightsAdapter (
    private var flightList: List<FlightsResponse>,
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

    fun filterList(filterlist: List<FlightsResponse>) {
        flightList = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FlightsViewHolder, position: Int) {
        holder.flightDate.text = flightList.get(position).data.flight_date
        holder.flightNumber.text = flightList.get(position).data.flight.number
        holder.destination.text = flightList.get(position).data.arrival.airport
        holder.gate.text = flightList.get(position).data.departure.gate
        holder.status.text = flightList.get(position).data.flight_status
        holder.airlineNameWithIcon.setImageDrawable(flightList.get(position).data.airline.airlineNameWithIcon)

    }



    override fun getItemCount(): Int {
        return flightList.size
    }

    class FlightsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flightDate: TextView = itemView.findViewById(R.id.flightDate)
        val flightNumber: TextView = itemView.findViewById(R.id.flightNumber)
        val destination: TextView = itemView.findViewById(R.id.destination)
        val gate: TextView = itemView.findViewById(R.id.gate)
        val status: TextView = itemView.findViewById(R.id.status)
        val airlineNameWithIcon: ImageView = itemView.findViewById(R.id.airlineNameWithIcon)
    }
}