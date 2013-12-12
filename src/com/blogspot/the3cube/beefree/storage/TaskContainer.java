package com.blogspot.the3cube.beefree.storage;

import java.util.Vector;

import com.blogspot.the3cube.beefree.ui.Observable;
import com.blogspot.the3cube.beefree.util.Task;

/**
 * A container that could hold all the task.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.1
 * @version 	v0.1
 */
public class TaskContainer extends Observable {

	private String filename_;
	private Vector<Task> tasks_;

	private static int nextTaskId = 1;

	/**
	 * @param filename
	 */
	public TaskContainer(String filename) {
		this.filename_ = filename;

		tasks_ = new Vector<Task>();
	}

	/**
	 * this method will init the container.<br>
	 * the container will create the file is the file does not exist in the
	 * first place.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		17 September 2011
	 * @finish		20 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	20 September 2011
	 * 
	 * @since		v0.1
	 * 
	 * @return 		true if the container is successfully read the text file.
	 */
	public boolean init() {
		boolean initSuccessfully = true;

		boolean exists = FileSystem.checkIfExists(filename_);
		if (exists) {
			Vector<String> read = FileSystem.readFromFile(filename_);
			Task[] tasks = generateTasks(read);
			for (Task t : tasks) {
				tasks_.add(t);
			}
		} else {
			initSuccessfully = FileSystem.createFile(filename_);
		}

		return initSuccessfully;
	}

	/**
	 * this method will generate task from the string array.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		17 September 2011
	 * @finish		20 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	20 September 2011
	 * 
	 * @since		v0.1
	 * 
	 * @param read 	the string read from the csv file.
	 * @return 		an array of task generate.
	 */
	private Task[] generateTasks(Vector<String> read) {
		Task[] tasks = new Task[read.size()];
		for (int i = 0; i < read.size(); i++) {
			tasks[i] = new Task(read.get(i));

			int taskId = tasks[i].getTaskId_();
			incrementNextTaskId(taskId);
		}
		return tasks;
	}

	/**
	 * this method add a single task into the container.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		17 September 2011
	 * @finish		20 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	20 September 2011
	 * 
	 * @since		v0.1
	 * 
	 * @param task 	the task to be added to the container.
	 * @return 		true if the task is added successfully.
	 */
	public boolean addTask(Task task) {
		boolean appendSuccessfully = false;
		
		if (task != null) {
			if (task.getTaskId_() < 1) {
				task.setTaskId_(nextTaskId);
			}
			
			String csv = task.toCSV();
			appendSuccessfully = FileSystem.appendToTextFile(filename_, csv);

			if (appendSuccessfully) {
				tasks_.add(task);
				incrementNextTaskId(task.getTaskId_());
				updateObserver();
			}
		}
		return appendSuccessfully;
	}

	/**
	 * this method will update the nextTaskId to ensure it is the next available
	 * id.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		17 September 2011
	 * @finish		20 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	20 September 2011
	 * 
	 * @since		v0.1
	 * 
	 * @param taskId 	the current task id.
	 */
	private static void incrementNextTaskId(int taskId) {
		if (taskId >= nextTaskId) {
			nextTaskId = taskId + 1;
		}
	}

	/**
	 * this method allow the task to be deleted.<br>
	 * if the task is not found, the task will not be deleted.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		17 September 2011
	 * @finish		20 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	20 September 2011
	 * 
	 * @since		v0.1
	 * 
	 * @param toBeDeleted 	the task to be deleted.
	 * @return 				true is the task is deleted successfully.
	 */
	public boolean deleteTask(Task toBeDeleted) {
		boolean taskDeleted = false;

		for (int i = 0; i < tasks_.size(); i++) {
			Task task = tasks_.get(i);
			if (task.equals(toBeDeleted)) {
				tasks_.remove(i);
				taskDeleted = true;
			}
		}

		if (taskDeleted) {
			boolean saved = saveToFile();
			taskDeleted = taskDeleted && saved;
		}
		return taskDeleted;
	}

