package com.blogspot.the3cube.beefree.logic.cmd;

import java.sql.Date;
import java.sql.Time;
import java.util.Vector;

import com.blogspot.the3cube.beefree.logic.google.GoogleCal;
import com.blogspot.the3cube.beefree.logic.google.GoogleDocs;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Display;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Logger;
import com.blogspot.the3cube.beefree.util.Task;
import com.blogspot.the3cube.beefree.util.Variable;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.extensions.When;

/**
 * Handle and execute the export command.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.2
 * @version 	v0.2
 * 
 * <p><pre>
 * command syntax:<br>
 * export [options]<br><br>
 * </pre></p>
 * <p><pre>
 * options:<br>
 * to [file | gcal | gdocs]		export to plain text file, google calendar or google docs<br>
 * with username:password			using the supplied for user authentication<br>
 * [file | gdocs] filename 		named the file if need with the supplied name<br><br>
 * </pre></p>
 * <p><pre>
 * example: <br>
 * export to file file1.txt to gcal with user1:pass1<br>
 * this would export all the task to a text file and Google Calendar using:<br>
 * filename:		file1.txt<br>
 * username: 		user1<br>
 * password: 		pass1<br>
 * target calendar: 	Primary Calendar<br><br>
 * 
 * export to file file1.txt<br>
 * this would export all the task to a text file using:<br>
 * filename: 		file1.txt<br><br>
 * 
 * export to gdocs [BeeFree] Tasks with user1:pass1<br>
 * this would export the tasks.csv to Google Doc using:<br>
 * username:		user1<br>
 * password:		pass1<br>
 * target filename: 	[BeeFree] Tasks<br><br>
 * </pre></p>
 */
public class ExportCommand implements Command {

	private final static String CLASSNAME = "ExportCommand";
	private final static String[] KEYWORDS = { "to", "with", "gdocs", "file" };

	private static enum Options {
		AUTHENTICATION, DESTINATION, FILENAME, G_DOCS, NO_MATCHING
	};

	private Storage storage_;

	private boolean exportToFile_ = false;
	private String exportToFilename_ = null;

	private boolean exportToGoogleCal_ = false;

	private boolean exportToGoogleDocs_ = false;
	private String exportToGoogleDocsFilename_ = null;

	private String username_ = null;
	private String password_ = null;

	public ExportCommand() {
		storage_ = new Storage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blogspot.the3cube.beefree.logic.cmd.Command#constructCommand(java
	 * .lang.String)
	 */
	@Override
	public void constructCommand(String input) {
		assert storage_ != null;
		assert input != null;

		Vector<String> parameters = parseNaturalLanguage(input, KEYWORDS);
		for (int i = 0; i < parameters.size(); i++) {
			String parameter = parameters.get(i);
			if (parameter.trim().length() != 0) {
				Options optionsType = checkOptionsType(parameter);
				parseOptions(optionsType, parameter);
			}
		}
	}

	/**
	 * this method will parse the natural language.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				12 October 2011
	 * @finish 				12 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param 				input the input to be parsed.
	 * @param keywords 		the keywords to parse with.
	 * @return 				a vector of string starting with those keywords.
	 */
	public Vector<String> parseNaturalLanguage(String input, String[] keywords) {
		Vector<String> parsed = new Vector<String>();

		String[] words = input.split(Variable.WHITESPACE);

		String set = new String();
		for (int i = 0; i < words.length; i++) {
			boolean isKeyword = false;
			for (int j = 0; j < keywords.length; j++) {
				if (words[i].equalsIgnoreCase(keywords[j])) {
					isKeyword = true;
					break;
				}
			}
			if (isKeyword) {
				set = set.trim();
				if (set.length() > 0) {
					parsed.add(set);
				}
				set = new String();
				set += words[i] + Variable.WHITESPACE;
			} else {
				set += words[i] + Variable.WHITESPACE;
			}
		}
		if (set.trim().length() > 0) {
			parsed.add(set.trim());
		}

		return parsed;
	}

	/**
	 * this method check what options is the input string and return the
	 * corresponding enum.<br>
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
	 * @param input 	the string input to be checked.
	 * @return 			the option type.
	 */
	private Options checkOptionsType(String input) {
		assert input != null;

		Options optionsType = Options.NO_MATCHING;

		if (input.startsWith("to")) {
			optionsType = Options.DESTINATION;
		} else if (input.startsWith("with")) {
			optionsType = Options.AUTHENTICATION;
		} else if (input.startsWith("gdocs")) {
			optionsType = Options.G_DOCS;
		} else if (input.startsWith("file")) {
			optionsType = Options.FILENAME;
		}

		return optionsType;
	}

