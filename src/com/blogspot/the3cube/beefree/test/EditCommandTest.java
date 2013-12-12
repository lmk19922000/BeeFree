package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.DateTimeUtil;
import com.blogspot.the3cube.beefree.logic.cmd.EditCommand;
import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Blockout;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Label;
import com.blogspot.the3cube.beefree.util.Task;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the EditCommand functionality.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 */
public class EditCommandTest {

	private static Storage storage_;

	private final static String BLOCKOUT01 = "1,Recess week,2012-03-01,2012-03-02";

	private final static String LABEL01 = "Completed Jobs";
	private final static String LABEL02 = "Home";
	private final static String LABEL03 = "Work";

	// formal command with one flag
	private final static String EDITCOMMAND01 = "-n Go to library 1";
	// formal command with many flags
	private final static String EDITCOMMAND02 = "-sf 11/2 -fb 13/2 1";
	// informal command with one option
	private final static String EDITCOMMAND03 = "to 2012-11-01 1";
	// informal command with many options
	private final static String EDITCOMMAND04 = "from 13:00:00 until 7pm 1";
	// informal format with non-interpreted part in name
	private final static String EDITCOMMAND05 = "name watch \"The day after tomorrow\" on 2-1 at 7 PM 1";
	// start date and start time use the common key word "from"
	// finish date and finish time use the common key word "until"
	private final static String EDITCOMMAND06 = "from 3/4 8 am to 5/4 10pm 1";
	// informal command with comma
	private final static String EDITCOMMAND07 = "name Play games on 24/10/11, label Entertainment 1";
	private final static String EDITCOMMAND08 = "name Software demo on 31st, dec 7 am 1";
	// command with informal date format
	private final static String EDITCOMMAND09 = "name Prepare homework from tomorrow to next tue 1";
	// command with no change
	private final static String EDITCOMMAND10 = "1";

	// task in the blockout period
	private final static String EDITCOMMAND11 = "name Buy gift on 2st, march 7 am 1";
	// wrong command without label
	private final static String EDITCOMMAND12 = "-n Go to theater -l 1";
	private final static String EDITCOMMAND13 = "name Go to the zoo label 1";
	// wrong command without flag or key word
	private final static String EDITCOMMAND14 = "Play chess label Entertainment 1";
	// formal command with wrong date
	private final static String EDITCOMMAND15 = "-n Revision for exam -sf 08112011 1";
	// command with start date and finish date not logical
	private final static String EDITCOMMAND16 = "-n Pay money -sf 11/2 -fb 9/2 1";
	private final static String EDITCOMMAND17 = "name This is wrong";
	// command with invalid task ID
	private final static String EDITCOMMAND18 = "name This is wrong 22";

	// test undo
	private final static String EDITCOMMAND19 = "NAME do stuff 1";

	private final static String[] COMMANDS = { EDITCOMMAND01, EDITCOMMAND02,
			EDITCOMMAND03, EDITCOMMAND04, EDITCOMMAND05, EDITCOMMAND06,
			EDITCOMMAND07, EDITCOMMAND08, EDITCOMMAND09, EDITCOMMAND10,
			EDITCOMMAND11, EDITCOMMAND12, EDITCOMMAND13, EDITCOMMAND14,
			EDITCOMMAND15, EDITCOMMAND16, EDITCOMMAND17, EDITCOMMAND18,
			EDITCOMMAND19 };

