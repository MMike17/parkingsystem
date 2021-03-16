package com.parkit.parkingsystem.config;

import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class used to read infos from .config file
 * 
 * @see com.parkit.parkingsystem.config.DataBaseConfig
 * @author MikeMatthews
 */
public class ConfigReader
{
	final String configFilePath = "../../resources/config.config";
	final String userJSONKey = "user";
	final String passwordJSONKey = "password";

	String userName = null;
	String password = null;

	/**
	 * Caches infos from .config file
	 */
	public ConfigReader()
	{
		JSONParser parser = new JSONParser();

		try
		{
			Object fileContent = parser.parse(new FileReader(configFilePath));
			JSONObject jsonObject = (JSONObject) fileContent;

			userName = jsonObject.getString(userJSONKey);
			password = jsonObject.getString(passwordJSONKey);
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