package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Class used to unit test ParkingService
 * 
 * @see com.parkit.parkingsystem.service.ParkingService
 * @author Mike Matthews
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ParkingService")
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			when(inputReaderUtil.readSelection()).thenReturn(1);

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
			when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

	/**
	 * Tests ParkingService.getNextparkingNumberIfAvailable
	 * 
	 * @see com.parkit.parkingsystem.service.ParkingService#getNextParkingNumberIfAvailable()
	 */
	@Test
	@Tag("Get next available parking number")
	public void getNextParkingNumberIfAvailableTest()
	{
		// GIVEN
		// WHEN
		parkingService.getNextParkingNumberIfAvailable();

		// THEN
		verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
	}

	/**
	 * Tests ParkingService.processIncomingVehicle
	 * 
	 * @see com.parkit.parkingsystem.service.ParkingService#processIncomingVehicle()
	 */
	@Test
	@Tag("Process incoming vehicle")
	public void processIncomingVehicleTest()
	{
		// GIVEN
		// WHEN
		parkingService.processIncomingVehicle();

		// THEN
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	/**
	 * Tests ParkingService.processExitingVehicle
	 * 
	 * @see com.parkit.parkingsystem.service.ParkingService#processExitingVehicle()
	 */
    @Test
	@Tag("Process exiting vehicle")
    public void processExitingVehicleTest(){
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }
}