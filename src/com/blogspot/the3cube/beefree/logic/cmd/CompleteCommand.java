package com.blogspot.the3cube.beefree.logic.cmd;

import java.util.Vector;

import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Display;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Task;

/**
 * Handle and execute the complete command.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.1
 * @version 	v0.1
 * 
 * <p><pre>
 * command syntax:<br>
 * complete taskId<br><br>
 * </pre></p>
 * <p><pre>
 * example: <br>
 * complete 1<br>
 * this would mark the task with the task id 1 as completed.<br><br>
 * 
 * complete 1;2;3<br>
 * this would mark the task with the task id 1, 2 and 3 as completed.<br><br>
 * </pre></p>
 */
public class CompleteCommand implements Command {

	private Storage storage_;
	
	private Vector<Task> completedTasks_;
	/* (non-Javadoc)
	 * @see com.blogspot.the3cube.beefree.logic.cmd.Command#constructCommand(java.lang.String)
	 */
	/** 
	 * @programmer 	Chua Jie Sheng
	 * @start 		05 October 2011
	 * @finish 		05 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.1
	 */
	@Override
	public void constructCommand(String input) {
		assert input != null;
		
		completedTasks_ = new Vector<Task>();
		
		boolean emptyString = input.length() < 1;
		if (emptyString) {
			completedTasks_ = null;
			return;
		}
		
		String[] parameters = input.split(";");
		for (int i = 0; i < parameters.length; i++) {
			boolean isNumber = parameters[i].matches("[0-9]*");
			if (isNumber) {
				int taskId = Integer.parseInt(parameters[i]);
				Task t = storage_.findTask(taskId);
				if (t != null) {
					completedTasks_.add(t);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.blogspot.the3cube.beefree.logic.cmd.Command#run()
	 */
	/** 
	 * @programmer 	Chua Jie Sheng
	 * @start 		05 October 2011
	 * @finish 		05 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.1
	 */
	@Override
	public Display run() {
		DisplayCLI cli = new DisplayCLI();
		String displayData = "";
		
		if (completedTasks_ == null) {
			displayData = "The command you entered is invalid. " 
							+ "Please refer to \"help complete\".";
		} else {
			for (int i = 0; i < completedTasks_.size(); i++) {
				Task task = completedTasks_.get(i);
				boolean added = task.markAsDone();
				added = added && storage_.saveToFile();
				if (!added) {
					completedTasks_.remove(i);
					displayData += "We unable to mark task by the name of, " 
									+ task.getTaskName_() 
									+ " as completed.";
				} else {
					displayData += "Marked " + task.getTaskName_() + " as completed.";
				}
				displayData += "\n";
			}
		}
		storage_.notifyTaskContainerObserver();
		
		cli.setData_(displayData.trim());
		return cli;
	}

	/* (non-Javadoc)
	 * @see com.blogspot.the3cube.beefree.logic.cmd.Command#undo()
	 */
	/** 
	 * @programmer 	Chua Jie Sheng
	 * @start 		05 October 2011
	 * @finish 		05 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.1
	 */
	@Override
	public Display undo() {
		DisplayCLI cli = new DisplayCLI();
		String displayData = "";
		
		if (completedTasks_ == null) {
			displayData = "We unable to undo this command as no changes in made.";
		} else {
			for (int i = 0; i < completedTasks_.size(); i++) {
				Task task = completedTasks_.get(i);
				boolean deleted = task.deleteLabel(Task.COMPLETED_LABEL);
				deleted = deleted && storage_.saveToFile();
				if (deleted) {
					displayData += "Marked " 
									+ task.getTaskName_() 
									+ " as not completed";
				}
				displayData += "\n";
			}
		}
		storage_.notifyTaskContainerObserver();
		
		cli.setData_(displayData.trim());
		return cli;
	}

	/* (non-Javadoc)
	 * @see com.blogspot.the3cube.beefree.logic.cmd.Command#setStorage(com.blogspot.the3cube.beefree.storage.Storage)
	 */
	/** 
	 * @programmer 	Chua Jie Sheng
	 * @start 		11 October 2011
	 * @finish 		11 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 */
	@Override
	public void setStorage(Storage storage) {
		storage_ = storage;
	}

}
