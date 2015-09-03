package zyy.config.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyLogger {

	static {
		PropertyConfigurator.configure("src/zyy/config/log/log4j.properties");
	}

	public static Logger getLogger(Class<?> c) {
		return Logger.getLogger(c.getClass());
	}

	public static Logger getLogger() {
		return Logger.getLogger(MyLogger.class);
	}
}
