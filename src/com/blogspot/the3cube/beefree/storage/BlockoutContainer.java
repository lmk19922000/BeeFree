package com.blogspot.the3cube.beefree.storage;

import java.sql.Date;
import java.util.Vector;

import com.blogspot.the3cube.beefree.ui.Observable;
import com.blogspot.the3cube.beefree.util.Blockout;

/**
 * A container to hold all the blockout dates.<br>
 * 
 * @author 	The3Cube
 * @since 	v0.1
 * @version v0.1
 * 
 */
public class BlockoutContainer extends Observable {
	private String filename_;
	private Vector<Blockout> blockouts_;

	private static int nextBlockoutId = 1;

	/**
	 * @param filename
	 */
	public BlockoutContainer(String filename) {
		this.filename_ = filename;

		blockouts_ = new Vector<Blockout>();
	}

	/**
	 * this method will init the container.<br>
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
	 * @return 		true if the container is successfully read the text file.
	 */
	public boolean init() {
		boolean initSuccessfully = true;

		boolean exists = FileSystem.checkIfExists(filename_);
		if (exists) {
			Vector<String> read = FileSystem.readFromFile(filename_);
			Blockout[] blockouts = generateBlockouts(read);
			for (Blockout b : blockouts) {
				blockouts_.add(b);
			}
		} else {
			initSuccessfully = FileSystem.createFile(filename_);
		}

		return initSuccessfully;
	}

	/**
	 * this method will generate blockout from the string array.<br>
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
	 * @param read 	the string array read from the file
	 * @return 		an array of blockout
	 */
	private Blockout[] generateBlockouts(Vector<String> read) {
		Blockout[] blockouts = new Blockout[read.size()];
		for (int i = 0; i < read.size(); i++) {
			blockouts[i] = new Blockout(read.get(i));

			int blockoutId = blockouts[i].getBlockoutId_();
			incrementNextBlockoutId(blockoutId);

		}
		return blockouts;
	}

	/**
	 * this method will update the nextBlockoutId to ensure it is the next
	 * available id.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				19 September 2011
	 * @finish 				27 September 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			27 September 2011
	 * 
	 * @since 				v0.1
	 * 
	 * @param blockoutId 	the current blockoutId.
	 */
	private static void incrementNextBlockoutId(int blockoutId) {
		if (blockoutId >= nextBlockoutId) {
			nextBlockoutId = blockoutId + 1;
		}
	}

	/**
	 * this method add a single blockout period to the container.<br>
	 * this method will check if the blockout date is valid.<br>
	 * if the blockout last only one day, both the start and the end date is the
	 * same date.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			19 September 2011
	 * @finish 			27 September 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		27 September 2011
	 * 
	 * @since 			v0.1
	 * 
	 * @param blockout 	the blockout to be added to the container.
	 * @return 			true is the blockout is added successfully.
	 */
	public boolean addBlockout(Blockout blockout) {
		boolean appendSuccessfully = false;

		boolean isNull = blockout == null;
		boolean isValid = true;

		if (!isNull) {
			boolean isStartDateValid = blockout.getStartBlockout_() != null;
			boolean isEndDateValid = blockout.getEndBlockout_() != null;

			isValid = isStartDateValid && isEndDateValid;
		} else {
			isValid = false;
		}

		if (isValid) {
			if (blockout.getBlockoutId_() < 1) {
				blockout.setBlockoutId_(nextBlockoutId);
			}

			String csv = blockout.toCSV();
			appendSuccessfully = FileSystem.appendToTextFile(filename_, csv);

			if (appendSuccessfully) {
				blockouts_.add(blockout);
				incrementNextBlockoutId(blockout.getBlockoutId_());
				updateObserver();
			}
		}

		return appendSuccessfully;
	}

	/**
	 * this method delete the blockout period from the container.<br>
	 * if the blockout is not found, the blockout will not be deleted.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				19 September 2011
	 * @finish 				27 September 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			27 September 2011
	 * 
	 * @since 				v0.1
	 * 
	 * @param toBeDeleted 	the blockout to be deleted.
	 * @return 				true is the blockout is deleted successfully.
	 */
	public boolean deleteBlockout(Blockout toBeDeleted) {
		boolean blockoutDeleted = false;

		for (int i = 0; i < blockouts_.size(); i++) {
			Blockout blockout = blockouts_.get(i);
			if (blockout.equals(toBeDeleted)) {
				blockouts_.remove(i);
				blockoutDeleted = true;
			}
		}

		if (blockoutDeleted) {
			boolean saved = saveToFile();
			blockoutDeleted = blockoutDeleted && saved;
		}

		return blockoutDeleted;
	}

