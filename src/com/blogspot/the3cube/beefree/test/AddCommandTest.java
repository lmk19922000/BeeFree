package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.DateTimeUtil;
import com.blogspot.the3cube.beefree.logic.cmd.AddCommand;
import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Blockout;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Task;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the AddCommand functionality.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 */
public class AddCommandTest {

	private static Storage storage_;

	private final static String BLOCKOUT01 = "1,Recess week,2012-03-01,2012-03-02";

	// formal command with one flag
	private final static String ADDCOMMAND01 = "watch TV -sf 11/2";
	// formal command with many flags
	private final static String ADDCOMMAND02 = "celebrate new year day -sf Jan 1 -l Entertainment";
	// informal command with one option
	private final static String ADDCOMMAND03 = "go to school on 11/8";
	// informal command with many options
	private final static String ADDCOMMAND04 = "go to the library on 10/10 from 4pm to 9pm";
	// informal format with non-interpreted part in name
	private final static String ADDCOMMAND05 = "watch \"The day after tomorrow\" on 2-1 at 7 PM";
	// informal command with many options
	private final static String ADDCOMMAND06 = "hair cut by 11/10 @ 3pm";
	private final static String ADDCOMMAND07 = "Buy books for next sem from 2nd jan until 7th jan";
	// formal command with many flags
	private final static String ADDCOMMAND08 = "Do homework -sf 2010-12-31 -fb 2011-01-01 -st 23:59:00 -ft 00:59:00";
	private final static String ADDCOMMAND09 = "Finish 2103T project by 28 Oct";
	// start date and start time use the common key word "from"
	// finish date and finish time use the common key word "until"
	private final static String ADDCOMMAND10 = "Prepare report from 11/2 8 am until 13/2 6pm";
	// informal command with comma
	private final static String ADDCOMMAND11 = "Play games on 24/10/11, label Entertainment";
	private final static String ADDCOMMAND12 = "Software demo on 31st, dec 7 am";
	// command with informal date format
	private final static String ADDCOMMAND13 = "Prepare homework from tomorrow to next tue";

	// task in the blockout period
	private final static String ADDCOMMAND14 = "Buy gift on 2st, march 7 am";
	// formal command without task name
	private final static String ADDCOMMAND15 = "-l Job";
	// wrong command without label
	private final static String ADDCOMMAND16 = "Go to theater -l";
	private final static String ADDCOMMAND17 = "Go to the zoo label";
	// formal command with wrong date
	private final static String ADDCOMMAND18 = "Revision for exam -sf 08112011";
	// command with start date and finish date not logical
	private final static String ADDCOMMAND19 = "Pay money -sf 11/2 -fb 9/2";

	// test undo
	private final static String ADDCOMMAND20 = "do stuff";

	private final static String[] COMMANDS = { ADDCOMMAND01, ADDCOMMAND02,
			ADDCOMMAND03, ADDCOMMAND04, ADDCOMMAND05, ADDCOMMAND06,
			ADDCOMMAND07, ADDCOMMAND08, ADDCOMMAND09, ADDCOMMAND10,
			ADDCOMMAND11, ADDCOMMAND12, ADDCOMMAND13, ADDCOMMAND14,
			ADDCOMMAND15, ADDCOMMAND16, ADDCOMMAND17, ADDCOMMAND18,
			ADDCOMMAND19, ADDCOMMAND20 };

	private final static String CSV01 = "1,watch TV,2012-02-11,,,,";
	private final static String CSV02 = "2,celebrate new year day,2012-01-01,,,,Entertainment";
	private final static String CSV03 = "3,go to school,2012-08-11,2012-08-11,,,";
	private final static String CSV04 = "4,go to the library,2012-10-10,2012-10-10,16:00:00,21:00:00,";
	private final static String CSV05 = "5,watch The day after tomorrow,2012-01-02,2012-01-02,19:00:00,,";
	private final static String CSV06 = "6,hair cut,,2012-10-11,,15:00:00,";
	private final static String CSV07 = "7,Buy books for next sem,2012-01-02,2012-01-07,,,";
	private final static String CSV08 = "8,Do homework,2010-12-31,2011-01-01,23:59:00,00:59:00,";
	private final static String CSV09 = "9,Finish 2103T project,,2012-10-28,,,";
	private final static String CSV10 = "10,Prepare report,2012-02-11,2012-02-13,08:00:00,18:00:00,";
	private final static String CSV11 = "11,Play games,2011-10-24,2011-10-24,,,Entertainment";
	private final static String CSV12 = "12,Software demo,2011-12-31,2011-12-31,07:00:00,,";
	private final static String CSV13 = "13,Prepare homework,"
			+ DateTimeUtil.DateParser("tomorrow") + ","
			+ DateTimeUtil.DateParser("next tue") + ",,,";
	private final static String CSV14 = "Note: Buy gift is in the Recess week blocked period.\nBuy gift has been added.";
	private final static String CSV15 = "Task name is empty.\nEncountered error when adding your task.";
	private final static String CSV16 = "There is no label specified.\nEncountered error when adding your task.";
	private final static String CSV17 = "There is no label specified.\nEncountered error when adding your task.";
	private final static String CSV18 = "The start date is not in correct format or not valid.\nEncountered error when adding your task.";
	private final static String CSV19 = "The start date is after the finish date.\nEncountered error when adding your task.";
	private final static String CSV20 = "do stuff has been deleted.";

	private final static String[] Expected = { CSV01, CSV02, CSV03, CSV04,
			CSV05, CSV06, CSV07, CSV08, CSV09, CSV10, CSV11, CSV12, CSV13,
			CSV14, CSV15, CSV16, CSV17, CSV18, CSV19, CSV20 };

	/**
	 * this method will perform the required setup.<br>
	 * 
	 * @programmer Chua Jie Sheng
	 * @start 19 September 2011
	 * @finish 27 September 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 27 September 2011
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
		for (int i = 0; i < COMMANDS.length; i++) {
			AddCommand command = new AddCommand();
			command.setStorage(storage_);
			command.constructCommand(COMMANDS[i]);
			DisplayCLI returnMessage = (DisplayCLI) command.run();
			// test content of returned message
			if (i > 12 && i < 19) {
				boolean isMatched = returnMessage.getData_()
						.equals(Expected[i]);
				if (!isMatched) {
					System.out.println("Output:   " + returnMessage.getData_());
					System.out.println("Expected: " + Expected[i]);
				}
				assertTrue(isMatched);
			}
			// test undo
			if (i == 19) {
				DisplayCLI undoMessage = (DisplayCLI) command.undo();
				boolean isSame = undoMessage.getData_().equals(Expected[19]);
				if (!isSame) {
					System.out.println("Output:   " + undoMessage.getData_());
					System.out.println("Expected: " + Expected[19]);
				}
			}
		}

		// test content of task
		Vector<Task> result = storage_.getTasks_();
		// There are total 14 tasks added
		assertTrue(result.size() == 14);

		for (int i = 0; i < 13; i++) {
			Task task = result.get(i);
			boolean isMatched = task.toCSV().equals(Expected[i]);
			if (!isMatched) {
				System.out.println("Output:   " + task.toCSV());
				System.out.println("Expected: " + Expected[i]);
			}
			assertTrue(isMatched);
		}
	}
}
