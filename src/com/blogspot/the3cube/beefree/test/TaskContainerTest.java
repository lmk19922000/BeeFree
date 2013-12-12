package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.storage.TaskContainer;
import com.blogspot.the3cube.beefree.util.Task;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the TaskContainer functionality.<br>
 * 
 * @author 		Chua Jie Sheng
 * @since 		v0.1
 * @version 	v0.1
 * 
 */
public class TaskContainerTest {

	private TaskContainer tasks_;

	private final static String task01 = "10,thing to do tml,null,null,null,null,1;cs2103t;3,0";
	private final static String task02 = "11,say hello to gerald,,,,,school;3,10";
	private final static String task03 = "12,>.<,,,,,,0";
	private final static String task04 = "13,hello,2011-12-31,2012-01-30,15:59:01,23:59:00,,0";
	private final static String task05 = "14,dots,,2011-12-14,,10:30:00,3,10";
	private final static String task06 = "15,his,,,,,,0";
	private final static String task07 = "16,welcome to firefox,,,,,,0";
	private final static String task08 = "17,code faster,,,,,,0";
	private final static String task09 = "18,please work!,,,,,,0";

	private final static String[] newTasks = { task01, task02, task03, task04,
			task05, task06, task07, task08, task09 };

	/**
	 * this method start the testing.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		17 September 2011
	 * @finish		20 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	20 September 2011
	 * 
	 * @since		v0.1
	 */
	@Test
	public void test() {
		tasks_ = new TaskContainer(Variable.TEST_TASK_FILENAME);

		// clear before adding
		FileSystem.clearTextFile(Variable.TEST_TASK_FILENAME);
		
		// init the file
		tasks_.init();

		// add new task
		for (int i = 0; i < newTasks.length; i++) {
			Task newTask = new Task(newTasks[i]);
			assertTrue(tasks_.addTask(newTask));
			
			int currentTaskId = newTask.getTaskId_();
			int nextTaskId = tasks_.getNextTaskId();
			assertTrue(nextTaskId > currentTaskId);
		}

		// add a null task
		assertFalse(tasks_.addTask(null));
		
		// edit a task
		Task editedTask = new Task(task07);
		editedTask.setTaskName_("Welcome to Chrome!");
		Task oldTask = tasks_.editTask(editedTask);
		assertTrue(oldTask.equals(new Task(task07)));
		
		// delete a task
		Task deleteTask = new Task(task08);
		assertTrue(tasks_.deleteTask(deleteTask));
		
		// delete a deleted task, should return false
		assertFalse(tasks_.deleteTask(deleteTask)); 
		
		// edit a deleted task, should return null
		assertNull(tasks_.editTask(deleteTask));
	}

}
