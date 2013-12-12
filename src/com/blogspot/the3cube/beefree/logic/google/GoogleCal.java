package com.blogspot.the3cube.beefree.logic.google;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.blogspot.the3cube.beefree.util.Logger;
import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * This class handle the communication aspect with Google Cal.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.2
 * @version 	v0.2
 * 
 */
public class GoogleCal {
	private final static String CLASSNAME = "GoogleCal";
	private final static String URL = "https://www.google.com/calendar/feeds/default/private/full";

	private String appName_ = "";
	private String entryPrefix_ = "[" + appName_ + "]";
	private String username_, password_;

	private CalendarService client_;

	/**
	 * the constructor.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param appName 	the application name accessing Google Cal.
	 */
	public GoogleCal(String appName) {
		this.appName_ = appName;
		this.entryPrefix_ = "[" + appName_ + "]";
	}

	/**
	 * this method will set perform the authentication for the client service.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param username 	the user account which you want to use.
	 * @param password 	the password matching the user account provided.
	 * @throws 			AuthenticationException
	 */
	public void authenticate(String username, String password)
			throws AuthenticationException {
		this.username_ = username;
		this.password_ = password;

		client_ = new CalendarService(appName_);
		client_.setUserCredentials(username_, password_);
	}

	/**
	 * this method will add entries to the primary calendar of the user.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param entries 	the entries to be added to the primary calendar of the user.
	 * @return 			the updated entries.
	 * @throws 			IOException
	 * @throws 			ServiceException
	 */
	public Vector<CalendarEventEntry> addEntries(
			Vector<CalendarEventEntry> entries) throws IOException,
			ServiceException {
		URL postUrl = new URL(URL);
		Vector<CalendarEventEntry> insertedEntries = new Vector<CalendarEventEntry>();
		for (int i = 0; i < entries.size(); i++) {
			CalendarEventEntry insertedEntry = client_.insert(postUrl,
					entries.get(i));
			insertedEntries.add(insertedEntry);
		}
		return insertedEntries;
	}

	/**
	 * this method will delete all entries matching the string provided.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param query 	the entries matching this query string will be deleted.
	 * @throws 			IOException
	 * @throws 			ServiceException
	 */
	public void deleteAllEntries(String query) throws IOException,
			ServiceException {
		URL feedUrl = new URL(URL);
		Query myQuery = new Query(feedUrl);
		myQuery.setFullTextQuery(query);
		CalendarEventFeed myResultsFeed = client_.query(myQuery,
				CalendarEventFeed.class);
		if (myResultsFeed.getEntries().size() > 0) {
			for (int i = 0; i < myResultsFeed.getEntries().size(); i++) {
				CalendarEventEntry matchEntry = (CalendarEventEntry) myResultsFeed
						.getEntries().get(i);
				String entryTitle = matchEntry.getTitle().getPlainText();

				Logger.getInstance().debug(CLASSNAME, "deleteAllEntries",
						"deleting entries: " + entryTitle);

				matchEntry.delete();
			}
		}
	}

	/**
	 * this method will create the DateTime object for a calendar entry.<br>
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
	 * @param date 	the date in YYYY-MM-DD format.
	 * @param time 	the time in HH:MM:SS format.
	 * @param gmt 	the timezone in +HH:MM format.
	 * @return 		the created DateTime object.
	 */
	public static DateTime createDateTime(String date, String time, String gmt) {
		String rfcString = date + "T" + time + gmt;
		return DateTime.parseDateTime(rfcString);
	}

	/**
	 * this method will create the DateTime object for a calendar entry.<br>
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
	 * @param date 	the date in YYYY-MM-DD format.
	 * @param gmt 	the timezone in +HH:MM format.
	 * @return 		the created DateTime object.
	 */
	public static DateTime createDate(String date, String gmt) {
		String rfcString = date + "T00:00:00" + "+00:00";
		DateTime datetime = DateTime.parseDateTime(rfcString);
		datetime.setDateOnly(true);
		return datetime;
	}

	/**
	 * this method will create the When object for the calendar entry.<br>
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
	 * @param from 	the DateTime object of the starting date.
	 * @param to 	the DateTime object of the ending date.
	 * @return 		the created When object with the parameters.
	 */
	public static When createDuration(DateTime from, DateTime to) {

		When eventTimes = new When();
		eventTimes.setStartTime(from);
		eventTimes.setEndTime(to);

		return eventTimes;
	}

	/**
	 * this method will create the calendar entry.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param name 			the name of the entry.
	 * @param description 	the description of the entry.
	 * @param duration 		the duration of the entry.
	 * @return 				the create CalendarEventEntry.
	 */
	public static CalendarEventEntry createEvent(String name,
			String description, When duration) {
		CalendarEventEntry entry = new CalendarEventEntry();
		entry.setTitle(new PlainTextConstruct(name));
		entry.setContent(new PlainTextConstruct(description));

		if (duration == null) {
			entry = null;
		} else {
			entry.addTime(duration);
		}
		return entry;
	}

	/**
	 * Get the next day given a day string of the format <yyyy>-<mm>-<dd>.<br>
	 * adopted from:
	 * http://svn.apache.org/repos/asf/db/derby/code/trunk/java/demo
	 * /localcal/src/GCalendar.java<br>
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
	 * @param day 	the day which you want to find the next day.
	 * @return 		the date of the next day.
	 * @throws 		Exception
	 */
	public static String getNextDay(String day) throws Exception {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		Date date = sdf.parse(day);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return sdf.format(cal.getTime());
	}

	/**
	 * @return the appName_.
	 */
	public String getAppName_() {
		return appName_;
	}

	/**
	 * @return the entryPrefix_.
	 */
	public String getEntryPrefix_() {
		return entryPrefix_;
	}

	/**
	 * @param appName_
	 *            the appName_ to set.
	 */
	public void setAppName_(String appName_) {
		this.appName_ = appName_;
	}

	/**
	 * @param entryPrefix_
	 *            the entryPrefix_ to set.
	 */
	public void setEntryPrefix_(String entryPrefix_) {
		this.entryPrefix_ = entryPrefix_;
	}

}
