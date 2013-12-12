package com.blogspot.the3cube.beefree.ui;

import java.util.Vector;

/**
 * This class should be extended by a data object which are observable.<br>
 * The data structure should inherit the addObserver method updateObserver
 * method.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.2
 * @version 	v0.2
 * 
 */
public abstract class Observable {
	private Vector<Observer> observers_ = new Vector<Observer>();

	/**
	 * an observable allow adding of observer to his private vector.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			16 October 2011
	 * @finish 			16 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param o 		the observer which will be notified of changes.<br>
	 */
	public void addObserver(Observer o) {
		assert observers_ != null;
		
		if (o != null) {
			observers_.add(o);
		}
	}

	/**
	 * an observable should implement this method.<br>
	 * the observer should update his list of observer when thing change.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		16 October 2011
	 * @finish 		16 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 		v0.2
	 */
	public void updateObserver() {
		assert observers_ != null;
		
		for (Observer o : observers_) {
			o.updateView();
		}

	}
}
