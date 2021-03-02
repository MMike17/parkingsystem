package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

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
	static DataBaseTestConfig testDataBase = new DataBaseTestConfig();
	static ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
	static DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();

	@BeforeAll
	public static void setupTests()
	{
		parkingSpotDAO.dataBaseConfig = testDataBase;
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
		final int expectedParkingSpot = 1;

		// WHEN
		int selectedParkingSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		// THEN
		assertEquals(expectedParkingSpot, selectedParkingSpot);
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
		// GIVEN
		final int expectedParkingSpot = 4;

		// WHEN
		int selectedParkingSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

		// THEN
		assertEquals(expectedParkingSpot, selectedParkingSpot);
	}

	/**
	 * Tests ParkingSpotDAO.updateParking for cars
	 * 
	 * @see com.parkit.parkingsystem.dao.ParkingSpotDAO#updateParking(com.parkit.parkingsystem.model.ParkingSpot)
	 */
	@Test
	public void testParkingUpdateForCar()
	{
		// GIVEN
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		// WHEN
		boolean updateSucceeded = parkingSpotDAO.updateParking(parkingSpot);

		// THEN
		if (updateSucceeded)
			assertEquals(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), parkingSpot.getId() + 1);
		else
			fail("Failed to update ticket");
	}

	/**
	 * Tests ParkingSpotDAO.updateParking for bikes
	 * 
	 * @see com.parkit.parkingsystem.dao.ParkingSpotDAO#updateParking(com.parkit.parkingsystem.model.ParkingSpot)
	 */
	@Test
	public void testParkingUpdateForBike()
	{
		// GIVEN
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);

		// WHEN
		boolean updateSucceeded = parkingSpotDAO.updateParking(parkingSpot);

		// THEN
		if (updateSucceeded)
			assertEquals(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE), parkingSpot.getId() + 1);
		else
			fail("Failed to update ticket");
	}
}