	private final static String CSV01 = "1,Go to library,2012-01-01,2012-12-31,08:00:00,22:00:00,Completed Jobs;Home;Work";
	private final static String CSV02 = "1,Go to library,2012-02-11,2012-02-13,08:00:00,22:00:00,Completed Jobs;Home;Work";
	private final static String CSV03 = "1,Go to library,2012-02-11,2012-11-01,08:00:00,22:00:00,Completed Jobs;Home;Work";
	private final static String CSV04 = "1,Go to library,2012-02-11,2012-11-01,13:00:00,19:00:00,Completed Jobs;Home;Work";
	private final static String CSV05 = "1,watch The day after tomorrow,2012-01-02,2012-01-02,19:00:00,19:00:00,Completed Jobs;Home;Work";
	private final static String CSV06 = "1,watch The day after tomorrow,2012-04-03,2012-04-05,08:00:00,22:00:00,Completed Jobs;Home;Work";
	private final static String CSV07 = "1,Play games,2011-10-24,2011-10-24,08:00:00,22:00:00,Entertainment";
	private final static String CSV08 = "1,Software demo,2011-12-31,2011-12-31,07:00:00,22:00:00,Entertainment";
	private final static String CSV09 = "1,Prepare homework,"
			+ DateTimeUtil.DateParser("tomorrow") + ","
			+ DateTimeUtil.DateParser("next tue")
			+ ",07:00:00,22:00:00,Entertainment";
	private final static String CSV10 = "1,Prepare homework,"
			+ DateTimeUtil.DateParser("tomorrow") + ","
			+ DateTimeUtil.DateParser("next tue")
			+ ",07:00:00,22:00:00,Entertainment";
	private final static String CSV11 = "Note: Buy gift is in the Recess week blocked period.\nBuy gift has been edited.";
	private final static String CSV12 = "There is no label specified.\nFailed to edit your task.";
	private final static String CSV13 = "There is no label specified.\nFailed to edit your task.";
	private final static String CSV14 = "The first option is incorrect.\nFailed to edit your task.";
	private final static String CSV15 = "The start date is not in correct format or not valid.\nFailed to edit your task.";
	private final static String CSV16 = "The start date is after the finish date.\nFailed to edit your task.";
	private final static String CSV17 = "No task ID at the end of the command.\nFailed to edit your task.";
	private final static String CSV18 = "There is no task with task ID: 22.\nFailed to edit your task.";
	private final static String CSV19 = "Buy gift has been edited.";

	private final static String[] Expected = { CSV01, CSV02, CSV03, CSV04,
			CSV05, CSV06, CSV07, CSV08, CSV09, CSV10, CSV11, CSV12, CSV13,
			CSV14, CSV15, CSV16, CSV17, CSV18, CSV19 };

	/**
	 * this method will perform the required setup.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.2
	 */
	@BeforeClass
	public static void setup() {
		FileSystem.clearTextFile(Variable.TEST_BLOCKOUT_FILENAME);
		FileSystem.clearTextFile(Variable.TEST_TASK_FILENAME);

		storage_ = new Storage(Variable.TEST_TASK_FILENAME,
				Variable.TEST_BLOCKOUT_FILENAME);
		storage_.init();

		storage_.addBlockout(new Blockout(BLOCKOUT01));
	}

	/**
	 * this method will perform the testing.<br>
	 * 
	 * @throws ParseException
	 * 
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.2
	 */
	@Test
	public void test() throws ParseException {
		Task task1 = new Task();
		// Initialise task fields
		task1.setTaskName_("Go to school");

		Vector<Label> labelArray = new Vector<Label>();
		labelArray.add(new Label(LABEL01));
		labelArray.add(new Label(LABEL02));
		labelArray.add(new Label(LABEL03));
		task1.setLabels_(labelArray);

		task1.setStartFrom_(DateTimeUtil.DateParser("2012-01-01"));
		task1.setFinishBy_(DateTimeUtil.DateParser("2012-12-31"));

		task1.setStartTime_(DateTimeUtil.TimeParser("08:00:00"));
		task1.setFinishTime_(DateTimeUtil.TimeParser("22:00:00"));

		storage_.addTask(task1);

		for (int i = 0; i < COMMANDS.length; i++) {
			EditCommand command = new EditCommand();
			command.setStorage(storage_);
			command.constructCommand(COMMANDS[i]);
			DisplayCLI returnMessage = (DisplayCLI) command.run();
			// test content of returned message
			if (i > 9 && i < 18) {
				boolean isMatched = returnMessage.getData_()
						.equals(Expected[i]);
				if (!isMatched) {
					System.out.println("Output:   " + returnMessage.getData_());
					System.out.println("Expected: " + Expected[i]);
				}
				assertTrue(isMatched);
			}
			// test undo
			else if (i == 18) {
				DisplayCLI undoMessage = (DisplayCLI) command.undo();
				boolean isSame = undoMessage.getData_().equals(Expected[18]);
				if (!isSame) {
					System.out.println("Output:   " + undoMessage.getData_());
					System.out.println("Expected: " + Expected[18]);
				}
				assertTrue(isSame);
			}
			// test content of the task edited
			else {
				Vector<Task> result = storage_.getTasks_();
				for (int j = 0; j < result.size(); j++) {
					if (result.get(j).getTaskId_() == 1) {
						Task task = result.get(j);
						boolean matched = task.toCSV().equals(Expected[i]);
						if (!matched) {
							System.out.println("Output:   " + task.toCSV());
							System.out.println("Expected: " + Expected[i]);
						}
						assertTrue(matched);
						break;
					}
				}
			}
		}
	}
}