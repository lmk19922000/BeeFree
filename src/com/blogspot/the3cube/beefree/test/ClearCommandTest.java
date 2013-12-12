package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.cmd.ClearCommand;
import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Task;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the ClearCommand functionality.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 */
public class ClearCommandTest {

	private static Storage storage_;

	// clear one label with may tasks under that label
	private final static String CLEARCOMMAND01 = "Home";
	// clear may labels
	private final static String CLEARCOMMAND02 = "School;Job";
	// clear Completed label as default
	private final static String CLEARCOMMAND03 = "";
	// clear one label with one task under that label
	private final static String CLEARCOMMAND04 = "Project";
	// clear label with one task under it and then undo
	private final static String CLEARCOMMAND05 = "Entertainment";
	// clear label with many tasks under it and then undo
	private final static String CLEARCOMMAND06 = "Important";

	private final static String Expected01 = "Your tasks have been cleared.";
	private final static String Expected02 = "Your tasks have been cleared.";
	private final static String Expected03 = "Your tasks have been cleared.";
	private final static String Expected04 = "Your tasks have been cleared.";
	private final static String Expected05 = "Your tasks have been cleared.";
	private final static String Expected06 = "Your tasks have been cleared.";
	private final static String Expected07 = "Your tasks have been added back.";

	private final static String[] COMMANDS = { CLEARCOMMAND01, CLEARCOMMAND02,
			CLEARCOMMAND03, CLEARCOMMAND04, CLEARCOMMAND05, CLEARCOMMAND06 };

	private final static String[] Expected = { Expected01, Expected02,
			Expected03, Expected04, Expected05, Expected06, Expected07 };

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
	}

	/**
	 * this method will perform the testing.<br>
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
	public void test() {
		Task task1 = new Task();
		Task task2 = new Task();
		Task task3 = new Task();
		Task task4 = new Task();
		Task task5 = new Task();
		Task task6 = new Task();
		Task task7 = new Task();
		Task task8 = new Task();
		Task task9 = new Task();
		Task task10 = new Task();

		task1.setTaskName_("A");
		task2.setTaskName_("B");
		task3.setTaskName_("C");
		task4.setTaskName_("D");
		task5.setTaskName_("E");
		task6.setTaskName_("F");
		task7.setTaskName_("G");
		task8.setTaskName_("H");
		task9.setTaskName_("I");
		task10.setTaskName_("J");

		task1.addLabel("Home");
		task2.addLabel("Home");
		task3.addLabel("School");
		task3.addLabel("Work");
		task4.addLabel("Job");
		task5.addLabel("Project");
		task6.addLabel("Work");
		task7.addLabel("Completed");
		task8.addLabel("Entertainment");
		task9.addLabel("Important");
		task10.addLabel("Important");

		storage_.addTask(task1);
		storage_.addTask(task2);
		storage_.addTask(task3);
		storage_.addTask(task4);
		storage_.addTask(task5);
		storage_.addTask(task6);
		storage_.addTask(task7);
		storage_.addTask(task8);
		storage_.addTask(task9);
		storage_.addTask(task10);

		for (int i = 0; i < COMMANDS.length; i++) {
			ClearCommand command = new ClearCommand();
			command.setStorage(storage_);
			command.constructCommand(COMMANDS[i]);
			DisplayCLI returnMessage = (DisplayCLI) command.run();

			boolean isMatched = returnMessage.getData_().equals(Expected[i]);
			if (!isMatched) {
				System.out.println("Output:   " + returnMessage.getData_());
				System.out.println("Expected: " + Expected[i]);
			}
			assertTrue(isMatched);

			// test undo
			if (i == 4 || i == 5) {
				DisplayCLI undoMessage = (DisplayCLI) command.undo();

				boolean isSame = undoMessage.getData_().equals(Expected[6]);
				if (!isSame) {
					System.out.println("Output:   " + undoMessage.getData_());
					System.out.println("Expected: " + Expected[6]);
				}
				assertTrue(isSame);
			}
		}

		Vector<Task> resultTasks = storage_.getTasks_();
		// after clearing, there are only task 6;8;9;10 left
		assertTrue(resultTasks.size() == 4);
	}
}