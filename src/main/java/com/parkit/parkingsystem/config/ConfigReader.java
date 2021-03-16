package com.parkit.parkingsystem.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class used to read infos from .conf file
 * 
 * @see com.parkit.parkingsystem.config.DataBaseConfig
 * @author MikeMatthews
 */
public class ConfigReader
{
	final String configFilePath = "src/main/resources/config.conf";
	final String userJSONKey = "user";
	final String passwordJSONKey = "password";

	String userName = null;
	String password = null;

	/**
	 * Caches infos from .conf file
	 */
	public ConfigReader()
	{
		JSONParser parser = new JSONParser();

		try(FileReader reader = new FileReader(configFilePath))
		{
			Object fileContent = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) fileContent;

			userName = (String) jsonObject.get(userJSONKey);
			password = (String) jsonObject.get(passwordJSONKey);
		}
		catch(IOException | ParseException exception)
		{
			System.out.println("Couldn't read config from file\n" + exception);
		}
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPassword()
	{
		return password;
	}
}