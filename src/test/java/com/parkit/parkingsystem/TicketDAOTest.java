package com.parkit.parkingsystem;

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
import org.junit.jupiter.api.Test;

/**
 * Class used to unit test TicketDAO
 * 
 * @see com.parkit.parkingsystem.dao.TicketDAO
 * @author Mike Matthews
 */
public class TicketDAOTest
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
	public void testGetTicket()
	{
		// what should we check ?
		// if we get ticket

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