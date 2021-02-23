package com.parkit.parkingsystem.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
		DecimalFormat decimalFormat = new DecimalFormat("#.##");

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
				float fare = Float.parseFloat(decimalFormat.format(duration * Fare.CAR_RATE_PER_HOUR));
                ticket.setPrice(fare);
                break;
            }
            case BIKE: {
				float fare = Float.parseFloat(decimalFormat.format(duration * Fare.BIKE_RATE_PER_HOUR));
                ticket.setPrice(fare);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}