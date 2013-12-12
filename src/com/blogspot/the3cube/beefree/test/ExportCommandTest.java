package com.blogspot.the3cube.beefree.test;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.cmd.ExportCommand;

/**
 * this class test the ExportCommand functionality.<br>
 * 
 * @author 	Chua Jie Sheng
 * @since 	v0.1
 * @version v0.2
 * 
 */
public class ExportCommandTest {

	/**
	 * this method will perform the required setup.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		11 October 2011
	 * @finish 		11 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @throws 		java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * this method will perform the required setup.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		11 October 2011
	 * @finish 		11 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @throws 		java.lang.Exception
	 */
	@Test
	public void naturalLanguageTest() {
		final String[] KEYWORDS = { "to", "with", "gdocs", "file" };
		String testcase01 = "to file file1.txt to gcal with user1:pass1";
		String testcase02 = "to file file1.txt";
		String testcase03 = "to gdocs [BeeFree] Tasks with user1:pass1";
		
		ExportCommand cmd = new ExportCommand();
		Vector<String> result01 = cmd.parseNaturalLanguage(testcase01, KEYWORDS);
		for (int i = 0; i < result01.size(); i++) {
			System.out.println(result01.get(i));
		}
		System.out.println("-");
		Vector<String> result02 = cmd.parseNaturalLanguage(testcase02, KEYWORDS);
		for (int i = 0; i < result02.size(); i++) {
			System.out.println(result02.get(i));
		}
		System.out.println("-");
		Vector<String> result03 = cmd.parseNaturalLanguage(testcase03, KEYWORDS);
		for (int i = 0; i < result03.size(); i++) {
			System.out.println(result03.get(i));
		}
		System.out.println("-");
	}

}
