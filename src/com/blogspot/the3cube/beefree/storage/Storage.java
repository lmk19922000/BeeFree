package com.blogspot.the3cube.beefree.storage;

import java.sql.Date;
import java.util.Vector;

import com.blogspot.the3cube.beefree.ui.Observer;
import com.blogspot.the3cube.beefree.util.Blockout;
import com.blogspot.the3cube.beefree.util.Task;

/**
 * The fascade class for storage component.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.1
 * @version 	v0.1
 */
public class Storage {
	private final static String TASKS_FILENAME = "tasks.csv";
	private final static String BLOCKOUTS_FILENAME = "blockouts.csv";
	
	private TaskContainer tasks_;
	private BlockoutContainer blockouts_;
	
	/**
	 * construct the storage object.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			10 October 2011
	 * @finish 			10 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 */
	public Storage() {
		tasks_ = new TaskContainer(TASKS_FILENAME);
		blockouts_ = new BlockoutContainer(BLOCKOUTS_FILENAME);
	}
	
	/**
	 * construct the storage object by providing their filename.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				10 October 2011
	 * @finish 				10 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param tasksFile		the file which to contains the tasks.
	 * @param blockoutsFile	the file which to contains the blockouts.
	 */
	public Storage(String tasksFile, String blockoutsFile) {
		tasks_ = new TaskContainer(tasksFile);
		blockouts_ = new BlockoutContainer(blockoutsFile);
	}
	
	/**
	 * init method which start up for {@link BlockoutContainer#init()} 
	 * and {@link TaskContainer#init()}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			10 October 2011
	 * @finish 			10 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			BlockoutContainer#init()
	 * @see				TaskContainer#init()
	 * 
	 * @return 			true if both storage is init.
	 */
	public boolean init() {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		if (!tasks_.init()) {
			tasks_ = null;
		}
		if (!blockouts_.init()) {
			blockouts_ = null;
		}
		
		boolean bothInit = tasks_ != null && blockouts_ != null;
		return bothInit;
	}
	
	/**
	 * saveToFile method which for {@link BlockoutContainer#saveToFile()} 
	 * and {@link TaskContainer#saveToFile()}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			05 November 2011
	 * @finish 			05 November 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		05 November 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			BlockoutContainer#saveToFile()
	 * @see				TaskContainer#saveToFile()
	 * 
	 * @return 			true if both storage is saved.
	 */
	public boolean saveToFile() {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		boolean saved = true;
		saved = tasks_.saveToFile() && blockouts_.saveToFile();
		
		return saved;
	}
	
