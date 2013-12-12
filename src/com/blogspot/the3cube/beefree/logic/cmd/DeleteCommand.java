package com.blogspot.the3cube.beefree.logic.cmd;

import java.util.Vector;

import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Display;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Logger;
import com.blogspot.the3cube.beefree.util.Task;

/**
 * Handle and execute the delete command.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 *          <p>
 * 
 *          <pre>
 * command syntax:<br>
 * delete taskID<br><br>
 * </pre>
 * 
 *          example: <br>
 *          delete 1;2<br>
 *          this would delete 2 tasks, with the taskId 1 and 2<br>
 *          <br>
 * 
 */
public class DeleteCommand implements Command {
	private Storage storage_;
	private final static String CLASSNAME = "DeleteCommand";

	private Vector<Task> tasksArray_;// the vector of all tasks
	private int[] deleteTaskIDs_;// the IDs of the tasks needed to be deleted
	// the vector of the tasks needed to be deleted
	private Vector<Task> deleteTasks_ = new Vector<Task>();
	private boolean isCorrectCommand_ = true;
	private String returnMessage_ = "";

	public DeleteCommand() {
		// do nothing
	}

	/**
	 * this method will get the delete tasks the user want to delete.<br>
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
	public void getDeleteTasks() {
		assert deleteTaskIDs_.length != 0;

		boolean taskExist;
		String invalidTaskIDs = "";
		int count = 0;

		for (int i = 0; i < deleteTaskIDs_.length; i++) {
			taskExist = false;
			for (int j = 0; j < tasksArray_.size(); j++) {
				if (deleteTaskIDs_[i] == tasksArray_.get(j).getTaskId_()) {
					deleteTasks_.add(tasksArray_.get(j));
					taskExist = true;
				}
			}
			if (!taskExist) {
				invalidTaskIDs = invalidTaskIDs + deleteTaskIDs_[i] + ";";
				count++;
			}
		}

		Logger.getInstance().debug(CLASSNAME, "getDeleteTasks",
				"invalidTaskIDs: " + invalidTaskIDs);

		if (invalidTaskIDs.length() >= 1) {
			invalidTaskIDs = invalidTaskIDs.substring(0,
					invalidTaskIDs.length() - 1);
		}
		if (count == 1) {
			returnMessage_ = returnMessage_ + invalidTaskIDs
					+ " is an invalid task ID.\n";
		} else if (count >= 2) {
			returnMessage_ = returnMessage_ + invalidTaskIDs
					+ " are invalid task IDs.\n";
		}
	}

	/**
	 * this method will remove the duplicate tasks in the deleteTasks_ vector.<br>
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
		for (int i = 0; i < deleteTasks_.size(); i++) {
			for (int j = i + 1; j < deleteTasks_.size(); j++) {
				if (deleteTasks_.get(i).getTaskId_() == deleteTasks_.get(j)
						.getTaskId_()) {
					deleteTasks_.remove(j);
					j--;
				}
			}
		}
	}

	/**
	 * this method will delete the tasks the user want to delete.<br>
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
	public void deleteTasks() {
		boolean isSuccessful = false;

		if (deleteTasks_.size() == 0) {
			returnMessage_ = returnMessage_ + "There is no task to delete.";
			return;
		}
		for (int i = 0; i < deleteTasks_.size(); i++) {
			isSuccessful = storage_.deleteTask(deleteTasks_.get(i));
			if (!isSuccessful) {
				break;
			}
		}

		if (isSuccessful) {
			returnMessage_ = returnMessage_ + "Your tasks have been deleted.";
		} else {
			returnMessage_ = returnMessage_
					+ "Encountered error when deleting your tasks.";
		}
	}

	/**
	 * this method will add back the deleted tasks to storage_.<br>
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
	public boolean addBackDeletedTasks() {
		boolean isSuccessful = false;

		if (deleteTasks_.size() == 0) {
			isSuccessful = true;
		}

		for (int i = 0; i < deleteTasks_.size(); i++) {
			isSuccessful = storage_.addTask(deleteTasks_.get(i));
			if (!isSuccessful) {
				break;
			}
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
		tasksArray_ = storage_.getTasks_();

		DisplayCLI MESSAGE = new DisplayCLI();

		if (isCorrectCommand_) {
			getDeleteTasks();
			removeDuplicateTasks();
			deleteTasks();
		}

		MESSAGE.setData_(returnMessage_);
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
		String noTaskMessage = new String("There is no task to add back.");

		DisplayCLI MESSAGE = new DisplayCLI();
		MESSAGE.setData_(unsuccessfulMessage);

		boolean isSuccessful;

		if (isCorrectCommand_) {
			isSuccessful = addBackDeletedTasks();

			if (isSuccessful) {
				if (deleteTasks_.size() == 0) {
					MESSAGE.setData_(noTaskMessage);
				} else {
					MESSAGE.setData_(successfulMessage);
				}
			}
		}

		return MESSAGE;
	}

	/*
	 * (non-Javadoc)
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
		input = input.trim();

		String[] stringDeleteTaskIDs = input.split(";");
		int size = stringDeleteTaskIDs.length;
		deleteTaskIDs_ = new int[size];
		try {
			for (int i = 0; i < size; i++) {
				deleteTaskIDs_[i] = Integer.parseInt(stringDeleteTaskIDs[i]);
			}
		} catch (NumberFormatException e) {
			returnMessage_ = returnMessage_
					+ "The task ID is not in number format.";
			isCorrectCommand_ = false;
		}

		Logger.getInstance().debug(CLASSNAME, "constructCommand",
				"isCorrectCommand_: " + isCorrectCommand_);
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