	/**
	 * 
	 * this method will allow the program to hot swap the blockout period.<br>
	 * the parameter input will be the blockout to be swap in.<br>
	 * the return will be the blockout that is swapped out.<br>
	 * if no matching blockout is found, the blockout will be added.<br>
	 * 
	 * @programmer 				Chua Jie Sheng
	 * @start 					19 September 2011
	 * @finish 					27 September 2011
	 * 
	 * @reviewer 				The3Cube
	 * @reviewdate 				27 September 2011
	 * 
	 * @since 					v0.1
	 * 
	 * @param editedBlockout 	the blockout with field edited.
	 * @return 					the old blockout that has been swapped out.
	 */
	public Blockout editBlockout(Blockout editedBlockout) {
		Blockout oldBlockout = null;
		boolean editSuccessfully = false;

		for (int i = 0; i < blockouts_.size(); i++) {
			Blockout blockout = blockouts_.get(i);
			if (blockout.equals(editedBlockout)) {
				oldBlockout = blockouts_.remove(i);
				blockouts_.add(i, editedBlockout);
				editSuccessfully = saveToFile();
				break;
			}
		}

		if (!editSuccessfully) {
			oldBlockout = null;
		}

		return oldBlockout;
	}

	/**
	 * this method will save the data change into the text file.<br>
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
	 * @return 		true if the data is save successfully.
	 */
	public boolean saveToFile() {
		boolean savedSuccessfully = true;
		boolean clearSuccessfully = FileSystem.clearTextFile(filename_);

		if (clearSuccessfully) {
			for (int i = 0; i < blockouts_.size(); i++) {
				Blockout blockout = blockouts_.get(i);
				String csv = blockout.toCSV();
				boolean appendSuccessfully = FileSystem.appendToTextFile(
						filename_, csv);

				savedSuccessfully = savedSuccessfully && appendSuccessfully;
			}
			updateObserver();
		} else {
			savedSuccessfully = false;
		}

		return savedSuccessfully;
	}
	
	/**
	 * this method will find blockout with provided blockout id.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				19 September 2011
	 * @finish 				27 September 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			27 September 2011
	 * 
	 * @since 				v0.1
	 * 
	 * @param blockoutId	the blockout id to find.
	 * @return				the blockout which is found.
	 */
	public Blockout findBlockout(int blockoutId) {
		Blockout blockout = null;
		
		for (int i = 0; i < blockouts_.size(); i++) {
			Blockout b = blockouts_.get(i);
			if (b.getBlockoutId_() == blockoutId) {
				blockout = b;
				break;
			}
		}
		
		return blockout;
	}
	
	/**
	 * this method check if the date supplied is within any blockout.<br>
	 * if it fall under any range, it return the blockout.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			09 October 2011
	 * @finish 			09 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.1
	 * 
	 * @param when		the date to be checked.
	 * @return			the blockout which block the date if any.
	 */
	public Blockout checkIfDateBlocked(Date when) {
		boolean isBlocked = false;
		Blockout blockout = null;
		
		for (int i = 0; i < blockouts_.size(); i++) {
			Blockout b = blockouts_.get(i);
			long start = b.getStartBlockout_().getTime();
			long event = when.getTime();
			long end = b.getEndBlockout_().getTime();
			
			boolean moreThanStart = start <= event;
			boolean lessThanEnd = event <= end;
			isBlocked = moreThanStart && lessThanEnd;
			
			if (isBlocked) {
				blockout = b;
				break;
			}
		}
		return blockout;
	}

	/**
	 * @return the blockouts_.
	 */
	public Vector<Blockout> getBlockouts_() {
		return blockouts_;
	}

	/**
	 * @param blockouts_ 	the blockouts_ to set.
	 */
	public void setBlockouts_(Vector<Blockout> blockouts_) {
		this.blockouts_ = blockouts_;
	}

	/**
	 * @return the nextBlockoutId.
	 */
	public static int getNextBlockoutId() {
		return nextBlockoutId;
	}

	/**
	 * @param nextBlockoutId 	the nextBlockoutId to set.
	 */
	public static void setNextBlockoutId(int nextBlockoutId) {
		BlockoutContainer.nextBlockoutId = nextBlockoutId;
	}
}
