package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.DateTimeUtil;
import com.blogspot.the3cube.beefree.logic.cmd.BlockoutCommand;
import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Blockout;
import com.blogspot.the3cube.beefree.util.Display;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the BlockoutCommand functionality.<br>
 * 
 * @author Chua Jie Sheng
 * @since v0.1
 * @version v0.1
 * 
 */
public class BlockoutCommandTest {

	private static Storage storage_;

	private final static String BLOCKOUTCOMMAND01 = "today on 2011-10-02";
	private final static String BLOCKOUTCOMMAND02 = "holiday from 2010-10-01 to 2011-10-01";
	private final static String BLOCKOUTCOMMAND03 = "formula1 from 2011-11-02 to 2011-11-05";
	private final static String BLOCKOUTCOMMAND04 = "tomorrow on 2011-10-03";
	private final static String BLOCKOUTCOMMAND05 = "delete 1;2;3";
	private final static String BLOCKOUTCOMMAND06 = "..";
	private final static String BLOCKOUTCOMMAND07 = "examination from 14/11 to 20/11";

	private final static String[] COMMANDS = { BLOCKOUTCOMMAND01,
			BLOCKOUTCOMMAND02, BLOCKOUTCOMMAND03, BLOCKOUTCOMMAND04, BLOCKOUTCOMMAND07 };

	private final static String[] COMMANDS2 = { BLOCKOUTCOMMAND05,
			BLOCKOUTCOMMAND06 };

	private final static String BLOCKOUT_RESPONSE_01 = "today blockout is added.";
	private final static String BLOCKOUT_RESPONSE_02 = "holiday blockout is added.";
	private final static String BLOCKOUT_RESPONSE_03 = "formula1 blockout is added.";
	private final static String BLOCKOUT_RESPONSE_04 = "tomorrow blockout is added.";
	private final static String BLOCKOUT_RESPONSE_05 = "Blockout by the name of, today have been deleted."
			+ "\n"
			+ "Blockout by the name of, holiday have been deleted."
			+ "\n"
			+ "Blockout by the name of, formula1 have been deleted.";
	private final static String BLOCKOUT_RESPONSE_06 = "The command you entered is invalid. Please refer to \"help blockout\".";
	private final static String BLOCKOUT_RESPONSE_07 = "examination blockout is added.";

	private final static String[] RESPONSE = { BLOCKOUT_RESPONSE_01,
			BLOCKOUT_RESPONSE_02, BLOCKOUT_RESPONSE_03, BLOCKOUT_RESPONSE_04, BLOCKOUT_RESPONSE_07 };
	private final static String[] RESPONSE2 = { BLOCKOUT_RESPONSE_05,
			BLOCKOUT_RESPONSE_06 };

	private final static String BLOCKED_DATE01 = "2011-10-02";
	private final static String BLOCKED_DATE02 = "2010-12-31";
	private final static String BLOCKED_DATE03 = "2010-10-01";
	private final static String BLOCKED_DATE04 = "2011-10-01";
	private final static String BLOCKED_DATE05 = "2011-11-02";
	private final static String BLOCKED_DATE06 = "2011-11-03";
	private final static String BLOCKED_DATE07 = "2011-11-04";
	private final static String BLOCKED_DATE08 = "2011-11-05";
	private final static String BLOCKED_DATE09 = "2011-10-03";

	private final static String[] BLOCKED_DATE = { BLOCKED_DATE01,
			BLOCKED_DATE02, BLOCKED_DATE03, BLOCKED_DATE04, BLOCKED_DATE05,
			BLOCKED_DATE06, BLOCKED_DATE07, BLOCKED_DATE08, BLOCKED_DATE09 };

	private final static String[] BLOCKED_DATE_RESPONSE = { "today", "holiday",
			"holiday", "holiday", "formula1", "formula1", "formula1",
			"formula1", "tomorrow" };

	private final static String NON_BLOCKED_DATE01 = "2011-10-04";
	private final static String NON_BLOCKED_DATE02 = "2010-09-30";

	private final static String[] NON_BLOCKED_DATE = { NON_BLOCKED_DATE01,
			NON_BLOCKED_DATE02 };

	/**
	 * this method will perform the required setup.<br>
	 * 
	 * @programmer Chua Jie Sheng
	 * @start 01 October 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 04 October 2011
	 * 
	 * @since v0.1
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		FileSystem.clearTextFile(Variable.TEST_BLOCKOUT_FILENAME);
		FileSystem.clearTextFile(Variable.TEST_TASK_FILENAME);

		storage_ = new Storage(Variable.TEST_TASK_FILENAME,
				Variable.TEST_BLOCKOUT_FILENAME);
		storage_.init();
	}

	@Test
	public void test() {
		for (int i = 0; i < COMMANDS.length; i++) {

			BlockoutCommand command = new BlockoutCommand();
			command.setStorage(storage_);
			command.constructCommand(COMMANDS[i]);
			Display display = command.run();

			assertNotNull(display);
			if (display instanceof DisplayCLI) {
				DisplayCLI cli = (DisplayCLI) display;
				String data = cli.getData_();
				boolean equal = RESPONSE[i].equals(data);
				System.out.println("response[" + i + "]:" + RESPONSE[i]);
				System.out.println("expected:" + data);
				assertTrue(equal);
				if (!equal) {
					System.out.println(RESPONSE[i]);
					System.out.println(data);
				}
			}
		}

		for (int i = 0; i < BLOCKED_DATE.length; i++) {
			Date date = DateTimeUtil.DateParser(BLOCKED_DATE[i]);
			Blockout response = storage_.checkIfDateBlocked(date);
			assertTrue(response.getBlockoutName_().equals(
					BLOCKED_DATE_RESPONSE[i]));
		}

		for (int i = 0; i < NON_BLOCKED_DATE.length; i++) {
			Date date = DateTimeUtil.DateParser(NON_BLOCKED_DATE[i]);
			Blockout response = storage_.checkIfDateBlocked(date);
			assertNull(response);
		}

		for (int i = 0; i < COMMANDS2.length; i++) {

			BlockoutCommand command = new BlockoutCommand();
			command.setStorage(storage_);
			command.constructCommand(COMMANDS2[i]);
			Display display = command.run();

			assertNotNull(display);
			if (display instanceof DisplayCLI) {
				DisplayCLI cli = (DisplayCLI) display;
				String data = cli.getData_();
				boolean equal = RESPONSE2[i].equals(data);
				System.out.println("response[" + i + "]:" + RESPONSE2[i]);
				System.out.println("expected:" + data);
				assertTrue(equal);
				if (!equal) {
					System.out.println(RESPONSE2[i]);
					System.out.println(data);
				}
			}
		}
	}

}
