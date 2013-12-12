package com.blogspot.the3cube.beefree.logic.cmd;

import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Display;

/**
 * Interface which provide a format for all Command to be based on.<br>
 *
 * @author		The3Cube
 * @since		v0.1
 * @version		v0.1
 * 
 */
public interface Command {

	/**
	 * interface provide a format for all Command to construct with the user input.<br>
	 * the user input will be strip off the Command (e.g. add, edit, delete, ...).<br>
	 * the sequence is: new Command(); setContainer(...); constructCommand;<br>
	 *
	 * @since v0.1
	 *
	 * @param input the user input.
	 */
	void constructCommand(String input);
	
	/**
	 * interface provide a format for all Command to be based on.<br>
	 * 
	 * @since v0.1
	 * 
	 * @return the display for the respective user interface.
	 */
	Display run();
	
	/**
	 * interface provide a format for all Command to be based on.<br>
	 *
	 * @since v0.1
	 *
	 * @return the display for the respective user interface.
	 */
	Display undo();
	
	/**
	 * interface provide a format for all Command to have reference to the storage facade class.<br>
	 *
	 * @since v0.2
	 *
	 * @param storage
	 */
	void setStorage(Storage storage);
}
