package com.blogspot.the3cube.beefree.logic.cmd;

import java.util.Vector;

import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Display;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Label;
import com.blogspot.the3cube.beefree.util.Task;

/**
 * Handle and execute the clear command.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 *          <p>
 * 
 *          <pre>
 * command syntax:<br>
 * clear labelNames<br><br>
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 * example: <br>
 * clear<br>
 * this would clear all task with the label Completed<br><br>
 * 
 * clear CS2103T<br>
 * this would clear all task with the label CS2103T<br><br> 
 * 
 * clear CS2103T;CS2106<br>
 * this would clear all task with the label CS2103T or CS2106<br>
 * </pre>
 * 
 *          </p>
 */
public class ClearCommand implements Command {
	private Storage storage_;

	private Vector<Task> taskArray_;// the vector of all tasks
	// the vector of the tasks needed to be cleared
	private Vector<Task> clearTasks_ = new Vector<Task>();
	// the vector of the labels needed to be cleared
	private Vector<String> clearLabels_ = new Vector<String>();

	public ClearCommand() {
		// do nothing
	}

	/**
	 * this method will get the tasks needed to be cleared.<br>
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
	public void getClearTasks() {
		Vector<Label> labelsOfTask = new Vector<Label>();

		for (int i = 0; i < taskArray_.size(); i++) {
			labelsOfTask = taskArray_.get(i).getLabels_();
			for (int j = 0; j < labelsOfTask.size(); j++) {
				for (int k = 0; k < clearLabels_.size(); k++) {
					if ((clearLabels_.get(k).compareToIgnoreCase(
							labelsOfTask.get(j).toString()) == 0)) {
						clearTasks_.add(taskArray_.get(i));
					}
				}
			}
		}
	}

	/**
	 * this method will remove the duplicate tasks in vector clearTasks_.<br>
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
	public void removeDuplicateTasks() {
		for (int i = 0; i < clearTasks_.size(); i++) {
			for (int j = i + 1; j < clearTasks_.size(); j++) {
				if (clearTasks_.get(j).getTaskId_() == clearTasks_.get(i)
						.getTaskId_()) {
					clearTasks_.remove(j);
					j--;
				}
			}
		}
	}

	/**
	 * this method will delete the tasks.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.2
	 * @return a boolean value indicating whether the delete operations are
	 *         successful.
	 */
	public boolean deleteTasks() {
		boolean isSuccessful = false;

		if (clearTasks_.size() != 0) {
			for (int i = 0; i < clearTasks_.size(); i++) {
				isSuccessful = storage_.deleteTask(clearTasks_.get(i));
				if (!isSuccessful) {
					break;
				}
			}
		} else {
			isSuccessful = true;
		}

		return isSuccessful;
	}

	/**
	 * this method will add back the tasks to undo the clear command.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @return a boolean value indicating whether the add operations are
	 *         successful.
	 */
	public boolean addBackTasks() {
		boolean isSuccessful = false;
		if (clearTasks_.size() != 0) {
			for (int i = 0; i < clearTasks_.size(); i++) {
				isSuccessful = storage_.addTask(clearTasks_.get(i));
				if (!isSuccessful) {
					break;
				}
			}
		} else {
			isSuccessful = true;
		}
		return isSuccessful;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.cmd.Command#run()
	 */
	/**
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.2
	 */
	@Override
	public Display run() {
		assert clearLabels_.size() != 0;

		boolean isSuccessful;
		String successfulMessage = new String("Your tasks have been cleared.");
		String unsuccessfulMessage = new String(
				"Encountered error when clearing your tasks.");
		DisplayCLI MESSAGE = new DisplayCLI();

		taskArray_ = storage_.getTasks_();

		getClearTasks();
		removeDuplicateTasks();
		isSuccessful = deleteTasks();

		if (isSuccessful) {
			MESSAGE.setData_(successfulMessage);
		} else {
			MESSAGE.setData_(unsuccessfulMessage);
		}

		return MESSAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.cmd.Command#undo()
	 */
	/**
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.2
	 */
	@Override
	public Display undo() {
		String successfulMessage = new String(
				"Your tasks have been added back.");
		String unsuccessfulMessage = new String(
				"Encountered error when adding back your tasks.");
		DisplayCLI MESSAGE = new DisplayCLI();
		boolean isSuccessful;

		isSuccessful = addBackTasks();

		if (isSuccessful) {
			MESSAGE.setData_(successfulMessage);
		} else {
			MESSAGE.setData_(unsuccessfulMessage);
		}

		return MESSAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @programmer Le Minh Khue
	 * 
	 * @start 19 September 2011
	 * 
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * 
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.1
	 * 
	 * @see
	 * com.blogspot.the3cube.beefree.cmd.Command#constructCommand(java.lang.
	 * String)
	 */
	/**
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 02 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 03 October 2011
	 * 
	 * @since v0.2
	 */
	@Override
	public void constructCommand(String input) {
		String cmdWithoutSpace = input.trim();

		if (cmdWithoutSpace.equals("")) {
			clearLabels_.add("Completed");
		} else {
			String[] clearLabelArray = cmdWithoutSpace.split(";");
			for (int i = 0; i < clearLabelArray.length; i++) {
				clearLabels_.add(clearLabelArray[i]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blogspot.the3cube.beefree.logic.cmd.Command#setStorage(com.blogspot
	 * .the3cube.beefree.storage.Storage)
	 */
	/**
	 * @programmer Chua Jie Sheng
	 * @start 11 October 2011
	 * @finish 11 October 2011
	 * 
	 * @reviewer
	 * @reviewdate
	 * 
	 * @since v0.2
	 */
	@Override
	public void setStorage(Storage storage) {
		storage_ = storage;
	}
}
