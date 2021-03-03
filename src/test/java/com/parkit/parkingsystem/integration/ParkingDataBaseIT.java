package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Date;

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

	@AfterEach
	private void closeTest()
	{
		// closes ticket in database
		Ticket ticket = getTicketWithRegistrationNumber();
		ticket.setOutTime(new Date());
		ticketDAO.updateTicket(ticket);
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
		
		// inserts test ticket in database
		Ticket testTicket = new Ticket();
		testTicket.setParkingSpot(parkingService.getNextParkingNumberIfAvailable());

		try
		{
			testTicket.setVehicleRegNumber(inputReaderUtil.readVehicleRegistrationNumber());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		Date insertedInTime = new Date();
		insertedInTime.setTime(System.currentTimeMillis() - (1000 * 60 * 60));
		testTicket.setInTime(insertedInTime);
		
		ticketDAO.saveTicket(testTicket);

		// WHEN
		parkingService.processExitingVehicle();

		// THEN
		Ticket ticket = getTicketWithRegistrationNumber();

		assertNotEquals(ticket.getOutTime(), null);
		assertNotEquals(ticket.getPrice(), 0);
	}

	static Ticket getTicketWithRegistrationNumber()
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
}
