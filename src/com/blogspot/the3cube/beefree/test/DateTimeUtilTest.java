package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.DateTimeUtil;
/**
 * this class test the DateTimeUtil functionality.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 */
public class DateTimeUtilTest {
	// date without year specified
	private final static String DATE01 = "31/12";
	private final static String DATE02 = "1/1";
	// month in name, date in ordinal number
	private final static String DATE03 = "2ND JAN";
	private final static String DATE04 = "11th january";
	// date in cardinal number
	private final static String DATE05 = "feb 5";
	// full date format
	private final static String DATE06 = "11/2/2012";
	// date in cardinal number in reversed order
	private final static String DATE07 = "29 dec";
	private final static String DATE08 = "October 28";
	// date and month separated by "-"
	private final static String DATE09 = "26-12";
	// time and "AM" separated
	private final static String TIME01 = "9 AM";
	// time and "hrs" stick together
	private final static String TIME02 = "9hrs";
	// time in pm format
	private final static String TIME03 = "3pm";
	// special case for 12:00:00
	private final static String TIME04 = "12 noon";
	// time without second
	private final static String TIME05 = "19:30";
	// time without second and has pm format
	private final static String TIME06 = "10:30 pm";

	private final static String[] INPUTS = { DATE01, DATE02, DATE03, DATE04,
			DATE05, DATE06, DATE07, DATE08, DATE09, TIME01, TIME02, TIME03,
			TIME04, TIME05, TIME06 };

	private final static String Expected01 = "2011-12-31";
	private final static String Expected02 = "2012-01-01";
	private final static String Expected03 = "2012-01-02";
	private final static String Expected04 = "2012-01-11";
	private final static String Expected05 = "2012-02-05";
	private final static String Expected06 = "2012-02-11";
	private final static String Expected07 = "2011-12-29";
	private final static String Expected08 = "2012-10-28";
	private final static String Expected09 = "2011-12-26";
	private final static String Expected10 = "09:00:00";
	private final static String Expected11 = "09:00:00";
	private final static String Expected12 = "15:00:00";
	private final static String Expected13 = "12:00:00";
	private final static String Expected14 = "19:30:00";
	private final static String Expected015 = "22:30:00";

	private final static String[] Expected = { Expected01, Expected02,
			Expected03, Expected04, Expected05, Expected06, Expected07,
			Expected08, Expected09, Expected10, Expected11, Expected12,
			Expected13, Expected14, Expected015 };

	/**
	 * this method will perform the testing.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 23 October 2011
	 * @finish 23 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 */
	@Test
	public void test() {
		// test date
		for (int i = 0; i < 9; i++) {
			boolean matched = DateTimeUtil.DateParser(INPUTS[i]).toString()
					.equals(Expected[i]);
			if (!matched) {
				System.out.println(DateTimeUtil.DateParser(INPUTS[i])
						.toString());
				System.out.println(Expected[i]);
			}
			assertTrue(matched);
		}
		// test time
		for (int i = 9; i < 15; i++) {
			boolean matched = DateTimeUtil.TimeParser(INPUTS[i]).toString()
					.equals(Expected[i]);
			if (!matched) {
				System.out.println("Input:    "
						+ DateTimeUtil.TimeParser(INPUTS[i]).toString());
				System.out.println("Expected: " + Expected[i]);
			}
			assertTrue(matched);
		}
	}
}
