package com.parkit.parkingsystem.model;

import java.util.Calendar;
import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime()
	{
		if(inTime != null)
        	return (Date) inTime.clone();
		else
			return null;
    }

    public void setInTime(Date inTime)
	{
		if(this.inTime == null)
			this.inTime = new Date();

		if(inTime != null)
	        this.inTime.setTime(inTime.getTime());
    }

    public Date getOutTime()
	{
		if(outTime != null)
        	return (Date) outTime.clone();
		else
			return null;
    }

    public void setOutTime(Date outTime)
	{
		if(this.outTime == null)
			this.outTime = new Date();

		if(outTime != null)
	        this.outTime.setTime(outTime.getTime());
    }
}
