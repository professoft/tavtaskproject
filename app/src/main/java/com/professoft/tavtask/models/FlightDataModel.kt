package com.professoft.tavtask.models

class FlightDataModel {
    lateinit var flight_status: String
    lateinit var flight_date: String
    lateinit var departure: DepartureModel
    lateinit var arrival: ArrivalModel
    lateinit var airline: AirlineModel
    lateinit var flight: FlightModel

    constructor(flight_date: String,
                flight_status: String,
                departure: DepartureModel,
                arrival: ArrivalModel,
                airline: AirlineModel,
                flights: FlightModel
    ) {
        this.departure = departure
        this.arrival = arrival
        this.flight_status = flight_status
        this.airline=airline
        this.flight_date = flight_date
        this.flight = flight
    }

    constructor()
}
