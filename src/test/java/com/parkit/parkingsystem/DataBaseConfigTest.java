package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.parkit.parkingsystem.config.DataBaseConfig;

import org.junit.jupiter.api.Test;

/**
 * Class used to unit test DataBaseConfig
 * 
 * @see com.parkit.parkingsystem.config.DataBaseConfig
 * @author Mike Matthews
 */
public class DataBaseConfigTest
{
	static DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Tests DataBaseConfig.getConnection
	 * 
	 * @see com.parkit.parkingsystem.config.DataBaseConfig#getConnection()
	 */
	@Test
	public void testGetConnection()
	{
		// GIVEN
		String expectedDatabaseUrl = "jdbc:mysql://localhost:3306/prod?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
		boolean expectedConnectionClosedState = false;

		// WHEN
		Connection testConnection = null;

		try
		{
			testConnection = dataBaseConfig.getConnection();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		// THEN
		if (testConnection == null)
			fail("Couldn't retrieve connection");

		try
		{
			assertEquals(expectedConnectionClosedState, testConnection.isClosed());

			DatabaseMetaData metaData = testConnection.getMetaData();

			assertEquals(expectedDatabaseUrl, metaData.getURL());
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Tests DataBaseConfig.closeConnection()
	 * 
	 * @see com.parkit.parkingsystem.config.DataBaseConfig#closeConnection(java.sql.Connection)
	 * @see java.sql.Connection
	 */
	@Test
	public void testCloseConnection()
	{
		// GIVEN
		boolean expectedConnectionClosedState = true;
		Connection testConnection = null;

		try
		{
			testConnection = dataBaseConfig.getConnection();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		if (testConnection == null)
			fail("Couldn't retrieve connection to start test");

		// WHEN
		dataBaseConfig.closeConnection(testConnection);

		// THEN
		try
		{
			assertEquals(expectedConnectionClosedState, testConnection.isClosed());
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
}