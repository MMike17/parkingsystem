package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar()
	{
		// GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		// WHEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
        parkingService.processIncomingVehicle();

		// THEN
		Ticket ticket = getTicketWithRegistrationNumber();

		int parkingSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		assertNotEquals(ticket.getParkingSpot().getId(), parkingSpot);
    }

    @Test
	public void testParkingLotExit()
	{
		// GIVEN
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle(); // set car parking in database

		// WHEN
		parkingService.processExitingVehicle();

		// THEN
		Ticket ticket = getTicketWithRegistrationNumber();

		assertNotEquals(ticket.getOutTime(), null);

		if(hasMinimumFare(ticket))
			assertNotEquals(ticket.getPrice(), 0);
	}

	Ticket getTicketWithRegistrationNumber()
	{
		Ticket ticket = null;
		String regNumber = new String();

		try
		{
			regNumber = inputReaderUtil.readVehicleRegistrationNumber();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

		ticket = ticketDAO.getTicket(regNumber);

		if(ticket == null)
			fail("Couldn't retrieve ticket from DB");

		return ticket;
	}

	boolean hasMinimumFare(Ticket ticket)
	{
		double parkingRate = 0;

		switch (ticket.getParkingSpot().getParkingType())
		{
			case CAR:
				parkingRate = Fare.CAR_RATE_PER_HOUR;
				break;
			case BIKE:
				parkingRate = Fare.BIKE_RATE_PER_HOUR;
				break;
		}

		// hours =(*60)> minutes =(*60)> seconds =(*1000)> miliseconds
		double minTime = (1 * 60 * 60 * 1000) / (parkingRate / 0.01);
		double parkingDuration = ticket.getOutTime().getTime() - ticket.getInTime().getTime();

		return parkingDuration > minTime;
	}
}
