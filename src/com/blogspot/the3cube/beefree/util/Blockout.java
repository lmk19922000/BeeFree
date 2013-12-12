package com.blogspot.the3cube.beefree.util;

import java.sql.Date;

import com.blogspot.the3cube.beefree.BeeFree;
import com.blogspot.the3cube.beefree.logic.DateTimeUtil;

/**
 * JavaBean to hold the Blackout date.<br>
 * 
 * @author 	The3Cube
 * @since 	v0.1
 * @version v0.1
 * 
 */
public class Blockout implements CSV {
	private int blockoutId_ = -1;
	private String blockoutName_;

	private Date startBlockout_;
	private Date endBlockout_;

	/**
	 * construct a empty blockout object.
	 */
	public Blockout() {

	}

	/**
	 * construct a blockout object with a csv string.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		19 September 2011
	 * @finish 		27 September 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	27 September 2011
	 * 
	 * @since 		v0.1
	 * 
	 * @param csvString 	a blockout csv string
	 */
	public Blockout(String csvString) {
		String[] parameters = csvString.split(Variable.COMMA);

		for (int i = 0; i < parameters.length; i++) {
			if (i == 0) {
				blockoutId_ = Integer.parseInt(parameters[i]);
			}
			if (i == 1) {
				blockoutName_ = parameters[i];
			}
			if (i == 2) {
				startBlockout_ = DateTimeUtil.DateParser(parameters[i]);
			}
			if (i == 3) {
				endBlockout_ = DateTimeUtil.DateParser(parameters[i]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blogspot.the3cube.beefree.data.CSV#toCSV()
	 */
	/**
	 * @programmer 	Chua Jie Sheng
	 * @start 		19 September 2011
	 * @finish 		27 September 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	27 September 2011
	 */
	@Override
	public String toCSV() {
		String r = "";

		r += blockoutId_;
		r += Variable.COMMA;
		r += blockoutName_;
		r += Variable.COMMA;
		if (startBlockout_ != null) {
			r += startBlockout_;
		}
		r += Variable.COMMA;
		if (endBlockout_ != null) {
			r += endBlockout_;
		}

		return r;
	}

	/**
	 * this method will format the string for display in cli.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		19 September 2011
	 * @finish 		27 September 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	27 September 2011
	 * 
	 * @since 		v0.1
	 * 
	 * @return 		the string formatted for printing in cli.
	 */
	public String getDetailsString() {
		String r = "";

		r += "Blockout ID:\t" + blockoutId_;
		r += "\n";
		r += "Blockout:\t" + blockoutName_;

		if (startBlockout_ != null) {
			r += "\n";
			r += "From:\t\t" + startBlockout_;
		}
		if (endBlockout_ != null) {
			r += "\n";
			r += "To:\t\t" + endBlockout_;
		}

		return r;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blockoutId_;
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
		Blockout other = (Blockout) obj;
		if (blockoutId_ != other.blockoutId_)
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Blockout clone() {
		// TODO Auto-generated method stub
		return new Blockout(toCSV());
	}

	/**
	 * @return 	the blockoutId_.
	 */
	public int getBlockoutId_() {
		return blockoutId_;
	}

	/**
	 * @return 	the blockoutName_.
	 */
	public String getBlockoutName_() {
		return blockoutName_;
	}

	/**
	 * @return 	the startBlockout_.
	 */
	public Date getStartBlockout_() {
		return startBlockout_;
	}

	/**
	 * @return 	the endBlockout_.
	 */
	public Date getEndBlockout_() {
		return endBlockout_;
	}

	/**
	 * @param blockoutId_ 	the blockoutId_ to set.
	 */
	public void setBlockoutId_(int blockoutId_) {
		this.blockoutId_ = blockoutId_;
	}

	/**
	 * @param blockoutName_ 	the blockoutName_ to set.
	 */
	public void setBlockoutName_(String blockoutName_) {
		this.blockoutName_ = blockoutName_;
	}

	/**
	 * @param startBlockout_ 	the startBlockout_ to set.
	 */
	public void setStartBlockout_(Date startBlockout_) {
		this.startBlockout_ = startBlockout_;
	}

	/**
	 * @param endBlockout_ 	the endBlockout_ to set.
	 */
	public void setEndBlockout_(Date endBlockout_) {
		this.endBlockout_ = endBlockout_;
	}
}
