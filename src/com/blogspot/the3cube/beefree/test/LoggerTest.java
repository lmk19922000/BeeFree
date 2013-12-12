package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.util.Log;
import com.blogspot.the3cube.beefree.util.Logger;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the logger functionality.<br>
 * 
 * @author 	Chua Jie Sheng
 * @since 	v0.1
 * @version v0.1
 * 
 */
public class LoggerTest {

	private static final String CLASSNAME = "LoggerTest";

	private Logger log_ = null;
	private String filename_ = Variable.LOG_FILE;
	private Log logLevel_ = Log.DEBUG;

	/**
	 * this method setup the tester<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		13 September 2011
	 * @finish 		14 September 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	14 September 2011
	 * 
	 * @since 		v0.1
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		FileSystem.clearTextFile(Variable.LOG_FILE);
		log_ = Logger.getInstance();
	}

	/**
	 * this method clean up the tester.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		13 September 2011
	 * @finish 		14 September 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	14 September 2011
	 * 
	 * @since 		v0.1
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log_ = null;
	}

	/**
	 * Test method for
	 * {@link com.blogspot.the3cube.beefree.util.Logger#debug(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testDebug() {
		String message = "debug message";
		
		String result = "";
		result += "[" + CLASSNAME + "]";
		result += "[" + "testDebug" + "]";
		result += " " + message + "";
		
		log_.debug(CLASSNAME, "testDebug", message);

		boolean found = findString(result);

		assertTrue("If found debug message", found);
	}

	/**
	 * Test method for
	 * {@link com.blogspot.the3cube.beefree.util.Logger#error(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testError() {
		String message = "error message";

		String result = "";
		result += "[" + CLASSNAME + "]";
		result += "[" + "testError" + "]";
		result += " " + message + "";
		
		log_.error(CLASSNAME, "testError", message);

		boolean found = findString(result);

		assertTrue("If found error message", found);
	}
	
	/**
	 * this method search for a string from the text file.<br>
	 *
	 * @programmer 	Chua Jie Sheng
	 * @start 		13 September 2011
	 * @finish 		14 September 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	14 September 2011
	 * 
	 * @since 		v0.1
	 * 
	 * @param search 	the string to find.
	 * @return 			if the string is found.
	 */
	private boolean findString(String search) {
		boolean found = false;
		File file = new File(filename_);
		try {
			String line = null;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(search) > -1) {
					found = true;
				}
			}
		} catch (FileNotFoundException f) {
			System.err.format("FileNotFoundException: %s%n", f);
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		return found;
	}
}
