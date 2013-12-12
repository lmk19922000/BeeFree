package com.blogspot.the3cube.beefree.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import com.blogspot.the3cube.beefree.storage.FileSystem;

/**
 * This log the event of BeeFree.<br>
 * 
 * @author The3Cube
 * @since v0.1
 * @version v0.1
 * 
 */
public class Logger {
	private static Logger logger_;

	private String logFile_ = Variable.LOG_FILE;
	private Log logLevel_ = Variable.DEFAULT_LOG_LEVEL;

	private static final boolean APPEND_TO_FILE = true;

	/**
	 * construct the Logger object.<br>
	 */
	private Logger() {
	}

	/**
	 * construct the Logger object.<br>
	 * 
	 * @param filename
	 *            the file to log to.
	 * @param logLevel
	 *            the log level of the logger.
	 */
	private Logger(String filename, Log logLevel) {
		logFile_ = filename;
		logLevel_ = logLevel;
	}

	/**
	 * allow the object to obtain a logger instance for logging.<br>
	 * only one log object is allow as it log to the same file.<br>
	 * 
	 * @programmer Chua Jie Sheng
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate
	 * 
	 * @since v0.2
	 * @return a logger object for use.
	 */
	public static Logger getInstance() {
		if (logger_ == null) {
			logger_ = new Logger(Variable.LOG_FILE, Variable.DEFAULT_LOG_LEVEL);
		}
		return logger_;
	}

	/**
	 * this method will log if the logLevel is set to DEBUG.<br>
	 * 
	 * @programmer Chua Jie Sheng
	 * @start 13 September 2011
	 * @finish 14 September 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 14 September 2011
	 * 
	 * @since v0.1
	 * 
	 * @param className
	 *            the classname which called this logger.
	 * @param methodName
	 *            the method of the class which called this logger.
	 * @param message
	 *            the message to be printed into the logger.
	 */
	public void debug(String className, String methodName, String message) {
		if (logLevel_ == Log.DEBUG) {
			String log = "";
			log += "[" + className + "]";
			log += "[" + methodName + "]";
			log += " " + message + "";
			print(log);
		}
	}

	/**
	 * this method will log all message as ERROR is a higher priority message.<br>
	 * 
	 * @programmer Chua Jie Sheng
	 * @start 13 September 2011
	 * @finish 14 September 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 14 September 2011
	 * 
	 * @since v0.1
	 * 
	 * @param className
	 *            the classname which called this logger.
	 * @param methodName
	 *            the method of the class which called this logger.
	 * @param message
	 *            the message to be printed into the logger.
	 */
	public void error(String className, String methodName, String message) {
		String log = "";
		log += "[" + className + "]";
		log += "[" + methodName + "]";
		log += " " + message + "";
		print(log);
	}

	/**
	 * this method will get the data and time in string for printing in the log
	 * file<br>
	 * 
	 * @programmer Chua Jie Sheng
	 * @start 13 September 2011
	 * @finish 14 September 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 14 September 2011
	 * 
	 * @since v0.1
	 * 
	 * @return date and time formatted in "DD-MM-YYYY HH:MM".
	 */
	private String getTime() {
		String time = "";

		Calendar today = Calendar.getInstance();

		time += today.get(Calendar.DAY_OF_MONTH);
		time += "-";
		time += today.get(Calendar.MONTH) + 1;
		time += "-";
		time += today.get(Calendar.YEAR);
		time += " ";
		time += String.format("%2d", today.get(Calendar.HOUR_OF_DAY));
		time += ":";
		time += String.format("%2d", today.get(Calendar.MINUTE));

		return time;
	}

	/**
	 * this method will print the message out to the log file<br>
	 * it will not print the log message if:<br>
	 * 1. unable to print to the log file specify (default or user specify)
	 * (i.e. exception)<br>
	 * 2. FileWriter threw exception for any other reason<br>
	 * 
	 * @programmer Chua Jie Sheng
	 * @start 13 September 2011
	 * @finish 14 September 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 14 September 2011
	 * 
	 * @since v0.1
	 * 
	 * @param message
	 *            the message to print to the writer.
	 */
	private void print(String message) {
		FileSystem.appendToTextFile(logFile_, getTime() + Variable.WHITESPACE
				+ message);
	}

	/**
	 * @return the logFile_.
	 */
	public String getLogFile_() {
		return logFile_;
	}

	/**
	 * @return the logLevel_.
	 */
	public Log getLogLevel_() {
		return logLevel_;
	}

	/**
	 * @param logFile_
	 *            the logFile_ to set.
	 */
	public void setLogFile_(String logFile_) {
		this.logFile_ = logFile_;
	}

	/**
	 * @param logLevel_
	 *            the logLevel_ to set.
	 */
	public void setLogLevel_(Log logLevel_) {
		this.logLevel_ = logLevel_;
	}

}
