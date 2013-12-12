package com.blogspot.the3cube.beefree.logic;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * this class provide utilities for date and time handling.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 */
public class DateTimeUtil {

	/**
	 * this static method provides functionality of reading the different
	 * formatted date from the string.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The 3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a date-like input.
	 * @return date which the input supposes to mean.
	 */
	public static Date DateParser(String input) {
		Date date = null;
		input = input.trim();
		boolean isEmpty = input.isEmpty();

		if (!isEmpty) {
			date = parseFormalDateInput(input);

			if (date == null) {
				date = parseInformalDateInput(input);
			}
		}

		return date;
	}

	/**
	 * this static method provides functionality of reading the different
	 * formatted time from the string.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a time-like input.
	 * @return time which the input supposes to mean.
	 */
	public static Time TimeParser(String input) {
		Time time = null;
		input = input.trim();
		boolean isEmpty = input.isEmpty();

		if (!isEmpty) {
			time = parseFormalTimeInput(input);
			if (time == null) {
				time = parseInformalTimeInput(input);
			}
		}
		return time;
	}

	/**
	 * this static method will parse the formal date input format.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a date-like input.
	 * @return date which the input supposes to mean.
	 */
	public static Date parseFormalDateInput(String input) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		java.util.Date sdfDate;

		input = input.replace('/', '-');

