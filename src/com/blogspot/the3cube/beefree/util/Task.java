package com.blogspot.the3cube.beefree.util;

import java.sql.Date;
import java.sql.Time;
import java.util.Vector;

import com.blogspot.the3cube.beefree.logic.DateTimeUtil;

/**
 * JavaBean to hold Task.<br>
 * The csv formatted string is in the format of:<br>
 * taskId, taskName, startFrom, finishBy, startTime, finishTime, labelIds,
 * pTaskId;<br>
 * 
 * @author 		The3Cube
 * @since 		v0.1
 * @version 	v0.1
 */
public class Task implements CSV {
	public final static String COMPLETED_LABEL = "Completed";
	
	private int taskId_ = -1;
	private String taskName_;

	private Date startFrom_;
	private Date finishBy_;

	private Time startTime_;
	private Time finishTime_;

	private Vector<Label> labels_;

	/**
	 * construct a empty task.
	 */
	public Task() {
		labels_ = new Vector<Label>();
	}
	
	/**
	 * construct a task object with a csv string.
	 * 
	 * @param csvString a task csv string.
	 */
	public Task(String csvString) {
		String[] parameters = csvString.split(",");
		labels_ = new Vector<Label>();

		for (int i = 0; i < parameters.length; i++) {
			
			if (i == 0) {
				taskId_ = Integer.parseInt(parameters[i]);
			}
			if (i == 1) {
				taskName_ = parameters[i];
			}
			if (i == 2) {
				startFrom_ = DateTimeUtil.DateParser(parameters[i]);
			}
			if (i == 3) {
				finishBy_ = DateTimeUtil.DateParser(parameters[i]);
			}
			if (i == 4) {
				startTime_ = DateTimeUtil.TimeParser(parameters[i]);
			}
			if (i == 5) {
				finishTime_ = DateTimeUtil.TimeParser(parameters[i]);
			}
			if (i == 6) {
				parseLabels(parameters[i]);
			}
		}
	}
	