	/**
	 * this method will save the data change into the text file.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		17 September 2011
	 * @finish		20 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	20 September 2011
	 * 
	 * @since		v0.1
	 * 
	 * @return 		true if all the data is save successfully.
	 */
	public boolean saveToFile() {
		boolean savedSuccessfully = true;

		boolean clearSuccessfully = FileSystem.clearTextFile(filename_);

		if (clearSuccessfully) {
			for (int i = 0; i < tasks_.size(); i++) {
				Task task = tasks_.get(i);
				String csv = task.toCSV();
				boolean appendSuccessfully = FileSystem.appendToTextFile(
						filename_, csv);

				savedSuccessfully = savedSuccessfully
						&& appendSuccessfully;
			}
		} else {
			savedSuccessfully = false;
		}

		updateObserver();
		
		return savedSuccessfully;
	}

	/**
	 * this method allow the program to hot swap a task.<br>
	 * parameter task is the new task to be swap in and return old task.<br>
	 * if the task is not found in the container, the task will be added.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start				17 September 2011
	 * @finish				20 September 2011
	 * 
	 * @reviewer			The3Cube
	 * @reviewdate			20 September 2011
	 * 
	 * @since				v0.1
	 * 
	 * @param editedTask 	the task with field edited.
	 * @return 				the old task that has been swapped out.
	 */
	public Task editTask(Task editedTask) {
		Task oldTask = null;
		boolean editSuccessfully = false;

		for (int i = 0; i < tasks_.size(); i++) {
			Task task = tasks_.get(i);
			if (editedTask.equals(task)) {
				oldTask = tasks_.remove(i);
				tasks_.add(i, editedTask);
				editSuccessfully = saveToFile();
				break;
			}
		}

		if (!editSuccessfully) {
			oldTask = null;
		}

		return oldTask;
	}
	
	/**
	 * find a task with a given task id.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			05 October 2011
	 * @finish			05 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param taskId	the task id to find.
	 * @return			the task if found else null.
	 */
	public Task findTask(int taskId) {
		Task task = null;
		
		for (int i = 0; i < tasks_.size(); i++) {
			Task t = tasks_.get(i);
			if (t.getTaskId_() == taskId) {
				task = t;
				break;
			}
		}
		
		return task;
	}
	
	/**
	 * this method return all task with the matched label name.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			28 September 2011
	 * @finish			28 September 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param labelName	the label name to find.
	 * @return			a vector of matched tasks.
	 */
	public Vector<Task> findLabels(String labelName) {
		Vector<Task> matched = new Vector<Task>();
		
		for (int i = 0; i < tasks_.size(); i++) {
			Task t = tasks_.get(i);
			if (t.containsLabel(labelName)) {
				matched.add(t);
			}
		}
		
		return matched;
	}
	
	/**
	 * this method clone the vector task for sorting purpose.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			23 October 2011
	 * @finish			23 October 2011
	 * 
	 * @reviewer			
	 * @reviewdate			
	 * 
	 * @since			v0.1
	 * 
	 * @return			a new vector a new tasks
	 */
	public Vector<Task> cloneTasks() {
		assert tasks_ != null;
		
		Vector<Task> clonedTasks = new Vector<Task>();
		assert clonedTasks != null;
		
		for (Task t : tasks_) {
			clonedTasks.add(t.clone());
		}
		
		return clonedTasks;
	}

	/**
	 * @return the tasks_.
	 */
	public Vector<Task> getTasks_() {
		return tasks_;
	}

	/**
	 * @param tasks_
	 *            the tasks_ to set.
	 */
	public void setTasks_(Vector<Task> tasks_) {
		this.tasks_ = tasks_;
	}

	/**
	 * @return the nextTaskId.
	 */
	public static int getNextTaskId() {
		return nextTaskId;
	}
	/**
	 * @param nextTaskId
	 *            the nextTaskId to set.
	 */
	public static void setNextTaskId(int nextTaskId) {
		TaskContainer.nextTaskId = nextTaskId;
	}

}