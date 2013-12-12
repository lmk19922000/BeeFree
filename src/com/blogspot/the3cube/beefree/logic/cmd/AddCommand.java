package com.blogspot.the3cube.beefree.logic.cmd;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Vector;

import com.blogspot.the3cube.beefree.logic.DateTimeUtil;
import com.blogspot.the3cube.beefree.storage.Storage;
import com.blogspot.the3cube.beefree.util.Blockout;
import com.blogspot.the3cube.beefree.util.Display;
import com.blogspot.the3cube.beefree.util.DisplayCLI;
import com.blogspot.the3cube.beefree.util.Label;
import com.blogspot.the3cube.beefree.util.Logger;
import com.blogspot.the3cube.beefree.util.Task;

/**
 * Handle and execute the add command.<br>
 * 
 * @author Le Minh Khue
 * @since v0.2
 * @version v0.2
 * 
 *          <p>
 * 
 *          <pre>
 * command syntax:<br>
 * add taskName [options]<br><br>
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 * options:<br>
 * -sf date			specify the date which the task will start from<br>
 * -fb date			specify the date which the task need to finish by<br>
 * -st time			specify the time which the task will start from<br>
 * -ft time			specify the time which the task need to finish by<br>
 * -l labelsName		specify the labels which this task would be under, it could have single or multiple labels as "label1;label2;..."<br><br>
 * 
 * Note: The add command only allow the user to add one task at a time<br>
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 * example: <br>
 * add Finish project status report -sf 2011-09-01 -st 00:01 -ft 2011-09-02 -ft 23:59 -l Work;Project<br>
 * this would add a task with the following:<br>
 * task name: 		Finish project status report<br>
 * start from: 		1st Sept 2011, 00:01<br> 
 * finish by: 		2nd Sept 2011, 23:59<br>
 * labels: 			Work, Project<br>
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 * The add command can support some basic natural inputs based on key words:<br>
 * For specifying "one-date" tasks: "tomorrow" / "today" / "this
 * monday".... / "next friday".... / "next next wed".... / "on" +
 * date......<br>
 * For specifying start date: "from" / "start" + date<br>
 * For specifying finish date: "to" / "till" / "until" / "finish' / "by" + date<br>
 * For specifying start time: "from" / "start" / "at" / "@" + time<br>
 * For specifying finish time: "to" / "till" / "until" / "finish" / "by" + time<br>
 * Note: The start date and start time can be combined together with one
 * key word. Eg:
 * "from monday 3pm", "tomorrow 8 AM", .....
 * The same syntax can be  applied for finish date and finish time.<br><br>
 * 
 * If the words or dates after these key words are not correct or cannot
 * be interpreted, they will be interpreted as a part of the
 * name. User are advised to check the information of the task after
 * adding using the natural format<br>
 * For specifying label: "label" / "labels" + new labels
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 * In some cases, the name of the task may contain the above key words
 * and cause  misunderstanding for BeeFree. To avoid this, user can place
 * the name of the task inside " ". Eg:
 * add "watch The day after tomorrow" this Sunday 9pm<br>
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 * example:<br>
 * add Go to school on monday from 3pm to 4pm<br>
 * this would add a task with the following:<br>
 * task name: 		Go to school<br>
 * start from: 		next monday from the date the task is entered, 15:00<br> 
 * finish by: 		next monday from the date the task is entered, 16:00<br>
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 * add Submit homework from monday 8am to friday 10pm, label School<br> 
 * this would add a task with the following:<br>
 * task name: 		Submit homework<br>
 * start from: 		next monday from the date the task is entered, 08:00<br> 
 * finish by: 		next friday from the date the task is entered, 22:00<br>
 * labels: 			School<br>
 * </pre>
 * 
 *          </p>
 *          <p>
 * 
 *          <pre>
 */
public class AddCommand implements Command {
	private final static String CLASSNAME = "AddCommand";

