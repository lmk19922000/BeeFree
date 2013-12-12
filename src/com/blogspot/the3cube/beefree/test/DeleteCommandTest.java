package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import com.blogspot.the3cube.beefree.logic.cmd.DeleteCommand;
import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Task;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the DeleteCommand functionality.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 */
public class DeleteCommandTest {

	private static Storage storage_;

	private final static String DELCOMMAND01 = "1";// delete one task
	private final static String DELCOMMAND02 = "2;3";// delete many tasks
	// delete one task many times
	private final static String DELCOMMAND03 = "4;4;5";
	// invalid task and valid task
	private final static String DELCOMMAND04 = "-1;8";
	private final static String DELCOMMAND05 = "";// null command
	private final static String DELCOMMAND06 = "11;12";// all tasks are invalid
	// not in number format
	private final static String DELCOMMAND07 = "go to school";
	private final static String DELCOMMAND08 = "6";// delete one task and undo
	// delete many tasks and undo
	private final static String DELCOMMAND09 = "6;7";

	private final static String Expected01 = "Your tasks have been deleted.";
	private final static String Expected02 = "Your tasks have been deleted.";
	private final static String Expected03 = "Your tasks have been deleted.";
	private final static String Expected04 = "-1 is an invalid task ID.\nYour tasks have been deleted.";
	private final static String Expected05 = "The task ID is not in number format.";
	private final static String Expected06 = "11;12 are invalid task IDs.\nThere is no task to delete.";
	private final static String Expected07 = "The task ID is not in number format.";
	private final static String Expected08 = "Your tasks have been deleted.";
	private final static String Expected09 = "Your tasks have been deleted.";
	private final static String Expected10 = "Your tasks have been added back.";
	private final static String Expected11 = "There is no task to add back.";

	private final static String[] COMMANDS = { DELCOMMAND01, DELCOMMAND02,
			DELCOMMAND03, DELCOMMAND04, DELCOMMAND05, DELCOMMAND06,
			DELCOMMAND07, DELCOMMAND08, DELCOMMAND09 };

	private final static String[] Expected = { Expected01, Expected02,
			Expected03, Expected04, Expected05, Expected06, Expected07,
			Expected08, Expected09, Expected10, Expected11 };

	/**
	 * this method will perform the required setup.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 28 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 28 October 2011
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
	 * @finish 28 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 28 October 2011
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

		task1.setTaskName_("A");
		task2.setTaskName_("B");
		task3.setTaskName_("C");
		task4.setTaskName_("D");
		task5.setTaskName_("E");
		task6.setTaskName_("F");
		task7.setTaskName_("G");
		task8.setTaskName_("H");

		storage_.addTask(task1);
		storage_.addTask(task2);
		storage_.addTask(task3);
		storage_.addTask(task4);
		storage_.addTask(task5);
		storage_.addTask(task6);
		storage_.addTask(task7);
		storage_.addTask(task8);

		for (int i = 0; i < COMMANDS.length; i++) {
			DeleteCommand command = new DeleteCommand();
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
			if (i == 7 || i == 8 || i == 5) {
				DisplayCLI undoMessage = (DisplayCLI) command.undo();

				boolean isSame = undoMessage.getData_().equals(Expected[9]);
				if (i == 5) {
					isSame = undoMessage.getData_().equals(Expected[10]);
				}
				if (!isSame) {
					System.out.println("Output:   " + undoMessage.getData_());
					if (i == 7 || i == 8) {
						System.out.println("Expected: " + Expected[9]);
					} else {
						System.out.println("Expected: " + Expected[10]);
					}
				}
				assertTrue(isSame);
			}
		}

		Vector<Task> resultTasks = storage_.getTasks_();
		for (int i = 0; i < resultTasks.size(); i++) {
			Task task = resultTasks.get(i);
			// after deleting, the remaining tasks are tasks with ID 6 and 7
			assertTrue(task.getTaskId_() == 6 || task.getTaskId_() == 7);
		}
	}
}
