package ivc.config;

import ivc.data.exception.ConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Configuration {

	private static Configuration configuration;
	private static Properties configFile;

	private Configuration() {
		super();
	}

	public String getProperty(String propertyName) throws IOException {
		return configFile.getProperty(propertyName);
	}

	public void setProperty(String propertyName, String propertyValue)
			throws IOException {
		configFile.setProperty(propertyName, propertyValue);
	}

	public String getConnectionString() throws ConfigurationException {
		try {
			return getProperty("connectionString");
		} catch (IOException e) {
			throw new ConfigurationException("connectionString");
		}
	}

	public String getDebugFile() throws ConfigurationException {
		try {
			return getProperty("debug.file");
		} catch (IOException e) {
			throw new ConfigurationException("debug.file");
		}
	}

	public String getDebugLevel() throws ConfigurationException {
		try {
			return getProperty("debug.level");
		} catch (IOException e) {
			throw new ConfigurationException("debug.level");
		}
	}

	public String getRepositoryFolder() throws ConfigurationException {
		try {
			return getProperty("repository.folder");
		} catch (IOException e) {
			throw new ConfigurationException("repository.folder");
		}
	}

	public List<String> getClientHosts() throws ConfigurationException {
		String clientsStr;
		List<String> clientsHosts = new ArrayList<String>();
		try {
			clientsStr = getProperty("clients.hosts");
		} catch (IOException e) {
			throw new ConfigurationException("clients.hosts");
		}
		String[] hostsArr = clientsStr.split(";");
		for (String hostStr : hostsArr) {
			clientsHosts.add(hostStr);
		}
		return clientsHosts;
	}

	public void setClientHosts(List<String> hosts)
			throws ConfigurationException {
		String hostsStr = "";
		if (hosts == null) {
			return;
		}
		Iterator<String> it = hosts.iterator();
		while (it.hasNext()) {
			hostsStr += it.next() + ";";
		}
		hostsStr = hostsStr.substring(0, hostsStr.length() - 2);
		try {
			setProperty("clients.hosts", hostsStr);
		} catch (IOException e) {
			throw new ConfigurationException("clients.hosts");
		}
	}

	public static Configuration getInstance() throws ConfigurationException {
		if (configuration == null) {
			configuration = new Configuration();
			Properties configFile = new Properties();
			try {
				configFile.load(configuration.getClass().getClassLoader()
						.getResourceAsStream("/server.properties"));
				configuration.setConfigFile(configFile);
			} catch (IOException e) {
				throw new ConfigurationException("Config File");
			}
		}
		return configuration;
	}

	public void setConfigFile(Properties configFile) {
		Configuration.configFile = configFile;
	}

	public static Properties getConfigFile() {
		return configFile;
	}

}
