package com.blogspot.the3cube.beefree.logic;

import java.util.Stack;

import com.blogspot.the3cube.beefree.logic.cmd.AddCommand;
import com.blogspot.the3cube.beefree.logic.cmd.BlockoutCommand;
import com.blogspot.the3cube.beefree.logic.cmd.ClearCommand;
import com.blogspot.the3cube.beefree.logic.cmd.Command;
import com.blogspot.the3cube.beefree.logic.cmd.CompleteCommand;
import com.blogspot.the3cube.beefree.logic.cmd.DeleteCommand;
import com.blogspot.the3cube.beefree.logic.cmd.DisplayCommand;
import com.blogspot.the3cube.beefree.logic.cmd.EditCommand;
import com.blogspot.the3cube.beefree.logic.cmd.ExportCommand;
import com.blogspot.the3cube.beefree.logic.cmd.HelpCommand;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Display;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * Handle the different command from the user.<br>
 * 
 * @author The3Cube
 * @since v0.1
 * @version 	v0.2
 * 
 */
public class CommandHandler {

	private Storage storage_;

	private Stack<Command> undoStack_;

	/**
	 * construct the CommandHandler object.<br>
	 */
	public CommandHandler() {
		storage_ = initStorage();
		
		undoStack_ = new Stack<Command>();
		assert undoStack_ != null;
	}

	/**
	 * bridging method to bridge the two different class.<br>
	 * 
	 * @since v0.1
	 * 
	 * @param input
	 *            the string user keyed in.
	 * @return the display to be display by the user interface.
	 */
	public Display doCmd(String input) {
		assert input != null;
		
		String cmd = input;
		String parameters = "";
		
		if (input.indexOf(Variable.SPACE) > 0) {
			cmd = input.substring(0, input.indexOf(Variable.SPACE));
			parameters = input.substring(input.indexOf(Variable.SPACE));
			parameters = parameters.trim();
		}

		CommandType commandType = checkCmd(cmd.toLowerCase());

		Display display = null;
		if (commandType == CommandType.UNDO) {
			display = undo();
		} else if (commandType != CommandType.INVALID) {
			Command command = parseCmd(commandType);
			command.setStorage(storage_);
			command.constructCommand(parameters);
			display = command.run();
			boolean possibleToUndo = checkIfCommandCouldBeUndo(commandType);
			if (possibleToUndo) {
				undoStack_.push(command);
			}

		} else {
			display = new DisplayCLI("The command you entered is not supported. Please refer to \"help\".");
		}
		return display;
	}

	/**
	 * check if the command can be undo.<br>
	 * 
	 * @param commandType
	 *            the command to be checked.
	 * @return if the command could be undo.
	 */
	private boolean checkIfCommandCouldBeUndo(CommandType commandType) {
		assert commandType != null;
		
		boolean possibleToUndo = true;

		if (commandType == CommandType.DISPLAY) {
			possibleToUndo = false;
		}
		if (commandType == CommandType.EXPORT) {
			possibleToUndo = false;
		}
		if (commandType == CommandType.HELP) {
			possibleToUndo = false;
		}

		return possibleToUndo;
	}

	/**
	 * allow the user interface to check what type of command the user input.<br>
	 * 
	 * @since v0.1
	 * 
	 * @param cmd
	 *            the string user keyed in.
	 * @return what type of command user input.
	 */
	private CommandType checkCmd(String cmd) {
		assert cmd != null;
		
		CommandType commandType = CommandType.INVALID;

		if (cmd.startsWith("a")) {
			commandType = CommandType.ADD;
		} else if (cmd.startsWith("b")) {
			commandType = CommandType.BLOCKOUT;
		} else if (cmd.startsWith("co")) {
			commandType = CommandType.COMPLETE;
		} else if (cmd.startsWith("c")) {
			commandType = CommandType.CLEAR;
		} else if (cmd.startsWith("done")) {
			commandType = CommandType.COMPLETE;
		} else if (cmd.startsWith("di")) {
			commandType = CommandType.DISPLAY;
		} else if (cmd.startsWith("do")) {
			commandType = CommandType.ADD;
		} else if (cmd.startsWith("d")) {
			commandType = CommandType.DELETE;
		} else if (cmd.startsWith("exit")) {
			commandType = CommandType.EXIT;
			System.exit(0);
		} else if (cmd.startsWith("ex")) {
			commandType = CommandType.EXPORT;
		} else if (cmd.startsWith("e")) {
			commandType = CommandType.EDIT;
		} else if (cmd.startsWith("f")) {
			commandType = CommandType.DISPLAY;
		} else if (cmd.startsWith("ls")) {
			commandType = CommandType.DISPLAY;
		} else if (cmd.startsWith("rm")) {
			commandType = CommandType.DELETE;
		} else if (cmd.startsWith("h")) {
			commandType = CommandType.HELP;
		} else if (cmd.startsWith("s")) {
			commandType = CommandType.DISPLAY;
		} else if (cmd.startsWith("u")) {
			commandType = CommandType.UNDO;
		}

		return commandType;
	}

	/**
	 * return to the user interface a Command object that could be executed.<br>
	 * 
	 * @since v0.1
	 * 
	 * @param type
	 *            the type of command return from checkCmd.
	 * @return the command object of the correct type.
	 */
	private Command parseCmd(CommandType type) {
		assert type != null;
		
		Command command = null;

		switch (type) {
		case ADD:
			command = new AddCommand();
			break;
		case DELETE:
			command = new DeleteCommand();
			break;
		case CLEAR:
			command = new ClearCommand();
			break;
		case COMPLETE:
			command = new CompleteCommand();
			break;
		case EDIT:
			command = new EditCommand();
			break;
		case EXPORT:
			command = new ExportCommand();
			break;
		case DISPLAY:
			command = new DisplayCommand();
			break;
		case BLOCKOUT:
			command = new BlockoutCommand();
			break;
		case HELP:
			command = new HelpCommand();
			break;
		}

		return command;
	}

	/**
	 * this method will init the storage container.<br>
	 * 
	 * @since	v0.2
	 * 
	 * @return 	the storage once it is successfully initiated.
	 */
	private Storage initStorage() {
		Storage storage = new Storage();
		if (!storage.init()) {
			storage = null;
		}
		return storage;
	}

	/**
	 * 
	 * this method will pop the command from the undo stack and run the undo
	 * method of that command.<br>
	 * 
	 * @since v0.1
	 * 
	 * @return the display to be display by the user interface.
	 */
	private Display undo() {
		Display display = null;
		if (undoStack_.empty()) {
			display = new DisplayCLI("We are already at the oldest changes. We can't undo anymore.");
		} else {
			Command previousCmd = undoStack_.pop();
			display = previousCmd.undo();
		}
		return display;
	}

	/**
	 * @return the storage_.
	 */
	public Storage getStorage_() {
		return storage_;
	}

	/**
	 * @param storage_ the storage_ to set.
	 */
	public void setStorage_(Storage storage_) {
		if (storage_ != null) {
			this.storage_ = storage_;
		}
	}
}