	/**
	 * facade method for {@link BlockoutContainer#addBlockout(Blockout)}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			10 October 2011
	 * @finish 			10 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			BlockoutContainer#addBlockout(Blockout)
	 * 
	 * @param blockout	the blockout to be added.
	 * @return			if the operation is successful.
	 */
	public boolean addBlockout(Blockout blockout) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return blockouts_.addBlockout(blockout);
	}
	
	/**
	 * facade method for {@link BlockoutContainer#deleteBlockout(Blockout)}.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				10 October 2011
	 * @finish 				10 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @see 				BlockoutContainer#deleteBlockout(Blockout)
	 * 
	 * @param toBeDeleted	the blockout to be deleted.
	 * @return				if the operation is successful.
	 */
	public boolean deleteBlockout(Blockout toBeDeleted) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return blockouts_.deleteBlockout(toBeDeleted);
	}
	
	/**
	 * facade method for {@link BlockoutContainer#editBlockout(Blockout)}.<br>
	 * 
	 * @programmer 				Chua Jie Sheng
	 * @start 					10 October 2011
	 * @finish 					10 October 2011
	 * 
	 * @reviewer 				The3Cube
	 * @reviewdate 				25 October 2011
	 * 
	 * @since 					v0.2
	 * 
	 * @see 					BlockoutContainer#editBlockout(Blockout)
	 * 
	 * @param editedBlockout	the edited blockout to be stored.
	 * @return					the old blockout.
	 */
	public Blockout editBlockout(Blockout editedBlockout) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return blockouts_.editBlockout(editedBlockout);
	}
	
	/**
	 * facade method for {@link BlockoutContainer#findBlockout(int)}.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				10 October 2011
	 * @finish 				10 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @see 				BlockoutContainer#findBlockout(int)
	 * 
	 * @param blockoutId	the blockout to be added.
	 * @return				the blockout if found.
	 */
	public Blockout findBlockout(int blockoutId) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return blockouts_.findBlockout(blockoutId);
	}
	
	/**
	 * facade method for {@link BlockoutContainer#checkIfDateBlocked(Date)}.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		10 October 2011
	 * @finish 		10 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @see 		BlockoutContainer#checkIfDateBlocked(Date)
	 * 
	 * @param when	the date to be checked.
	 * @return		the blockout which block the date if any.
	 */
	public Blockout checkIfDateBlocked(Date when) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return blockouts_.checkIfDateBlocked(when);
	}
	
	/**
	 * facade method for {@link BlockoutContainer#addObserver(Observer)}.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		10 October 2011
	 * @finish 		10 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @see 		BlockoutContainer#addObserver(Observer)
	 * 
	 * @param o		the observer to be added.
	 */
	public void addBlockoutContainerObserver(Observer o) {
		blockouts_.addObserver(o);
	}
	
	/**
	 * facade method for {@link BlockoutContainer#updateObserver()}.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		10 October 2011
	 * @finish 		10 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @see 		BlockoutContainer#updateObserver()
	 * 
	 */
	public void notifyBlockoutContainerObserver() {
		blockouts_.updateObserver();
	}
	
	/**
	 * facade method for {@link BlockoutContainer#getBlockouts_()}.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		10 October 2011
	 * @finish 		10 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @see 		BlockoutContainer#getBlockouts_()
	 * 
	 * @return		the vector of blockout from the blockout container.
	 */
	public Vector<Blockout> getBlockouts_() {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return blockouts_.getBlockouts_();
	}
	
	/**
	 * facade method for {@link TaskContainer#addTask(Task)}.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		10 October 2011
	 * @finish 		10 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @see 		TaskContainer#addTask(Task)
	 * 
	 * @param task	the task to be added.
	 * @return		if the operation is completed successfully.
	 */
	public boolean addTask(Task task) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return tasks_.addTask(task);
	}
	
	/**
	 * facade method for {@link TaskContainer#deleteTask(Task)}.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				10 October 2011
	 * @finish 				10 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @see 				TaskContainer#deleteTask(Task)
	 * 
	 * @param toBeDeleted	the task to be deleted.
	 * @return				if the task is deleted successfully.
	 */
	public boolean deleteTask(Task toBeDeleted) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return tasks_.deleteTask(toBeDeleted);
	}
	
	/**
	 * facade method for {@link TaskContainer#editTask(Task)}.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				10 October 2011
	 * @finish 				10 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @see 				TaskContainer#editTask(Task)
	 * 
	 * @param editedTask	the task which is edited.
	 * @return				the old task that is swapped out.
	 */
	public Task editTask(Task editedTask) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return tasks_.editTask(editedTask);
	}
	
	/**
	 * facade method for {@link TaskContainer#findTask(int)}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			10 October 2011
	 * @finish 			10 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			TaskContainer#findTask(int)
	 * 
	 * @param taskId	find the task with matching task id.
	 * @return			the task if found.
	 */
	public Task findTask(int taskId) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return tasks_.findTask(taskId);
	}
	
	/**
	 * facade method for {@link TaskContainer#findLabels(String)}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			10 October 2011
	 * @finish 			10 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			TaskContainer#findLabels(String)
	 * 
	 * @param labelName	the label name to search with.
	 * @return			a vector of task with the label specified.
	 */
	public Vector<Task> findLabels(String labelName) {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return tasks_.findLabels(labelName);
	}
	
	/**
	 * facade method for {@link TaskContainer#getTasks_()}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			10 October 2011
	 * @finish 			10 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			TaskContainer#getTasks_()
	 * 
	 * @return			a vector of task from the taskcontainer.
	 */
	public Vector<Task> getTasks_() {
		assert tasks_ != null;
		assert blockouts_ != null;
		
		return tasks_.getTasks_();
	}
	
	/**
	 * facade method for {@link FileSystem#createFile(String)}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			12 October 2011
	 * @finish 			12 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			FileSystem#createFile(String)
	 * 
	 * @param filename 	the name of the file to create.
	 * @return 			if the file is create.
	 */
	public static boolean createFile(String filename) {
		boolean created = true;
		if (!FileSystem.checkIfExists(filename)) {
			created = FileSystem.createFile(filename);
		}
		return created;
	}
	
	/**
	 * facade method for {@link TaskContainer#addObserver(Observer)}.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		10 October 2011
	 * @finish 		10 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @see 		TaskContainer#addObserver(Observer)
	 * 
	 * @param o		the observer to be added.
	 */
	public void addTaskContainerObserver(Observer o) {
		tasks_.addObserver(o);
	}
	
	/**
	 * facade method for {@link TaskContainer#updateObserver()}.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		10 October 2011
	 * @finish 		10 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @see 		TaskContainer#updateObserver()
	 * 
	 */
	public void notifyTaskContainerObserver() {
		tasks_.updateObserver();
	}
	
	/**
	 * facade method for {@link FileSystem#appendToTextFile(String, String)}.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			12 October 2011
	 * @finish 			12 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @see 			FileSystem#appendToTextFile(String, String)
	 * 
	 * @param text 	the line to be appended.
	 * @return 		if the append is completed successfully.
	 */
	public static boolean printToFile(String filename, String text) {
		return FileSystem.appendToTextFile(filename, text);
	}
}
