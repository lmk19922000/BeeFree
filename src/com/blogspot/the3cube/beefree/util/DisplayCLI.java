package com.blogspot.the3cube.beefree.util;




/**
 * This class display in a format specifically for the CLI.<br>
 *
 * @author		The3Cube
 * @since		v0.1
 * @version		v0.1
 * 
 */
public class DisplayCLI implements Display {
	private String data_;
	
	/**
	 * empty constructor.
	 */
	public DisplayCLI() {
		super();
	}
	
	/**
	 * construct a DisplayCLI with data string.
	 * 
	 * @param data	the data which you want to display.
	 */
	public DisplayCLI(String data) {
		this.data_ = data;
	}
	
	/**
	 * return the data for display.<br>
	 * formatted for display in CLI.<br><br>
	 */
	public String toString() {
		return data_;
	}

	/**
	 * @return the data_.
	 */
	public String getData_() {
		return data_;
	}

	/**
	 * @param data_ the data_ to set.
	 */
	public void setData_(String data_) {
		this.data_ = data_;
	}	
}
