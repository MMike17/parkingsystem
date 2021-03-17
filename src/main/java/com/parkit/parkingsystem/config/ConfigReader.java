package com.parkit.parkingsystem.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Class used to read infos from .conf file
 * 
 * @see com.parkit.parkingsystem.config.DataBaseConfig
 * @author MikeMatthews
 */
public class ConfigReader
{
	final static String configFilePath = "src/main/resources/config.conf";
	final static String userJSONKey = "user";
	final static String passwordJSONKey = "password";

	String userName = null;
	String password = null;

	/**
	 * Caches infos from .conf file
	 */
	public ConfigReader()
	{
		JSONParser parser = new JSONParser();

		try
		{
			InputStream inputStream = new FileInputStream(configFilePath);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");

			Object fileContent = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) fileContent;

			userName = (String) jsonObject.get(userJSONKey);
			password = (String) jsonObject.get(passwordJSONKey);
		}
		catch(Exception exception)
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