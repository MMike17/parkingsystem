package com.parkit.parkingsystem.service;

import java.io.Console;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

		long inMiliseconds = ticket.getInTime().getTime();
		long outMiliseconds = ticket.getOutTime().getTime();

		// miliseconds =(/1000)> seconds =(/60)> minutes =(/60)> hours 
		float duration = (float) (outMiliseconds - inMiliseconds) / 1000 / 60 / 60;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}