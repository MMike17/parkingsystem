package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Class used to test TicketDAO integration
 * 
 * @see com.parkit.parkingsystem.dao.TicketDAO
 * @author Mike Matthews
 */
@DisplayName("TicketDAO")
public class TicketDAOIT
{
	static TicketDAO testTicketDAO = new TicketDAO();
	static DataBaseTestConfig testDataBase = new DataBaseTestConfig();
	static DataBasePrepareService prepareDataBaseService = new DataBasePrepareService();

	@BeforeAll
	public static void setupTests()
	{
		testTicketDAO.dataBaseConfig = testDataBase;
	}

	@BeforeEach
	public void setupPerTest()
	{
		prepareDataBaseService.clearDataBaseEntries();
	}

	@AfterAll
	public static void cleanTests()
	{
		prepareDataBaseService.clearDataBaseEntries();
	}

	/**
	 * Tests TicketDAO.saveTicket
	 * 
	 * @see com.parkit.parkingsystem.dao.TicketDAO#saveTicket(com.parkit.parkingsystem.model.Ticket)
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
	@Test
	@Tag("Save ticket in databaase")
	public void testSaveTicket()
	{
		// GIVEN
		String testVehicleRegNumber = "SAVETEST";
		Ticket testTicket = generateTestTicket(testVehicleRegNumber);
		boolean expectedRowState = true;

		// WHEN
		testTicketDAO.saveTicket(testTicket);

		// THEN
		try
		{
			Connection testConnection = testDataBase.getConnection();
			PreparedStatement testStatement = testConnection.prepareStatement("Select * from ticket where VEHICLE_REG_NUMBER=?");
			testStatement.setString(1, testVehicleRegNumber);

			ResultSet testResults = testStatement.executeQuery();
			assertEquals(expectedRowState, testResults.next());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Tests TicketDAO.getTicket
	 * 
	 * @see com.parkit.parkingsystem.dao.TicketDAO#getTicket(String)
	 * @see com.parkit.parkingsystem.model.Ticket#getVehicleRegNumber()
	 */
	@Test
	@Tag("Get ticket from database")
	public void testGetTicket()
	{
		// GIVEN
		String testVehicleRegNumber = "GETTEST";
		Ticket testTicket = generateTestTicket(testVehicleRegNumber);
		testTicketDAO.saveTicket(testTicket);

		// WHEN
		Ticket retrievedTicket = testTicketDAO.getTicket(testVehicleRegNumber);

		// THEN
		assertNotEquals(null, retrievedTicket);
		assertEquals(testVehicleRegNumber, retrievedTicket.getVehicleRegNumber());
	}

	/**
	 * Tests TicketDAO.updateTicket
	 * 
	 * @see com.parkit.parkingsystem.dao.TicketDAO#updateTicket(com.parkit.parkingsystem.model.Ticket)
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
	@Test
	@Tag("Update ticket in databaase")
	public void testUpdateTicket()
	{
		// GIVEN
		String testVehicleRegNumber = "UPDTTEST";
		Ticket testTicket = generateTestTicket(testVehicleRegNumber);
		testTicketDAO.saveTicket(testTicket);

		// update index
		testTicket = testTicketDAO.getTicket(testVehicleRegNumber);
		testTicket.setPrice(10);
		testTicket.setOutTime(new Date());

		// WHEN
		boolean completedQuerry = testTicketDAO.updateTicket(testTicket);

		// THEN
		if (!completedQuerry)
			fail("Failed ticket update");

		assertEquals(testTicketDAO.getTicket(testVehicleRegNumber).getPrice(), testTicket.getPrice());
	}

	/**
	 * Tests TicketDAO.getVehicleOccurence
	 * 
	 * @see com.parkit.parkingsystem.dao.TicketDAO#getVehicleOccurence(String)
	 */
	@Test
	@Tag("Count vehicle occurences in database")
	public void testOccurencesCount() {
		// GIVEN
		final String vehicleRegNumber = "TEST";
		int ticketCount = 0, targetCount = 4;

		while (ticketCount < targetCount) {
			ticketCount++;

			Ticket testData = new Ticket();
			testData.setVehicleRegNumber(vehicleRegNumber);
			testData.setInTime(new Date(System.currentTimeMillis() - (10 * 60 * 1000) * (ticketCount + 1)));
			testData.setOutTime(new Date());
			testData.setParkingSpot(new ParkingSpot(ticketCount + 1, ParkingType.CAR, false));

			prepareDataBaseService.insertTestTicket(testData);
		}

		// WHEN
		int occurences = testTicketDAO.getVehicleOccurence(vehicleRegNumber);

		// THEN
		assertEquals(targetCount, occurences);
	}

	Ticket generateTestTicket(String vehicleRegNumber)
	{
		Ticket testTicket = new Ticket();
		testTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		testTicket.setVehicleRegNumber(vehicleRegNumber);
		testTicket.setPrice(0);
		testTicket.setInTime(new Date(new Date().getTime() - 10 * 1000 * 60));
		testTicket.setOutTime(null);

		return testTicket;
	}
}