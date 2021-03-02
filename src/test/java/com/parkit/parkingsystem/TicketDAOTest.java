package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	 * Tests TicketDAO.saveTicket()
	 * 
	 * @see com.parkit.parkingsystem.dao.TicketDAO#saveTicket(com.parkit.parkingsystem.model.Ticket)
	 * @see com.parkit.parkingsystem.model.Ticket
	 */
	@Test
	public void testSaveTicket()
	{
		// GIVEN
		String testVehicleRegNumber = "SAVETEST";
		Ticket testTicket = new Ticket();
		testTicket.setId(1);
		testTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		testTicket.setVehicleRegNumber(testVehicleRegNumber);
		testTicket.setPrice(0);
		testTicket.setInTime(new Date());
		testTicket.setOutTime(null);
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

	// what methods do we need to test ?

	// Ticket getTicket(String vehicleRegNumber)
	// boolean updateTicket(Ticket ticket)
}