package com.blogspot.the3cube.beefree.util;

/**
 * JavaBean to hold the label.<br>
 * This class is not part of the CSV as it will be not holded by a container.<br>
 * Each task will hold their own label in it own vector.<br>
 * 
 * @author 	The3Cube
 * @since 	v0.1
 * @version v0.1
 * 
 */
public class Label {
	private String labelName_;

	/**
	 * construct a empty label object.
	 */
	public Label() {
		this.labelName_ = "";
	}
	
	public Label(String labelName) {
		this.labelName_ = labelName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((labelName_ == null) ? 0 : labelName_.hashCode());
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
		Label other = (Label) obj;
		if (labelName_ == null) {
			if (other.labelName_ != null)
				return false;
		} else if (!labelName_.equals(other.labelName_))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return labelName_;
	}

	/**
	 * @return the labelName_.
	 */
	public String getLabelName_() {
		return labelName_;
	}

	/**
	 * @param labelName_ 	the labelName_ to set.
	 */
	public void setLabelName_(String labelName_) {
		this.labelName_ = labelName_;
	}
}
