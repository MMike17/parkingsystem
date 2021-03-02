package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

	/**
	 * Tests DataBaseConfig.closePreparedStatement()
	 * 
	 * @see com.parkit.parkingsystem.config.DataBaseConfig#closePreparedStatement(java.sql.PreparedStatement)
	 * @see java.sql.PreparedStatement
	 */
	@Test
	public void testClosePreparedStatement()
	{
		// GIVEN
		Connection testConnection = null;
		PreparedStatement testStatement = null;
		boolean expectedStatementCloseState = true;

		try
		{
			testConnection = dataBaseConfig.getConnection();
			testStatement = testConnection.prepareStatement("SELECT 1");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		// WHEN
		dataBaseConfig.closePreparedStatement(testStatement);

		// THEN
		try
		{
			assertEquals(expectedStatementCloseState, testStatement.isClosed());
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Tests DataBaseConfig.closeResultSet()
	 * 
	 * @see com.parkit.parkingsystem.config.DataBaseConfig#closeResultSet(java.sql.ResultSet)
	 * @see java.sql.ResultSet
	 */
	@Test
	public void testCloseResultSet()
	{
		// GIVEN
		Connection testConnection = null;
		PreparedStatement testStatement = null;
		ResultSet testResults = null;
		boolean expectedResultSetCloseState = true;

		try
		{
			testConnection = dataBaseConfig.getConnection();
			testStatement = testConnection.prepareStatement("Select 1");
			testResults = testStatement.executeQuery();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		// WHEN
		dataBaseConfig.closeResultSet(testResults);

		// THEN
		try
		{
			assertEquals(expectedResultSetCloseState, testResults.isClosed());
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
}