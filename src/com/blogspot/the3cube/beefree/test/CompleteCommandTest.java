package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.cmd.AddCommand;
import com.blogspot.the3cube.beefree.logic.cmd.CompleteCommand;
import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Task;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the CompleteCommand functionality.<br>
 * 
 * @author 	Chua Jie Sheng
 * @since 	v0.1
 * @version v0.1
 * 
 */
public class CompleteCommandTest {

	private static Storage storage_;

	private final static String ADDCOMMAND01 = "hello world";
	private final static String ADDCOMMAND02 = "start of a new year -sf 2011-12-31";
	private final static String ADDCOMMAND03 = "finish by 2012 -fb 2012-12-31";
	private final static String ADDCOMMAND04 = "the time should be added -st 23:59";
	private final static String ADDCOMMAND05 = "the time should not appear -ft 00:01";
	private final static String ADDCOMMAND06 = "bee bee";
	private final static String ADDCOMMAND07 = "this should have label -l Home;Work";
	private final static String ADDCOMMAND08 = "overtime! -sf 2010-12-31 -fb 2011-01-01 -st 23:59:00 -ft 00:59:00 -l Work ";
	private final static String ADDCOMMAND09 = "the quick brown fox jump over the lazy dog overtime! -sf 2000-12-31 -fb 2001-01-01 -st 23:59:00 -ft 00:59:00 -l Work";
	private final static String ADDCOMMAND10 = "this is a child of 1 -l Parent";
	private final static String ADDCOMMAND11 = "there is not parent for this >.< -l Home";

	private final static String[] ADDCOMMANDS = { ADDCOMMAND01, ADDCOMMAND02,
			ADDCOMMAND03, ADDCOMMAND04, ADDCOMMAND05, ADDCOMMAND06,
			ADDCOMMAND07, ADDCOMMAND08, ADDCOMMAND09, ADDCOMMAND10,
			ADDCOMMAND11 };

	private final static String COMPLETECOMMAND01 = "";
	private final static String COMPLETECOMMAND02 = "1;2;3";
	private final static String COMPLETECOMMAND03 = "helloworld";
	private final static String COMPLETECOMMAND04 = "123";
	private final static String COMPLETECOMMAND05 = "8";
	private final static String COMPLETECOMMAND06 = "11";
	
	private final static String[] COMPLETECOMMANDS = { COMPLETECOMMAND01, COMPLETECOMMAND02,
		COMPLETECOMMAND03, COMPLETECOMMAND04, COMPLETECOMMAND05, COMPLETECOMMAND06 };
	
	private final static String CSV01 = "1,hello world,,,,,Completed";
	private final static String CSV02 = "2,start of a new year,2011-12-31,,,,Completed";
	private final static String CSV03 = "3,finish by 2012,,2012-12-31,,,Completed";
	private final static String CSV04 = "4,the time should be added,,,23:59:00,,";
	private final static String CSV05 = "5,the time should not appear,,,,00:01:00,";
	private final static String CSV06 = "6,bee bee,,,,,";
	private final static String CSV07 = "7,this should have label,,,,,Home;Work";
	private final static String CSV08 = "8,overtime!,2010-12-31,2011-01-01,23:59:00,00:59:00,Work;Completed";
	private final static String CSV09 = "9,the quick brown fox jump over the lazy dog overtime!,2000-12-31,2001-01-01,23:59:00,00:59:00,Work";
	private final static String CSV10 = "10,this is a child of 1,,,,,Parent";
	private final static String CSV11 = "11,there is not parent for this >.<,,,,,Home;Completed";

	private final static String[] CSV = { CSV01, CSV02, CSV03, CSV04, CSV05,
			CSV06, CSV07, CSV08, CSV09, CSV10, CSV11 };

	private final static String CSV2_01 = "1,hello world,,,,,";
	private final static String CSV2_02 = "2,start of a new year,2011-12-31,,,,";
	private final static String CSV2_03 = "3,finish by 2012,,2012-12-31,,,";
	private final static String CSV2_04 = "4,the time should be added,,,23:59:00,,";
	private final static String CSV2_05 = "5,the time should not appear,,,,00:01:00,";
	private final static String CSV2_06 = "6,bee bee,,,,,";
	private final static String CSV2_07 = "7,this should have label,,,,,Home;Work";
	private final static String CSV2_08 = "8,overtime!,2010-12-31,2011-01-01,23:59:00,00:59:00,Work";
	private final static String CSV2_09 = "9,the quick brown fox jump over the lazy dog overtime!,2000-12-31,2001-01-01,23:59:00,00:59:00,Work";
	private final static String CSV2_10 = "10,this is a child of 1,,,,,Parent";
	private final static String CSV2_11 = "11,there is not parent for this >.<,,,,,Home";

	private final static String[] CSV2 = { CSV2_01, CSV2_02, CSV2_03, CSV2_04, CSV2_05,
			CSV2_06, CSV2_07, CSV2_08, CSV2_09, CSV2_10, CSV2_11 };


	/**
	 * this method will perform the required setup.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		06 October 2011
	 * @finish 		06 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.1
	 */
	@Before
	public void setUp() throws Exception {
		FileSystem.clearTextFile(Variable.TEST_BLOCKOUT_FILENAME);
		FileSystem.clearTextFile(Variable.TEST_TASK_FILENAME);

		storage_ = new Storage(Variable.TEST_TASK_FILENAME,
				Variable.TEST_BLOCKOUT_FILENAME);
		storage_.init();
	}

	/**
	 * this method will perform the required test.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		06 October 2011
	 * @finish 		06 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.1
	 */
	@Test
	public void test() {
		
		for (int i = 0; i < ADDCOMMANDS.length; i++) {
			AddCommand command = new AddCommand();
			command.setStorage(storage_);
			command.constructCommand(ADDCOMMANDS[i]);
			command.run();
		}
		
		for (int i = 0; i < COMPLETECOMMANDS.length; i++) {
			CompleteCommand command = new CompleteCommand();
			command.setStorage(storage_);
			command.constructCommand(COMPLETECOMMANDS[i]);
			command.run();
		}

		Vector<Task> result = storage_.getTasks_();
		for (int i = 0; i < result.size(); i++) {
			Task task = result.get(i);
			boolean matched = task.toCSV().equals(CSV[i]);
			if (!matched) {
				System.out.println(task.toCSV());
				System.out.println(CSV[i]);
			}
			assertTrue(matched);
		}
		
		for (int i = 0; i < COMPLETECOMMANDS.length; i++) {
			CompleteCommand command = new CompleteCommand();
			command.setStorage(storage_);
			command.constructCommand(COMPLETECOMMANDS[i]);
			command.undo();
		}

		result = storage_.getTasks_();
		for (int i = 0; i < result.size(); i++) {
			Task task = result.get(i);
			boolean matched = task.toCSV().equals(CSV2[i]);
			if (!matched) {
				System.out.println(task.toCSV());
				System.out.println(CSV2[i]);
			}
			assertTrue(matched);
		}
	}

}
