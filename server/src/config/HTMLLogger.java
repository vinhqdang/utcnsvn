package config;

import java.io.FileOutputStream;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;


import data.exception.ConfigurationException;

public class HTMLLogger extends Logger {
	
	private static HTMLLogger log;

	static {
		log = new HTMLLogger();
	}

	private HTMLLogger(String debugFile, String debugLevel) {
		super("HTMLLogger");
		HTMLLayout layout = new HTMLLayout();
		WriterAppender appender = null;
		try {
			FileOutputStream output = new FileOutputStream(debugFile);
			appender = new WriterAppender(layout, output);
		} catch (Exception e) {
		}

		addAppender(appender);
		setLevel((Level) Level.DEBUG);
	}

	private HTMLLogger() {
		super("HTMLLogger");
		try {
			String debugFile = Configuration.getInstance().getDebugFile();
			String debugLevel = Configuration.getInstance().getDebugLevel();
			new HTMLLogger(debugFile, debugLevel);
		} catch (ConfigurationException e) {
			error(e.getMessage());
			new HTMLLogger("log.html", "info");
		}
	}
	
	public static void debug(String message){
		log.debug(message);
	}
	
	public static void info(String message){
		log.info(message);
	}
	
	public static void error(String message){
		log.error(message);
	}
	
	public static void fatal(String message, Exception e){
		log.fatal(message,e);
	}
	
	public static void warn(String message){
		log.warn(message);
	}
	
	public static void warn(String message,Exception e){
		log.warn(message, e);
	}


}
