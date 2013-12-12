package com.blogspot.the3cube.beefree.ui;

/**
 * This class is a data object observer.<br>
 * Any class which wish to observer a data object should implement this class.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.2
 * @version 	v0.2
 * 
 */
public interface Observer {
	
	/**
	 * an observable object should call this class when it is modify so the
	 * observer could refresh the data it is holding.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		16 October 2011
	 * @finish 		16 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 */
	public void updateView();
}
