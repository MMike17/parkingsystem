package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class used to unit test ParkingSpotDAO
 * 
 * @see com.parkit.parkingsystem.dao.ParkingSpotDAO
 * @author Mike Matthews
 */
public class ParkingSpotDAOTest
{
	static ParkingSpotDAO parkingSpotDAO;
	static DataBasePrepareService dataBasePrepareService;

	@BeforeAll
	public static void setupTests()
	{
		parkingSpotDAO = new ParkingSpotDAO();
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	public void setupPerTest()
	{
		dataBasePrepareService.clearDataBaseEntries();
	}

	/**
	 * Tests ParkingSpotDAO.getNextAvailableSlot for ParkingType.CAR
	 * 
	 * @see com.parkit.parkingsystem.dao.ParkingSpotDAO#getNextAvailableSlot(com.parkit.parkingsystem.constants.ParkingType)
	 * @see com.parkit.parkingsystem.constants.ParkingType
	 */
	@Test
	public void testNextAvailableParkingSpotForCar()
	{
		// GIVEN
		ArrayList<String> extractedData = getDataFromDataBase("select * from parking where AVAILABLE = true and TYPE = ?", "PARKING_NUMBER");

		if (extractedData.size() < 0)
			fail("Retrieved data is empty");

		// WHEN
		Integer selectedParkingSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		// THEN
		assertEquals(selectedParkingSpot.toString(), extractedData.get(0));
	}

	/**
	 * Tests ParkingSpotDAO.getNextAvailableSlot for ParkingType.BIKE
	 * 
	 * @see com.parkit.parkingsystem.dao.ParkingSpotDAO#getNextAvailableSlot(com.parkit.parkingsystem.constants.ParkingType)
	 * @see com.parkit.parkingsystem.constants.ParkingType
	 */
	@Test
	public void testNextAvailableParkingSpotForBike()
	{
		// public int getNextAvailableSlot(ParkingType parkingType)
	}

	/**
	 * Tests ParkingSpotDAO.updateParking for ParkingType.CAR
	 * 
	 * @see com.parkit.parkingsystem.dao.ParkingSpotDAO#updateParking(com.parkit.parkingsystem.model.ParkingSpot)
	 * @see com.parkit.parkingsystem.constants.ParkingType
	 */
	@Test
	public void testParkingUpdateForCar()
	{
		// public boolean updateParking(ParkingSpot parkingSpot)
	}

	/**
	 * Tests ParkingSpotDAO.updateParking for ParkingType.BIKE
	 * 
	 * @see com.parkit.parkingsystem.dao.ParkingSpotDAO#updateParking(com.parkit.parkingsystem.model.ParkingSpot)
	 * @see com.parkit.parkingsystem.constants.ParkingType
	 */
	@Test
	public void testParkingUpdateForBike()
	{
		// public boolean updateParking(ParkingSpot parkingSpot)
	}

	ArrayList<String> getDataFromDataBase(String command, String columnName)
	{
		ArrayList<String> results = new ArrayList<String>();
		DataBaseConfig dbConfig = new DataBaseTestConfig();

		try
		{
			Connection connexion = dbConfig.getConnection();
			PreparedStatement statement = connexion.prepareStatement(command);
			statement.setString(1, ParkingType.CAR.toString());
			ResultSet retrievedResults = statement.executeQuery();

			while (retrievedResults.next())
				results.add(retrievedResults.getString(columnName));

			dbConfig.closeResultSet(retrievedResults);
			dbConfig.closePreparedStatement(statement);
		}
		catch (SQLException e)
		{
			fail(e);
		}
		catch (ClassNotFoundException e)
		{
			fail(e);
		}

		return results;
	}
}