	/**
	 * this method take in the retrieve labelIds, parse them and add to the vector.<br>
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
	 * @param parameter		the labelIds read from the csv file.
	 */
	private void parseLabels(String parameter) {
		String[] splitLabels = parameter.split(";");
		for (int i = 0; i < splitLabels.length; i++) {
			if (splitLabels[i].length() > 0) {
				labels_.add(new Label(splitLabels[i]));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.data.CSV#toCSV()
	 */
	@Override
	public String toCSV() {
		String r = "";
		r += taskId_;
		r += Variable.COMMA;
		r += taskName_;
		r += Variable.COMMA;
		if (startFrom_ != null) {
			r += startFrom_;
		}
		r += Variable.COMMA;
		if (finishBy_ != null) {
			r += finishBy_;
		}
		r += Variable.COMMA;
		if (startTime_ != null) {
			r += startTime_;
		}
		r += Variable.COMMA;
		if (finishTime_ != null) {
			r += finishTime_;
		}
		r += Variable.COMMA;
		r += generateLabels();
		return r;
	}

	/**
	 * this method will generate a label ids string from the int array.<br>
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
	 * @return		string of multiple labelIds seperated by ';'.
	 */
	private String generateLabels() {
		String r = "";

		for (int i = 0; i < labels_.size(); i++) {
			r += labels_.get(i);
			if (i < labels_.size() - 1) {
				// append the separator only if not the last
				r += ";";
			}
		}

		return r;
	}
	
	/**
	 * this method will add a new label to the current labels.<br>
	 * IMPORTANT: the task is NOT YET saved after calling this method.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			28 September 2011
	 * @finish			25 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate		25 October 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param labelName	the name of the new label to be added.
	 * @return			if the label is added.
	 */
	public boolean addLabel(String labelName) {
		boolean added = false;
		
		if (labelName.length() > 0) {
			labels_.add(new Label(labelName));
			added = true;
		}
		
		return added;
	}
	
	/**
	 * this method will delete the label specify.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			28 September 2011
	 * @finish			25 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param labelName the label name which you want to deleted.
	 * @return			true if it is valid, found and removed.
	 */
	public boolean deleteLabel(String labelName) {
		boolean deleted = false;
		
		for (int i = 0; i < labels_.size(); i++) {
			Label label = labels_.get(i);
			if (label.getLabelName_().equalsIgnoreCase(labelName)) {
				labels_.remove(i);
				deleted = true;
			}
		}
		
		return deleted;
	}
	
	/**
	 * this method will mark the task as done.<br>
	 * the only case it will return false is when it does not contain the label and adding the label failed.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start		28 September 2011
	 * @finish		25 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since		v0.1
	 * @return		true is label is added/already there.
	 */
	public boolean markAsDone() {
		boolean marked = false;
		
		if (!containsLabel(COMPLETED_LABEL)) {
			marked = addLabel(COMPLETED_LABEL);
		} else {
			marked = true;
		}
		
		return marked;
	}
	
	/**
	 * this method check if the task contain the specified label name.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			28 September 2011
	 * @finish			25 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param labelName	the label name to check.
	 * @return			if the task contain this label name.	
	 */
	public boolean containsLabel(String labelName) {
		boolean found = false;
		
		for (int i = 0; i < labels_.size(); i++) {
			Label label = labels_.get(i);
			if (label.getLabelName_().equalsIgnoreCase(labelName)) {
				found = true;
			}
		}
		
		return found;
	}
	
	/**
	 * this method will format the string for display in cli.<br>
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
	 * @return		the string in full detail format except the label
	 */
	public String getDetailsString() {
		String r = "";
		r += "Task ID:\t" + taskId_;
		r += "\n";
		r += "Task:\t\t" + taskName_;
		
		if (startFrom_ != null) {
			r += "\n";
			r += "Start Date:\t" + startFrom_;
			
			if (startTime_ != null) {
				r += ", " + startTime_;
			}
		}
		
		if (finishBy_ != null) {
			r += "\n";
			r += "End Date:\t" + finishBy_;
			
			if (finishTime_ != null) {
				r += ", " + finishTime_;
			}
		}
		
		if (labels_.size() > 0) {
			r += "\n";
			r += "Labels:\t\t";
			
			for (int i = 0; i < labels_.size(); i++) {
				if (i > 0) {
					r += ", ";
				}
				r += labels_.get(i);
			}
		}
		
		return r;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Task clone() {
		return new Task(toCSV());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + taskId_;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (taskId_ != other.taskId_)
			return false;
		return true;
	}

	/**
	 * @return the taskId_.
	 */
	public int getTaskId_() {
		return taskId_;
	}

	/**
	 * @return the taskName_.
	 */
	public String getTaskName_() {
		return taskName_;
	}

	/**
	 * @return the startFrom_.
	 */
	public Date getStartFrom_() {
		return startFrom_;
	}

	/**
	 * @return the finishBy_.
	 */
	public Date getFinishBy_() {
		return finishBy_;
	}

	/**
	 * @return the startTime_.
	 */
	public Time getStartTime_() {
		return startTime_;
	}

	/**
	 * @return the finishTime_.
	 */
	public Time getFinishTime_() {
		return finishTime_;
	}

	/**
	 * @return the labels_.
	 */
	public Vector<Label> getLabels_() {
		return labels_;
	}

	/**
	 * @param taskId_ the taskId_ to set.
	 */
	public void setTaskId_(int taskId_) {
		this.taskId_ = taskId_;
	}

	/**
	 * @param taskName_ the taskName_ to set.
	 */
	public void setTaskName_(String taskName_) {
		this.taskName_ = taskName_;
	}

	/**
	 * @param startFrom_ the startFrom_ to set.
	 */
	public void setStartFrom_(Date startFrom_) {
		this.startFrom_ = startFrom_;
	}

	/**
	 * @param finishBy_ the finishBy_ to set.
	 */
	public void setFinishBy_(Date finishBy_) {
		this.finishBy_ = finishBy_;
	}

	/**
	 * @param startTime_ the startTime_ to set.
	 */
	public void setStartTime_(Time startTime_) {
		this.startTime_ = startTime_;
	}

	/**
	 * @param finishTime_ the finishTime_ to set.
	 */
	public void setFinishTime_(Time finishTime_) {
		this.finishTime_ = finishTime_;
	}

	/**
	 * @param labels_ the labels_ to set.
	 */
	public void setLabels_(Vector<Label> labels_) {
		this.labels_ = labels_;
	}
}