	/**
	 * this method will parse the options.<br>
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
	 * @param optionsType 	the option type.
	 * @param parameter 	the input from the user.
	 */
	private void parseOptions(Options optionsType, String parameter) {
		switch (optionsType) {
		case AUTHENTICATION:
			int indexOfWhitespace = parameter.indexOf(Variable.WHITESPACE);
			if (indexOfWhitespace > 0) {
				String authentication = parameter.substring(indexOfWhitespace);

				boolean valid = false;
				int indexOfBreak = -1;

				valid = authentication.length() > 2;
				if (valid) {
					indexOfBreak = authentication.indexOf(":");
					valid = indexOfBreak > 0;
				}
				if (valid) {
					valid = indexOfBreak + 1 != authentication.length();
				}

				if (valid) {
					username_ = authentication.substring(0, indexOfBreak);
					password_ = authentication.substring(indexOfBreak + 1);
				}
			}
			break;
		case DESTINATION:
			if (parameter.indexOf("gcal") > 0) {
				exportToGoogleCal_ = true;
			}
			break;
		case FILENAME:
			indexOfWhitespace = parameter.indexOf(Variable.WHITESPACE);
			if (indexOfWhitespace > 0) {
				exportToFile_ = true;
				exportToFilename_ = parameter.substring(indexOfWhitespace);
			}
			break;
		case G_DOCS:
			indexOfWhitespace = parameter.indexOf(Variable.WHITESPACE);
			if (indexOfWhitespace > 0) {
				exportToGoogleDocs_ = true;
				exportToGoogleDocsFilename_ = parameter
						.substring(indexOfWhitespace);
			}
			break;
		case NO_MATCHING:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.logic.cmd.Command#run()
	 */
	@Override
	public Display run() {
		assert storage_ != null;

		DisplayCLI cli = new DisplayCLI();
		cli.setData_("");

		Logger.getInstance().debug(CLASSNAME, "run",
				"exportToFile_: " + exportToFile_);

		if (exportToFile_) {
			String message = runExportToFile();
			cli.setData_(cli.getData_() + "" + message);
		}

		boolean validAuthentication = username_ != null && password_ != null;

		Logger.getInstance().debug(CLASSNAME, "run",
				"validAuthentication: " + validAuthentication);

		Logger.getInstance().debug(CLASSNAME, "run",
				"exportToGoogleCal_: " + exportToGoogleCal_);

		if (exportToGoogleCal_ && validAuthentication) {
			String message = runExportToGoogleCal();
			cli.setData_(cli.getData_() + "\n" + message);
		}

		Logger.getInstance().debug(CLASSNAME, "run",
				"exportToGoogleDocs_: " + exportToGoogleDocs_);

		if (exportToGoogleDocs_ && validAuthentication) {
			String message = runExportToGoogleDocs();
			cli.setData_(cli.getData_() + "\n" + message);
		}

		if (cli.getData_().isEmpty()) {
			cli.setData_("nothing is done.");
		} else {
			cli.setData_(cli.getData_().trim());
		}
		return cli;
	}

	/**
	 * this method will perform the job of exporting to file.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		 11 October 2011
	 * @finish 		11 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @return 		the return message for the user.
	 */
	private String runExportToFile() {
		assert exportToFilename_ != null;

		Logger.getInstance().debug(CLASSNAME, "runExportToFile",
				"export to: " + exportToFilename_);

		boolean isSuccessful = true;
		Vector<Task> tasks = storage_.getTasks_();

		Storage.createFile(exportToFilename_);
		for (int i = 0; i < tasks.size(); i++) {
			isSuccessful &= Storage.printToFile(exportToFilename_, tasks.get(i)
					.getDetailsString());
			isSuccessful &= Storage.printToFile(exportToFilename_, "-");
		}

		String message = new String();
		if (isSuccessful) {
			message = "completed exporting with no errors.";
		} else {
			message = "we are sorry. we didnt manage to export all the tasks.";
		}

		return message;
	}

	/**
	 * this method will do the job of exporting the all task to google calendar.<br>
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
	 * @return the return message for the user.
	 */
	private String runExportToGoogleCal() {
		String message = "";

		GoogleCal gCal = new GoogleCal("BeeFree");

		try {
			gCal.authenticate(username_, password_);
			gCal.deleteAllEntries("[BeeFree]");
		} catch (Exception e) {
			message += "we are sorry. we didnt manage to clear your old updates.";

			Logger.getInstance().debug(CLASSNAME, "runExportToGoogleCal",
					"exception: " + e.getLocalizedMessage());
		}

		Vector<CalendarEventEntry> entries = new Vector<CalendarEventEntry>();
		Vector<Task> tasks = storage_.getTasks_();

		try {
			for (int i = 0; i < tasks.size(); i++) {
				Task t = tasks.get(i);

				CalendarEventEntry entry = createEventFromTask(gCal, t);

				if (entry != null) {
					entries.add(entry);
				}
			}

			gCal.addEntries(entries);
		} catch (Exception e) {
			message += "we are sorry. we didnt manage to upload all the tasks.";

			Logger.getInstance().debug(CLASSNAME, "runExportToGoogleCal",
					"exception: " + e.getMessage());
		}

		if (message.isEmpty()) {
			message = "completed uploading with no errors.";
		}

		return message;
	}
	
	/**
	 * this method create the entry from the task referenced.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				14 October 2011
	 * @finish 				14 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param gCal			the google calendar object.
	 * @param t				the task to create an event from.
	 * @return				the event if valid.
	 * @throws Exception
	 */
	private CalendarEventEntry createEventFromTask(GoogleCal gCal, Task t)
			throws Exception {
		assert gCal != null;
		assert t != null;

		String methodName = "createEventFromTask";
		Logger.getInstance().debug(CLASSNAME, methodName,
				"task: " + t.getTaskName_());

		DateTime start = null;
		Date startFrom = t.getStartFrom_();
		Time startTime = t.getStartTime_();

		DateTime finish = null;
		Date finishBy = t.getFinishBy_();
		Time finishTime = t.getFinishTime_();

		boolean isAllDayEvent = startTime == null || finishTime == null;
		boolean dateAvailable = startFrom != null && finishBy != null;
		boolean timeAvailable = startTime != null && finishTime != null;
		boolean dateTimeAvailable = dateAvailable && timeAvailable;
		boolean isValidTask = true;

		Logger.getInstance().debug(CLASSNAME, methodName,
				"isAllDayEvent: " + isAllDayEvent);
		Logger.getInstance().debug(CLASSNAME, methodName,
				"dateAvailable: " + dateAvailable);
		Logger.getInstance().debug(CLASSNAME, methodName,
				"timeAvailable: " + timeAvailable);
		Logger.getInstance().debug(CLASSNAME, methodName,
				"dateTimeAvailable: " + dateTimeAvailable);

		if (dateTimeAvailable) {
			start = GoogleCal.createDateTime(startFrom.toString(),
					startTime.toString(), Variable.GMT);
			finish = GoogleCal.createDateTime(finishBy.toString(),
					finishTime.toString(), Variable.GMT);
		} else if (isAllDayEvent && dateAvailable) {
			start = GoogleCal.createDate(startFrom.toString(),
					Variable.GMT_DATE_ONLY);
			finish = GoogleCal.createDate(
					GoogleCal.getNextDay(finishBy.toString()),
					Variable.GMT_DATE_ONLY);

			Logger.getInstance()
					.debug(CLASSNAME, methodName, "start: " + start);
			Logger.getInstance().debug(CLASSNAME, methodName,
					"finish: " + finish);
		} else {
			isValidTask = false;
		}

		CalendarEventEntry entry = null;
		if (isValidTask) {
			Logger.getInstance().debug(CLASSNAME, methodName,
					"adding task: " + t.getTaskName_());

			When eventTimes = GoogleCal.createDuration(start, finish);

			entry = GoogleCal.createEvent(gCal.getEntryPrefix_()
					+ Variable.WHITESPACE + t.getTaskName_(), "", eventTimes);
		}

		return entry;
	}

	/**
	 * this method will do the job of exporting the task csv file to google
	 * docs.<br>
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
	 * @return 		the return message for the user.
	 */
	private String runExportToGoogleDocs() {
		String message = "";

		Logger.getInstance().debug(CLASSNAME, "runExportToGoogleDocs",
				"uploading to docs with name: " + exportToGoogleDocs_);

		try {
			GoogleDocs docs = new GoogleDocs("BeeFree");
			docs.authenticate(username_, password_);

			DocumentListFeed allFeed = docs.getListOfDocument();
			docs.printDocuments(allFeed);

			DocumentListFeed feed = docs.getListOfDocument(
					exportToGoogleDocsFilename_, 10);

			DocumentListEntry uploadedEntry = docs.uploadFile(
					Variable.TASKS_FILENAME, exportToGoogleDocsFilename_);
			message = "Document now online @ :"
					+ uploadedEntry.getDocumentLink().getHref();

			Logger.getInstance().debug(
					CLASSNAME,
					"runExportToGoogleDocs",
					"Document now online @ :"
							+ uploadedEntry.getDocumentLink().getHref());

		} catch (Exception e) {
			message = "we are sorry. an error has occur. that's all we know.";
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.logic.cmd.Command#undo()
	 */
	@Override
	public Display undo() {
		DisplayCLI cli = new DisplayCLI("this command could not be undone");
		return cli;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blogspot.the3cube.beefree.logic.cmd.Command#setStorage(com.blogspot
	 * .the3cube.beefree.storage.Storage)
	 */
	@Override
	public void setStorage(Storage storage) {
		storage_ = storage;
	}

}
