package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import org.junit.jupiter.api.BeforeAll;

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

	@BeforeAll
	public static void setupTests()
	{
		testTicketDAO.dataBaseConfig = testDataBase;
	}

	// what methods do we need to test ?

	// boolean saveTicket(Ticket ticket)
	// Ticket getTicket(String vehicleRegNumber)
	// boolean updateTicket(Ticket ticket)
}