		try {
			if (input.matches("[0-9]{4}(-|/)[0-9]{1,2}(-|/)[0-9]{1,2}")) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			} else if (input.matches("[0-9]{1,2}(-|/)[0-9]{1,2}(-|/)[0-9]{4}")) {
				sdf = new SimpleDateFormat("dd-MM-yyyy");
			} else if (input.matches("[0-9]{1,2}(-|/)[0-9]{1,2}(-|/)[0-9]{2}")) {
				input = fillYearSpecial(input);
				sdf = new SimpleDateFormat("dd-MM-yyyy");
			} else if (input.matches("[0-9]{1,2}(-|/)[0-9]{1,2}")) {
				boolean isNextYear;
				isNextYear = checkIfDateOfNextYear(input);
				input = getFullDate(input, isNextYear);
				sdf = new SimpleDateFormat("dd-MM-yyyy");
			} else if (input.matches("[a-zA-Z]*[ ][0-9]{1,2}[ ][0-9]{4}")) {
				sdf = new SimpleDateFormat("MMMMMMMMM dd yyyy");
			} else if (input
					.matches("[a-zA-Z]*[ ][0-9]{1,2}[a-zA-Z]{2}[ ][0-9]{4}")) {
				if (isDateFormat(input)) {
					input = removeOrdinalNumber(input);
					sdf = new SimpleDateFormat("MMMMMMMMM dd yyyy");
				}
			} else if (input.matches("[a-zA-Z]*[ ][0-9]{1,2}[ ][0-9]{2}")) {
				input = fillYearSpecial(input);
				sdf = new SimpleDateFormat("MMMMMMMMM dd yyyy");
			} else if (input
					.matches("[a-zA-Z]*[ ][0-9]{1,2}[a-zA-Z]{2}[ ][0-9]{2}")) {
				if (isDateFormat(input)) {
					input = removeOrdinalNumber(input);
					input = fillYearSpecial(input);
					sdf = new SimpleDateFormat("MMMMMMMMM dd yyyy");
				}
			} else if (input.matches("[a-zA-Z]*[ ][0-9]{1,2}")) {
				input = parseSpecialFormalDate(input, 1);
				sdf = new SimpleDateFormat("dd-MM-yyyy");
			} else if (input.matches("[a-zA-Z]*[ ][0-9]{1,2}[a-zA-Z]{2}")) {
				if (isDateFormat(input)) {
					input = removeOrdinalNumber(input);
					input = parseSpecialFormalDate(input, 1);
					sdf = new SimpleDateFormat("dd-MM-yyyy");
				}
			} else if (input.matches("[0-9]{1,2}[ ][a-zA-Z]*[ ][0-9]{4}")) {
				sdf = new SimpleDateFormat("dd MMMMMMMMM yyyy");
			} else if (input
					.matches("[0-9]{1,2}[a-zA-Z]{2}[ ][a-zA-Z]*[ ][0-9]{4}")) {
				if (isDateFormat(input)) {
					input = removeOrdinalNumber(input);
					sdf = new SimpleDateFormat("dd MMMMMMMMM yyyy");
				}
			} else if (input.matches("[0-9]{1,2}[ ][a-zA-Z]*[ ][0-9]{2}")) {
				input = fillYearSpecial(input);
				sdf = new SimpleDateFormat("dd MMMMMMMMM yyyy");
			} else if (input
					.matches("[0-9]{1,2}[a-zA-Z]{2}[ ][a-zA-Z]*[ ][0-9]{2}")) {
				if (isDateFormat(input)) {
					input = removeOrdinalNumber(input);
					input = fillYearSpecial(input);
					sdf = new SimpleDateFormat("dd MMMMMMMMM yyyy");
				}
			} else if (input.matches("[0-9]{1,2}[ ][a-zA-Z]*")) {
				input = parseSpecialFormalDate(input, 2);
				sdf = new SimpleDateFormat("dd-MM-yyyy");
			} else if (input.matches("[0-9]{1,2}[a-zA-Z]{2}[ ][a-zA-Z]*")) {
				if (isDateFormat(input)) {
					input = removeOrdinalNumber(input);
					input = parseSpecialFormalDate(input, 2);
					sdf = new SimpleDateFormat("dd-MM-yyyy");
				}
			}
			sdf.setLenient(false);
			sdfDate = sdf.parse(input);
			date = new Date(sdfDate.getTime());
		} catch (ParseException e) {
			date = null;
		}

		return date;
	}

	/**
	 * this static method will parse the informal date input format.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a date-like input.
	 * @return date which the input supposes to mean.
	 */
	public static Date parseInformalDateInput(String input) {
		Date date = null;

		input = input.trim();
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		int currentYear = c.get(Calendar.YEAR);

		if (input.compareToIgnoreCase("tomorrow") == 0) {
			int day = c.get(Calendar.DATE) + 1;

			String answer = getDate(day, currentMonth, currentYear);
			date = Date.valueOf(answer);
		} else if (input.compareToIgnoreCase("today") == 0) {
			int day = c.get(Calendar.DATE);

			String answer = getDate(day, currentMonth, currentYear);
			date = Date.valueOf(answer);
		} else {
			String[] splittedDate = input.split(" ");
			// handle cases "sunday", "mon",....
			if (splittedDate.length == 1) {
				int dayInput = getDateOfWeek(splittedDate[0]);
				if (dayInput != 0) {
					int day;
					if (dayInput - c.get(Calendar.DAY_OF_WEEK) > 0) {
						day = c.get(Calendar.DATE)
								+ (dayInput - c.get(Calendar.DAY_OF_WEEK)) % 7;
					} else {
						day = c.get(Calendar.DATE)
								+ (dayInput - c.get(Calendar.DAY_OF_WEEK) + 7)
								% 7;
					}
					String answer = getDate(day, currentMonth, currentYear);
					date = Date.valueOf(answer);
				}
			} else if (splittedDate.length == 2) {
				int dayInput = getDateOfWeek(splittedDate[1]);
				if (dayInput != 0) {
					// handle cases "this fri",....
					if (splittedDate[0].compareToIgnoreCase("this") == 0) {
						int day;
						if (dayInput - c.get(Calendar.DAY_OF_WEEK) > 0) {
							day = c.get(Calendar.DATE)
									+ (dayInput - c.get(Calendar.DAY_OF_WEEK))
									% 7;
						} else {
							day = c.get(Calendar.DATE)
									+ (dayInput - c.get(Calendar.DAY_OF_WEEK) + 7)
									% 7;
						}
						String answer = getDate(day, currentMonth, currentYear);
						date = Date.valueOf(answer);
					}
					// handle cases "next fri",....
					else if (splittedDate[0].compareToIgnoreCase("next") == 0) {
						int day;
						if (c.get(Calendar.DAY_OF_WEEK) != 1) {
							day = c.get(Calendar.DATE)
									+ (dayInput - c.get(Calendar.DAY_OF_WEEK))
									% 7 + 7;
						} else {
							day = c.get(Calendar.DATE)
									+ (dayInput - c.get(Calendar.DAY_OF_WEEK));
						}

						String answer = getDate(day, currentMonth, currentYear);
						date = Date.valueOf(answer);
					}
				}
			}
			// handle cases "next next fri",....
			else if (splittedDate.length == 3) {
				if ((splittedDate[0].compareToIgnoreCase("next") == 0)
						&& (splittedDate[1].compareToIgnoreCase("next") == 0)) {
					int dayInput = getDateOfWeek(splittedDate[2]);
					int day;
					if (c.get(Calendar.DAY_OF_WEEK) != 1) {
						day = c.get(Calendar.DATE)
								+ (dayInput - c.get(Calendar.DAY_OF_WEEK)) % 7
								+ 14;
					} else {
						day = c.get(Calendar.DATE)
								+ (dayInput - c.get(Calendar.DAY_OF_WEEK)) + 7;
					}

					String answer = getDate(day, currentMonth, currentYear);
					date = Date.valueOf(answer);
				}
			}
		}
		return date;
	}

	/**
	 * this static method handles the case when the date input does not specify
	 * the year. Eg: July 7<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a date-like input.
	 * @param flag
	 *            the flag indicating the order of the date and month
	 * @return a string representing date which the input supposes to mean.
	 */
	public static String parseSpecialFormalDate(String input, int flag)
			throws ParseException {
		assert flag == 1 || flag == 2;

		boolean isNextYear;
		SimpleDateFormat sdf;
		java.util.Date sdfDate;

		if (flag == 1) {
			sdf = new SimpleDateFormat("MMMMMMMMM dd");
		} else {
			sdf = new SimpleDateFormat("dd MMMMMMMMM");
		}
		sdfDate = sdf.parse(input);
		String tempDate = (new Date(sdfDate.getTime())).toString();
		tempDate = reverseOrder(tempDate);
		String[] array = tempDate.split("-");
		tempDate = "";
		// only get the date and the month
		tempDate = tempDate + array[0] + "-" + array[1];
		isNextYear = checkIfDateOfNextYear(tempDate);
		String answer = getFullDate(tempDate, isNextYear);

		return answer;
	}

	/**
	 * this static method reverses the order of the input string.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string representing date needed to be reversed.
	 * @return the reversed string.
	 */
	public static String reverseOrder(String input) {
		assert input.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}");

		String[] inputString = input.split("-");

		String answer = "";

		for (int i = 2; i >= 0; i--) {
			answer = answer + inputString[i];
			if (i != 0) {
				answer = answer + "-";
			}
		}

		return answer;
	}

	/**
	 * this static method checks whether the date input is a date of next year.
	 * Eg: If current day is 20/11/2011 and the entered date is 1/1, then it
	 * should be interpreted as 1/1/2012<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a date-like input.
	 * @return true is the input is a date of next year.
	 */
	public static boolean checkIfDateOfNextYear(String input) {
		assert input.matches("[0-9]{1,2}-[0-9]{1,2}");

		boolean isNextYear;

		Calendar c = Calendar.getInstance();
		int currentDate = c.get(Calendar.DATE);
		int currentMonth = c.get(Calendar.MONTH) + 1;

		String[] temp = input.split("-");
		if (Integer.parseInt(temp[1]) > currentMonth) {
			isNextYear = false;
		} else if (Integer.parseInt(temp[1]) < currentMonth) {
			isNextYear = true;
		} else {
			if (Integer.parseInt(temp[0]) >= currentDate) {
				isNextYear = false;
			} else {
				isNextYear = true;
			}
		}

		return isNextYear;
	}

	/**
	 * this static method will get the full date of the input in cases the input
	 * contains only date and month.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a date-like input.
	 * @return full-date-format which the input suppose to mean.
	 */
	public static String getFullDate(String input, boolean isNextYear) {
		assert input.matches("[0-9]{1,2}-[0-9]{1,2}");

		String answer = input;
		Calendar c = Calendar.getInstance();

		if (isNextYear) {
			answer = answer + "-" + (c.get(Calendar.YEAR) + 1);
		} else {
			answer = answer + "-" + c.get(Calendar.YEAR);
		}

		return answer;
	}

	/**
	 * this static method gets the date of week of the input.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing the date of the week.
	 * @return an integer representing the date of the week.
	 */
	public static int getDateOfWeek(String input) {
		input = input.toLowerCase();

		if (input.startsWith("sun")) {
			return 8;
		} else if (input.startsWith("mon")) {
			return 2;
		} else if (input.startsWith("tue")) {
			return 3;
		} else if (input.startsWith("wed")) {
			return 4;
		} else if (input.startsWith("thu")) {
			return 5;
		} else if (input.startsWith("fri")) {
			return 6;
		} else if (input.startsWith("sat")) {
			return 7;
		}

		return 0;
	}

	/**
	 * this static method checks whether the specified year is a leap year.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param year
	 *            the year needed to be checked.
	 * @return true if the specified year is a leap year.
	 */
	public static boolean checkLeapYear(int year) {
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
			return true;
		return false;
	}

	/**
	 * this static method fills the two first digits of the year. Eg: "12" will
	 * become "2012".<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 23 October 2011
	 * @finish 23 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            the String needed to be filled.
	 * @return a string containing the filled year.
	 */
	public static String fillYearSpecial(String input) {
		assert input.matches("[0-9]{1,2}-[0-9]{1,2}-[0-9]{2}");

		char[] charArray = input.toCharArray();
		char[] tempArray = new char[charArray.length + 2];
		for (int i = 0; i < charArray.length; i++) {
			tempArray[i] = charArray[i];
		}
		tempArray[tempArray.length - 1] = tempArray[tempArray.length - 3];
		tempArray[tempArray.length - 2] = tempArray[tempArray.length - 4];
		tempArray[tempArray.length - 3] = '0';
		tempArray[tempArray.length - 4] = '2';
		String answer = new String(tempArray);

		return answer;
	}

	/**
	 * this static method checks whether the input ends with ordinal characters.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 23 October 2011
	 * @finish 23 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            the String needed to be checked.
	 * @return true if the string ends with ordinal characters.
	 */
	public static boolean isDateFormat(String input) {
		input = input.toLowerCase();

		if (input.indexOf("st") > -1) {
			return true;
		} else if (input.indexOf("nd") > -1) {
			return true;
		} else if (input.indexOf("rd") > -1) {
			return true;
		} else if (input.indexOf("th") > -1) {
			return true;
		}

		return false;
	}

	/**
	 * this static method removes the ordinal prefix of the date of the month.
	 * Eg: "13th" will become "13".<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 23 October 2011
	 * @finish 23 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            the string having the date needed to be modified.
	 * @return the string after modifying.
	 */
	public static String removeOrdinalNumber(String input) {
		input = input.toLowerCase();

		assert input.contains("st") || input.contains("nd")
				|| input.contains("rd") || input.contains("th");

		int index = -1;
		if (input.indexOf("st") > -1) {
			index = input.indexOf("st");
		} else if (input.indexOf("nd") > -1) {
			index = input.indexOf("nd");
		} else if (input.indexOf("rd") > -1) {
			index = input.indexOf("rd");
		} else if (input.indexOf("th") > -1) {
			index = input.indexOf("th");
		}

		char[] charArray = input.toCharArray();
		char[] tempArray = new char[charArray.length - 2];
		for (int i = 0; i < index; i++) {
			tempArray[i] = charArray[i];
		}
		for (int i = index + 2; i < charArray.length; i++) {
			tempArray[i - 2] = charArray[i];
		}
		String answer = new String(tempArray);

		return answer;
	}

	/**
	 * this static method gets the actual date of the input in cases the date is
	 * not in the format of the normal calendar. Eg: 32/12/2011<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param day
	 *            the day input.
	 * @param month
	 *            the month input.
	 * @param year
	 *            the year input.
	 * @return a string representing the actual date.
	 */
	public static String getDate(int day, int month, int year) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
			if (day > 31) {
				day -= 31;
				month += 1;
			}
			break;
		case 2:
			boolean isLeapYear = checkLeapYear(year);
			if (isLeapYear) {
				if (day > 29) {
					day -= 29;
					month += 1;
				}
			} else {
				if (day > 28) {
					day -= 28;
					month += 1;
				}
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if (day > 30) {
				day -= 30;
				month += 1;
			}
			break;
		case 12:
			if (day > 31) {
				day -= 31;
				month = 1;
				year += 1;
			}
			break;
		}
		String date = year + "-" + month + "-" + day;

		return date;
	}

	/**
	 * this static method parses the formal time input.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a time-like input.
	 * @return time which the input supposes to mean.
	 */
	public static Time parseFormalTimeInput(String input) {
		Time time = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		java.util.Date sdfTime;

		try {
			if (input.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}")) {
				sdf = new SimpleDateFormat("HH:mm:ss");
			} else if (input.matches("[0-9]{1,2}:[0-9]{1,2}")) {
				sdf = new SimpleDateFormat("HH:mm");
			} else if (input.matches("[0-9]{1,2}")) {
				sdf = new SimpleDateFormat("HH");
			}
			sdf.setLenient(false);
			sdfTime = sdf.parse(input);
			time = new Time(sdfTime.getTime());
		} catch (ParseException e) {
			time = null;
		}

		return time;
	}

	/**
	 * this static method parses the informal time input.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param input
	 *            string containing a time-like input.
	 * @return time which the input supposes to mean.
	 */
	public static Time parseInformalTimeInput(String input) {
		Time time = null;
		input = input.toLowerCase();

		String[] wordArray = input.split(" ");

		// in the format 3 pm, 8 AM,.....
		if (wordArray.length == 2) {
			String mainTime = wordArray[0];
			if (wordArray[1].equals("hr") || wordArray[1].equals("hrs")
					|| wordArray[1].equals("am") || wordArray[1].equals("pm")
					|| wordArray[1].equals("h") || wordArray[1].equals("noon")) {
				time = parseFormalTimeInput(mainTime);

				if ((wordArray[1].compareTo("pm") == 0) && (time != null)) {
					time = getTimePM(time);
				}
			}
		}
		// in the format 3pm , 8AM,....
		else if (wordArray.length == 1) {
			boolean isPM = false;
			char[] charArray = input.toCharArray();
			input = "";
			if (wordArray[0].endsWith("pm")) {
				isPM = true;
				for (int i = 0; i < charArray.length - 2; i++) {
					input = input + charArray[i];
				}
			} else if (wordArray[0].endsWith("am")
					|| wordArray[0].endsWith("hr")) {
				for (int i = 0; i < charArray.length - 2; i++) {
					input = input + charArray[i];
				}
			} else if (wordArray[0].endsWith("h")) {
				for (int i = 0; i < charArray.length - 1; i++) {
					input = input + charArray[i];
				}
			} else if (wordArray[0].endsWith("hrs")) {
				for (int i = 0; i < charArray.length - 3; i++) {
					input = input + charArray[i];
				}
			}

			time = parseFormalTimeInput(input);
			if (isPM && time != null) {
				time = getTimePM(time);
			}
		}
		return time;
	}

	/**
	 * this static method converts time from PM format to normal format.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 11 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @param time
	 *            the time in PM format.
	 * @return formal time format which the input supposed to be meant.
	 */
	public static Time getTimePM(Time time) {
		assert time.toString().matches("[0-9]{2}:[0-9]{2}:[0-9]{2}");

		Calendar c = Calendar.getInstance();
		c.setTime(time);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour != 12) {
			c.set(Calendar.HOUR_OF_DAY, hour + 12);
			time = new Time(c.getTimeInMillis());
		}

		return time;
	}
}