	private static enum Options {
		START_DATE_SPECIAL_1, START_DATE_SPECIAL_2, START_DATE, FINISH_DATE, START_TIME, FINISH_TIME, LABEL
	};

	private Storage storage_;

	private Task newTask_;
	private boolean isCorrectCommand_ = true;
	private String[] informalInputArray_;
	private String[] formalInputArray_;
	private boolean isFormalFormat_ = true;
	private Vector<String> keyWords_ = new Vector<String>();
	private Vector<Integer> indexKeyWords_ = new Vector<Integer>();
	private Blockout blockPeriod_;
	private String returnMessage_ = "";

	public AddCommand() {
		newTask_ = new Task();
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
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate October 2011
	 */
	@Override
	public void constructCommand(String input) {
		input = input.trim();
		String[] cmdWithRedundantSpaces = input.split(" ");

		if (input.equals("")) {
			isCorrectCommand_ = false;
			returnMessage_ += "Task name is empty.\n";
		}
		// remove if there are more than one white space between two words
		input = "";
		for (int i = 0; i < cmdWithRedundantSpaces.length; i++) {
			cmdWithRedundantSpaces[i] = cmdWithRedundantSpaces[i].replace(',',
					' ');
			cmdWithRedundantSpaces[i] = cmdWithRedundantSpaces[i].trim();
			if (cmdWithRedundantSpaces[i].length() > 0) {
				input += cmdWithRedundantSpaces[i] + " ";
			}
		}
		input = input.trim();

		informalInputArray_ = input.split(" ");

		input = " " + input;
		formalInputArray_ = input.split(" -");
		if (formalInputArray_.length == 1) {
			isFormalFormat_ = false;
		}

		Logger.getInstance().debug(CLASSNAME, "constructCommand",
				"isFormalFormat_: " + isFormalFormat_);
	}

	/**
	 * this method will check the various flags of the formal command and set
	 * them respective.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 */
	public void parseFormalInput() {
		formalInputArray_[0] = formalInputArray_[0].trim();
		if (formalInputArray_[0].equals("")) {
			returnMessage_ = returnMessage_ + "Task name is empty.\n";
			isCorrectCommand_ = false;
			return;
		}

		newTask_.setTaskName_(formalInputArray_[0]);

		for (int i = 1; i < formalInputArray_.length; i++) {
			Options optionType = checkFormalOptionsType(formalInputArray_[i]);
			if (optionType == null) {
				returnMessage_ = returnMessage_
						+ "One of the flags is incorrect.\n";
				isCorrectCommand_ = false;
				break;
			}
			Logger.getInstance().debug(CLASSNAME, "parseFormalInput",
					"formalInputArray_[" + i + "]: " + formalInputArray_[i]);
			setFormalOption(optionType, formalInputArray_[i]);
		}

		checkLogicOfDateAndTime();
	}

	/**
	 * this method will check what the option type is respectively.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * @param input
	 *            : the option to be checked.
	 * @return the type of option if valid.
	 */
	public Options checkFormalOptionsType(String input) {
		input = input.trim();
		String[] inputArray = input.split(" ");

		Options option = null;

		if (inputArray[0].equalsIgnoreCase("sf")) {
			option = Options.START_DATE;
		} else if (inputArray[0].equalsIgnoreCase("fb")) {
			option = Options.FINISH_DATE;
		} else if (inputArray[0].equalsIgnoreCase("st")) {
			option = Options.START_TIME;
		} else if (inputArray[0].equalsIgnoreCase("ft")) {
			option = Options.FINISH_TIME;
		} else if (inputArray[0].equalsIgnoreCase("l")) {
			option = Options.LABEL;
		}

		return option;
	}

	/**
	 * this method will set the action for the respective options.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @param optionType
	 *            : the type of option to set.
	 * @param value
	 *            : the value to be set to the option.
	 * 
	 * @since v0.2
	 */
	public void setFormalOption(Options optionType, String value) {
		assert optionType != null;

		String[] valueArray = value.split(" ");
		value = "";
		for (int i = 1; i < valueArray.length; i++) {
			value += valueArray[i];
			value += " ";
		}// remove the prefix that indicates the option type
		value = value.trim();

		setOption(optionType, value);
	}

	/**
	 * this method will perform the according actions to the specified options .<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 25 October 2011
	 * @finish 25 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @param optionType
	 *            the option
	 * @param value
	 *            the value of that option
	 * @since v0.2
	 */
	public void setOption(Options optionType, String value) {
		assert optionType != null;

		switch (optionType) {
		case START_DATE_SPECIAL_1:
		case START_DATE_SPECIAL_2:
		case START_DATE:
			if (DateTimeUtil.DateParser(value) == null) {
				isCorrectCommand_ = false;
				returnMessage_ += "The start date is not in correct format or not valid.\n";
			} else {
				newTask_.setStartFrom_(DateTimeUtil.DateParser(value));
			}
			break;
		case FINISH_DATE:
			if (DateTimeUtil.DateParser(value) == null) {
				isCorrectCommand_ = false;
				returnMessage_ += "The finish date is not in correct format or not valid.\n";
			} else {
				newTask_.setFinishBy_(DateTimeUtil.DateParser(value));
			}
			break;
		case START_TIME:
			if (DateTimeUtil.TimeParser(value) == null) {
				isCorrectCommand_ = false;
				returnMessage_ += "The start time is not in correct format or not valid.\n";
			} else {
				newTask_.setStartTime_(DateTimeUtil.TimeParser(value));
			}
			break;
		case FINISH_TIME:
			if (DateTimeUtil.TimeParser(value) == null) {
				isCorrectCommand_ = false;
				returnMessage_ += "The finish time is not in correct format or not valid.\n";
			} else {
				newTask_.setFinishTime_(DateTimeUtil.TimeParser(value));
			}
			break;
		case LABEL:
			if (parseLabels(value).isEmpty()) {
				isCorrectCommand_ = false;
				returnMessage_ += "There is no label specified.\n";
			} else {
				newTask_.setLabels_(parseLabels(value));
			}
			break;
		}
	}

	/**
	 * this method will check if the start date and the finish date are the
	 * same.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 September 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 */
	public void checkStartAndFromDate(boolean startIsFinish) {
		if (startIsFinish) {
			newTask_.setFinishBy_(newTask_.getStartFrom_());
		}

		// If the start date is null but the start time is not null, it should
		// be changed
		if (newTask_.getStartFrom_() == null && newTask_.getFinishBy_() != null
				&& newTask_.getFinishTime_() == null
				&& newTask_.getStartTime_() != null) {
			newTask_.setFinishTime_(newTask_.getStartTime_());
			newTask_.setStartTime_(null);
		}
	}

	/**
	 * this method will parse the label string which is separated by ';'.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 19 September 2011
	 * @finish 27 September 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 27 September 2011
	 * 
	 * @since v0.2
	 * 
	 * @param value
	 *            the labels separated by ';' to be parsed.
	 * @return the vector of labels indicated by the input string.
	 */
	public Vector<Label> parseLabels(String value) {
		Vector<Label> labels = new Vector<Label>();
		String[] labelsName = value.split(";");

		for (int i = 0; i < labelsName.length; i++) {
			if (!labelsName[i].equals("")) {
				labels.add(new Label(labelsName[i]));
			}
		}
		return labels;
	}

	/**
	 * this method will handle natural input from user.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 25 October 2011
	 * @finish 25 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 */
	public void parseInformalInput() {
		int indexSecondQuote = findSecondQuote();
		boolean startIsFinish = false;

		getKeyWords(indexSecondQuote);

		removeNonKeyWords();

		String taskName = findTaskName();
		if (taskName.equals("")) {
			returnMessage_ = returnMessage_ + "Task name is empty.\n";
			isCorrectCommand_ = false;
		}

		for (int i = 0; i < keyWords_.size() - 1; i++) {
			String value = getValueOfOption(i);
			value = checkBothDateAndTime(value, i);
			Options optionType = checkInformalOptionsType(keyWords_.get(i),
					value);
			if (optionType == null) {
				returnMessage_ = returnMessage_
						+ "One of the options is incorrect.\n";
				isCorrectCommand_ = false;
				break;
			}
			if (keyWords_.get(i).equalsIgnoreCase("on")) {
				startIsFinish = true;
			}
			if (optionType == Options.START_DATE_SPECIAL_1) {
				value = keyWords_.get(i);
			}
			if (optionType == Options.START_DATE_SPECIAL_2) {
				value = keyWords_.get(i) + " " + value;
			}
			setOption(optionType, value);
		}

		checkStartAndFromDate(startIsFinish);
		checkLogicOfDateAndTime();
	}

	/**
	 * this method will find the second quote in the natural input.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 */
	public int findSecondQuote() {
		int secondQuote = 0;
		int count = 0;
		for (int i = 0; i < informalInputArray_.length; i++) {
			if (informalInputArray_[i].contains("\"")) {
				count++;
				if (count == 2) {
					secondQuote = i;
					break;
				}
			}
		}

		return secondQuote;
	}

	/**
	 * this method will find the key words from the natural input.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 */
	public void getKeyWords(int index) {
		for (int i = index; i < informalInputArray_.length; i++) {
			if (informalInputArray_[i].compareToIgnoreCase("label") == 0
					|| informalInputArray_[i].compareToIgnoreCase("labels") == 0
					|| informalInputArray_[i].compareToIgnoreCase("from") == 0
					|| informalInputArray_[i].compareToIgnoreCase("to") == 0
					|| informalInputArray_[i].compareToIgnoreCase("at") == 0
					|| informalInputArray_[i].compareToIgnoreCase("@") == 0
					|| informalInputArray_[i].compareToIgnoreCase("on") == 0
					|| informalInputArray_[i].compareToIgnoreCase("till") == 0
					|| informalInputArray_[i].compareToIgnoreCase("until") == 0
					|| informalInputArray_[i].compareToIgnoreCase("start") == 0
					|| informalInputArray_[i].compareToIgnoreCase("finish") == 0
					|| informalInputArray_[i].compareToIgnoreCase("by") == 0
					|| informalInputArray_[i].compareToIgnoreCase("today") == 0
					|| informalInputArray_[i].compareToIgnoreCase("tomorrow") == 0
					|| informalInputArray_[i].compareToIgnoreCase("this") == 0
					|| informalInputArray_[i].compareToIgnoreCase("next") == 0) {
				keyWords_.add(informalInputArray_[i]);
				indexKeyWords_.add(new Integer(i));
			}
		}

		// add one more word to make the method getValueOfOption(int) work
		keyWords_.add("rubbish");
		indexKeyWords_.add(new Integer(informalInputArray_.length));
	}

	/**
	 * this method will remove non key words from the vector of key words.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 */
	public void removeNonKeyWords() {
		// remove in case of "from tomorrow", "until next wednesday",....
		for (int i = 0; i < keyWords_.size() - 1; i++) {
			if (keyWords_.get(i).equalsIgnoreCase("tomorrow")
					|| keyWords_.get(i).equalsIgnoreCase("today")
					|| keyWords_.get(i).equalsIgnoreCase("next")
					|| keyWords_.get(i).equalsIgnoreCase("this")) {
				String wordBefore = informalInputArray_[(int) indexKeyWords_
						.get(i) - 1];
				if (wordBefore.equalsIgnoreCase("from")
						|| wordBefore.equalsIgnoreCase("to")
						|| wordBefore.equalsIgnoreCase("till")
						|| wordBefore.equalsIgnoreCase("until")
						|| wordBefore.equalsIgnoreCase("start")
						|| wordBefore.equalsIgnoreCase("finish")
						|| wordBefore.equalsIgnoreCase("by")) {
					keyWords_.remove(i);
					indexKeyWords_.remove(i);
					i--;
				}
			}
		}
		// remove in case of "next next wednesday",....
		for (int i = 0; i < keyWords_.size() - 1; i++) {
			if (keyWords_.get(i).equalsIgnoreCase("next")) {
				String wordBefore = informalInputArray_[(int) indexKeyWords_
						.get(i) - 1];
				if (wordBefore.equalsIgnoreCase("next")) {
					keyWords_.remove(i);
					indexKeyWords_.remove(i);
					i--;
				}
			}
		}

		for (int i = 0; i < keyWords_.size() - 1; i++) {
			String value = getValueOfOption(i);
			value = checkBothDateAndTime(value, i);
			Options optionType = checkInformalOptionsType(keyWords_.get(i),
					value);
			if (optionType == null) {
				keyWords_.remove(i);
				indexKeyWords_.remove(i);
				i--;
			}
		}
	}

	/**
	 * this method will find the name of the task.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @return name of the task
	 * @since v0.2
	 */
	public String findTaskName() {
		String taskName = "";
		for (int i = 0; i < (int) indexKeyWords_.get(0); i++) {
			if (informalInputArray_[i].contains("\"")) {
				informalInputArray_[i] = informalInputArray_[i].replace("\"",
						"");
			}
			taskName = taskName + informalInputArray_[i];
			if (i != (int) indexKeyWords_.get(0) - 1) {
				taskName = taskName + " ";
			}
		}
		newTask_.setTaskName_(taskName);

		return taskName;
	}

	/**
	 * this method will find the according values of the options.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @param index
	 *            index of the key word in the vector of key words
	 * @return string of the value of the option
	 * 
	 * @since v0.2
	 */
	public String getValueOfOption(int index) {
		String value = "";
		for (int j = (int) indexKeyWords_.get(index) + 1; j < (int) indexKeyWords_
				.get(index + 1); j++) {
			value += informalInputArray_[j];
			value += " ";
		}
		value = value.trim();

		return value;
	}

	/**
	 * this method will check if the value of option is a combination of both
	 * date and time.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 27 October 2011
	 * @finish 27 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate October 2011
	 * 
	 * @param input
	 *            the value of the key word at the specified index
	 * @param index
	 *            the index of the key word
	 * @return string of the new value of the key word
	 * 
	 * @since v0.2
	 */
	public String checkBothDateAndTime(String input, int index) {
		String answer = "";
		String[] wordArray = input.split(" ");

		if (wordArray.length >= 1) {
			// Eg: "tomorrow 3PM", "next tue 8am"
			if (DateTimeUtil.TimeParser(wordArray[wordArray.length - 1]) != null) {
				Options optionType = checkInformalOptionsType(
						keyWords_.get(index), wordArray[wordArray.length - 1]);
				if (optionType != Options.LABEL) {
					setOption(optionType, wordArray[wordArray.length - 1]);
					for (int i = 0; i < wordArray.length - 1; i++) {
						answer = answer + wordArray[i];
						if (i != wordArray.length - 2) {
							answer = answer + " ";
						}
					}
					if (!answer.equals("")) {
						return answer;
					}
					if (keyWords_.get(index).equalsIgnoreCase("tomorrow")
							|| keyWords_.get(index).equalsIgnoreCase("today")) {
						return answer;
					}
				}
			}
		}
		if (wordArray.length >= 2) {
			String lastTwoWords = wordArray[wordArray.length - 2]
					+ wordArray[wordArray.length - 1];
			// Eg: "tomorrow 3 PM", "next tue 8 am",.....
			if (DateTimeUtil.TimeParser(lastTwoWords) != null) {
				Options optionType = checkInformalOptionsType(
						keyWords_.get(index), lastTwoWords);
				if (optionType != Options.LABEL) {
					setOption(optionType, lastTwoWords);
					for (int i = 0; i < wordArray.length - 2; i++) {
						answer = answer + wordArray[i];
						if (i != wordArray.length - 3) {
							answer = answer + " ";
						}
					}
					if (!answer.equals("")) {
						return answer;
					}
					if (keyWords_.get(index).equalsIgnoreCase("tomorrow")
							|| keyWords_.get(index).equalsIgnoreCase("today")) {
						return answer;
					}
				}
			}
		}

		answer = answer + input;
		return answer;
	}

	/**
	 * this method will check the option types of the natural input.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @param input
	 *            the option needed to be checked
	 * @param value
	 *            the value according to that option.
	 * @return the type of that option.
	 * 
	 * @since v0.2
	 */
	public Options checkInformalOptionsType(String input, String value) {
		input = input.trim();

		Options option = null;

		if (input.equalsIgnoreCase("tomorrow")
				|| input.equalsIgnoreCase("today")) {
			Date date = DateTimeUtil.DateParser(input);
			if (date != null) {
				option = Options.START_DATE_SPECIAL_1;
			}
		}
		if (input.equalsIgnoreCase("this") || input.equalsIgnoreCase("next")) {
			Date date = DateTimeUtil.DateParser(input + " " + value);
			if (date != null) {
				option = Options.START_DATE_SPECIAL_2;
			}
		}
		if (input.equalsIgnoreCase("from") || input.equalsIgnoreCase("on")
				|| input.equalsIgnoreCase("start")) {
			Date date = DateTimeUtil.DateParser(value);
			if (date != null) {
				option = Options.START_DATE;
			}
		}
		if (input.equalsIgnoreCase("to") || input.equalsIgnoreCase("till")
				|| input.equalsIgnoreCase("until")
				|| input.equalsIgnoreCase("finish")
				|| input.equalsIgnoreCase("by")) {
			Date date = DateTimeUtil.DateParser(value);
			if (date != null) {
				option = Options.FINISH_DATE;
			}
		}
		if (input.equalsIgnoreCase("to") || input.equalsIgnoreCase("till")
				|| input.equalsIgnoreCase("until")
				|| input.equalsIgnoreCase("finish")
				|| input.equalsIgnoreCase("by")
				|| input.equalsIgnoreCase("tomorrow")
				|| input.equalsIgnoreCase("today")
				|| input.equalsIgnoreCase("this")
				|| input.equalsIgnoreCase("next")) {
			Time time = DateTimeUtil.TimeParser(value);
			if (time != null) {
				option = Options.FINISH_TIME;
			}
		}
		if (input.equalsIgnoreCase("from") || input.equalsIgnoreCase("at")
				|| input.equalsIgnoreCase("@")
				|| input.equalsIgnoreCase("start")
				|| input.equalsIgnoreCase("on")
				|| input.equalsIgnoreCase("tomorrow")
				|| input.equalsIgnoreCase("today")
				|| input.equalsIgnoreCase("this")
				|| input.equalsIgnoreCase("next")) {
			Time time = DateTimeUtil.TimeParser(value);
			if (time != null) {
				option = Options.START_TIME;
			}
		}
		if (input.equalsIgnoreCase("label") || input.equalsIgnoreCase("labels")) {
			option = Options.LABEL;
		}

		return option;
	}

	/**
	 * this method will check the logic of date and time.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 15 October 2011
	 * @finish 15 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 */
	public void checkLogicOfDateAndTime() {
		if (newTask_.getStartFrom_() != null && newTask_.getFinishBy_() != null) {
			java.util.Date date1 = new java.util.Date(newTask_.getStartFrom_()
					.getTime());
			java.util.Date date2 = new java.util.Date(newTask_.getFinishBy_()
					.getTime());
			if (date1.compareTo(date2) > 0) {
				returnMessage_ = returnMessage_
						+ "The start date is after the finish date.\n";
				isCorrectCommand_ = false;
			} else if (date1.compareTo(date2) == 0
					&& newTask_.getStartTime_() != null
					&& newTask_.getFinishTime_() != null) {
				Calendar c1 = Calendar.getInstance();
				c1.setTime(Time.valueOf(newTask_.getStartTime_().toString()));
				Calendar c2 = Calendar.getInstance();
				c2.setTime(Time.valueOf(newTask_.getFinishTime_().toString()));

				if (c1.compareTo(c2) > 1) {
					returnMessage_ = returnMessage_
							+ "The start time is after the finish time.\n";
					isCorrectCommand_ = false;
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.cmd.Command#run()
	 */
	/**
	 * @programmer Le Minh Khue
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 */
	@Override
	public Display run() {
		boolean addSuccessful = false;
		DisplayCLI display = new DisplayCLI();

		if (isCorrectCommand_) {
			if (isFormalFormat_) {
				parseFormalInput();
			} else {
				parseInformalInput();
			}

			if (isCorrectCommand_) {
				addSuccessful = storage_.addTask(newTask_);

				Logger.getInstance().debug(CLASSNAME, "run",
						"addSuccessful: " + addSuccessful);

				if (addSuccessful) {
					boolean isBlocked = checkIfBlocked();

					Logger.getInstance().debug(CLASSNAME, "run",
							"isBlocked: " + isBlocked);

					if (isBlocked) {
						returnMessage_ = returnMessage_ + "Note: "
								+ newTask_.getTaskName_() + " is in the "
								+ blockPeriod_.getBlockoutName_()
								+ " blocked period.\n";
					}
					display.setData_(returnMessage_ + newTask_.getTaskName_()
							+ " has been added.");
				} else {
					display.setData_(returnMessage_ + newTask_.getTaskName_()
							+ " failed to be added.");
				}
			}
		}

		if (!isCorrectCommand_) {
			display.setData_(returnMessage_
					+ "Encountered error when adding your task.");
		}

		return display;
	}

	/**
	 * this method will check if the task is blocked by the block out periods.<br>
	 * 
	 * @programmer Le Minh Khue
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 * 
	 * @since v0.2
	 * 
	 * @return true if the task is blocked.
	 */
	public boolean checkIfBlocked() {
		boolean isBlocked = false;

		Date startDate = newTask_.getStartFrom_();
		Date endDate = newTask_.getFinishBy_();

		Blockout blockReturn = null;
		if (startDate != null) {
			blockReturn = storage_.checkIfDateBlocked(startDate);
		}
		if (endDate != null && blockReturn == null) {
			blockReturn = storage_.checkIfDateBlocked(endDate);
		}

		boolean startDateBlocked = startDate != null && blockReturn != null;
		if (startDateBlocked) {
			isBlocked = true;
			this.blockPeriod_ = blockReturn;
		}

		boolean endDateBlocked = endDate != null && blockReturn != null;
		if (endDateBlocked) {
			isBlocked = true;
			this.blockPeriod_ = blockReturn;
		}

		return isBlocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.cmd.Command#undo()
	 */
	/**
	 * 
	 * @programmer Le Minh Khue
	 * @start 16 October 2011
	 * @finish 16 October 2011
	 * 
	 * @reviewer The3Cube
	 * @reviewdate 25 October 2011
	 */
	@Override
	public Display undo() {
		DisplayCLI display = new DisplayCLI();
		display.setData_("Encountered error when deleting your task.");
		boolean isSuccessful = false;

		if (isCorrectCommand_) {
			if (newTask_ != null) {
				isSuccessful = storage_.deleteTask(newTask_);
				if (isSuccessful) {
					display.setData_(newTask_.getTaskName_()
							+ " has been deleted.");
				} else {
					display.setData_(newTask_.getTaskName_()
							+ " failed to be deleted.");
				}
			}
		}
		return display;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blogspot.the3cube.beefree.logic.cmd.Command#setStorage(com.blogspot
	 * .the3cube.beefree.storage.Storage)
	 */
	@Override
	public void setStorage(Storage storage) {
		this.storage_ = storage;
	}
}