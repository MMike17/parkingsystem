package com.parkit.parkingsystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

/**
 * Class used to unit test FareCalculatorService
 * 
 * @see com.parkit.parkingsystem.service.FareCalculatorService
 * @author Mike Matthews
 */
@DisplayName("FareCalculatorService")
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

	/**
	 * Tests FareCalculatorService.calculateFare for one hour car parking
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 * @see com.parkit.parkingsystem.constants.Fare#CAR_RATE_PER_HOUR
	 */
    @Test
	@Tag("Calculate fare for 1h of car parking")
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

	/**
	 * Tests FareCalculatorService.calculateFare for one hour bike parking
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 * @see com.parkit.parkingsystem.constants.Fare#BIKE_RATE_PER_HOUR
	 */
    @Test
	@Tag("Calculate fare for 1h of bike parking")
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

	/**
	 * Tests FareCalculatorService.calculateFare for unknown type
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
    @Test
	@Tag("Calculate fare for unknown type")
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

	/**
	 * Tests FareCalculatorService.calculateFare for minus one hour bike parking
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
    @Test
	@Tag("Calculate fare for -1h of car parking")
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

	/**
	 * Tests FareCalculatorService.calculateFare for less than one hour bike parking
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
    @Test
	@Tag("Calculate fare for less than an hour of bike parking")
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Double.parseDouble("0.75"), ticket.getPrice());
    }

	/**
	 * Tests FareCalculatorService.calculateFare for less than one hour car parking
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
    @Test
	@Tag("Calculate fare for less than an hour of car parking")
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Double.parseDouble("1.12") , ticket.getPrice());
    }

	/**
	 * Tests FareCalculatorService.calculateFare for less than half an hour car parking
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
	@Test
	@Tag("Calculate fare for less than half an hour of car parking")
	public void calculateFareCarWithLessThenHalfAnHourParkingTime()
	{
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 1000)); // 25 min parking time should return free
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals(0, ticket.getPrice());
	}

	/**
	 * Tests FareCalculatorService.calculateFare for more than one day car parking
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 * @see com.parkit.parkingsystem.constants.Fare#CAR_RATE_PER_HOUR
	 */
    @Test
	@Tag("Calculate fare for more than one day of car parking")
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

	/**
	 * Tests FareCalculatorService.calculateFare for recurent user
	 * 
	 * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
	 * @see com.parkit.parkingsystem.model.ParkingSpot
	 * @see com.parkit.parkingsystem.model.Ticket
	 * @see com.parkit.parkingsystem.constants.Fare#CAR_RATE_PER_HOUR
	 */
	@Test
	@Tag("Calculate fare for recurent user")
	public void calculateFareRecurentCar ()
	{
		// GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (3 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		// fareCalculatorService.calculateFare(ticket, true);

		// THEN
		// assertEquals(Double.parseDouble("4.27"), ticket.getPrice());
	}
}
