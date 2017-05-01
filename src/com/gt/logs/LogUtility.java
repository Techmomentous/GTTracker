package com.gt.logs;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogUtility {

	private static final Logger logger = LogManager.getLogger(LogUtility.class.getName());

	public static void LogDebug(String message, String className) {

		logger.trace("---------------------------------------");
		logger.debug(message + " : " + className);
		logger.trace("---------------------------------------");
	}

	public static void LogWarn(String message, String className) {

		logger.trace("---------------------------------------");
		logger.warn(message + " : " + className);
		logger.trace("---------------------------------------");
	}

	public static void LogInfo(String message, String className) {

		logger.trace("---------------------------------------");
		logger.info(message + " : " + className);
		logger.trace("---------------------------------------");
	}

	public static void LogError(String message, String className) {

		logger.trace("---------------------------------------");
		logger.error(message + " : " + className);
		logger.trace("---------------------------------------");
	}

}
