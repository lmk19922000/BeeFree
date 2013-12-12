package com.blogspot.the3cube.beefree.util;


/**
 * The class will hold the global variable for the software.<br>
 *
 * @author		The3Cube
 * @since		v0.1
 * @version		v0.2
 */
public class Variable {
	public final static String LOG_FILE = "log.txt";
	public final static Log DEFAULT_LOG_LEVEL = Log.DEBUG;

	public final static String TEST_TASK_FILENAME = "taskcontainertest.csv";
	public final static String TEST_BLOCKOUT_FILENAME = "blockoutcontainertest.csv";
	
	public final static String TASKS_FILENAME = "tasks.csv";
	public final static String BLOCKOUTS_FILENAME = "blockouts.csv";

	public final static String COMMA = ",";
	public final static String NEW_LINE = System.getProperty("line.separator");
	public final static String SPACE = " ";
	public final static String WHITESPACE = " ";
	
	public final static String GMT = "+08:00";
	public final static String GMT_DATE_ONLY = "+00:00";
	
	public final static int MIN_WIDTH = 680;
	public final static int MIN_HEIGHT = 400;
}
