package com.blogspot.the3cube.beefree.logic;

/**
 * The various command that BeeFree support.<br>
 *
 * @author		The3Cube
 * @since		v0.1
 * @version		v0.1
 * 
 */
public enum CommandType {
	// basic feature
	ADD, DELETE, CLEAR, EDIT, DISPLAY, EXIT,
	// unique feature
	BLOCKOUT, COMPLETE,
	// advance feature
	UNDO, HELP, EXPORT,
	// invalid
	INVALID
}
