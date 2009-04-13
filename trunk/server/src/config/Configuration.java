package config;

import java.io.IOException;
import java.util.Properties;

import data.exception.ConfigurationException;


public class Configuration {

	private static Configuration configuration;
	
	private Configuration(){
		super();
	}
	
	public String getProperty(String propertyName) throws IOException {
		Properties configFile = new Properties();
		configFile.load(getClass().getClassLoader().getResourceAsStream("/server.properties"));
		return configFile.getProperty(propertyName);
	}
	
	
	public String getConnectionString()throws ConfigurationException{
		try {
			return getProperty("connectionString");
		} catch (IOException e) {		
			throw new ConfigurationException("connectionString");
		}
	}
	
	public String getDebugFile()throws ConfigurationException{
		try {
			return getProperty("debug.file");
		} catch (IOException e) {	
			throw new ConfigurationException("debug.file");
		}
	}
	
	public String getDebugLevel()throws ConfigurationException{
		try {
			return getProperty("debug.level");
		} catch (IOException e) {		
			throw new ConfigurationException("debug.level");
		}
	}
	
	public String getRepositoryFolder()throws ConfigurationException{
		try {
			return getProperty("repository.folder");
		} catch (IOException e) {		
			throw new ConfigurationException("repository.folder");
		}
	}
	
	public static Configuration getInstance(){
		if (configuration == null){
			configuration =  new Configuration();
		}
		return configuration;
	}
	
}
