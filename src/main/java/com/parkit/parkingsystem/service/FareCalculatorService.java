package com.parkit.parkingsystem.service;

import java.math.BigDecimal;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	double freeMilThreshold = 30 * 60 * 1000;

    public void calculateFare(Ticket ticket, boolean isRecurent)
	{
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

		long inMiliseconds = ticket.getInTime().getTime();
		long outMiliseconds = ticket.getOutTime().getTime();

		// miliseconds =(/1000)> seconds =(/60)> minutes =(/60)> hours 
		float duration = (float) (outMiliseconds - inMiliseconds) / 1000 / 60 / 60;

		// less than 30 minutes parking time is free
		if((outMiliseconds - inMiliseconds) <= freeMilThreshold)
		{
			ticket.setPrice(0);
			return;
		}

		double selectedFare = 0;

		switch (ticket.getParkingSpot().getParkingType())
		{
            case CAR:
				selectedFare = Fare.CAR_RATE_PER_HOUR;
                break;

            case BIKE:
				selectedFare = Fare.BIKE_RATE_PER_HOUR;
                break;

            default:
				throw new IllegalArgumentException("Unkown Parking Type");
        }

		ticket.setPrice(computeFare(duration, selectedFare, isRecurent));
    }

	double computeFare (float duration, double rate, boolean isRecurent)
	{
		double fare = duration * rate;

		// recurent user discount
		if(isRecurent)
			fare -= (fare / 100 * 5);

		int fareConversion = (int) (fare * 100);
		BigDecimal computedFare = new BigDecimal(Integer.toString(fareConversion));
		computedFare = computedFare.divide(new BigDecimal("100.00"));

		return computedFare.doubleValue();
	}
}