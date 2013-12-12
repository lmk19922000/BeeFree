package com.blogspot.the3cube.beefree.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.blogspot.the3cube.beefree.storage.BlockoutContainer;
import com.blogspot.the3cube.beefree.storage.FileSystem;
import com.blogspot.the3cube.beefree.util.Blockout;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * this class test the BlockoutContainer functionality.<br>
 * 
 * @author 	Chua Jie Sheng
 * @since 	v0.1
 * @version v0.1
 * 
 */
public class BlockoutContainerTest {

	private BlockoutContainer blockouts_;

	private final static String blockout01 = "1,vacation,2011-12-31,2012-01-30";
	private final static String blockout02 = "5,invalid,,";
	private final static String blockout03 = "4,holiday,2100-12-31,2200-01-30";
	private final static String blockout04 = "8,hello,null,null";
	private final static String blockout05 = "9,hello,junk date,junk date";

	private final static String[] newBlockouts = { blockout01, blockout02,
			blockout03, blockout04, blockout05 };
	private final static boolean[] appendSuccessfully = { true, false, true,
			false, false };

	/**
	 * this method start the testing.<br>
	 * 
	 * @programmer	Chua Jie Sheng
	 * @start		19 September 2011
	 * @finish		27 September 2011
	 * 
	 * @reviewer	The3Cube
	 * @reviewdate	27 September 2011
	 * 
	 * @since 		v0.1
	 */
	@Test
	public void test() {
		blockouts_ = new BlockoutContainer(Variable.TEST_BLOCKOUT_FILENAME);

		// clear before adding
		FileSystem.clearTextFile(Variable.TEST_TASK_FILENAME);

		// init the file
		blockouts_.init();

		// add new blockout
		for (int i = 0; i < newBlockouts.length; i++) {
			Blockout newBlockout = new Blockout(newBlockouts[i]);
			assertTrue(blockouts_.addBlockout(newBlockout) == appendSuccessfully[i]);

			if (appendSuccessfully[i]) {
				int currentBlockoutId = newBlockout.getBlockoutId_();
				int nextBlockoutId = blockouts_.getNextBlockoutId();
				assertTrue(nextBlockoutId > currentBlockoutId);
			}
		}

		// add a null blockout
		assertFalse(blockouts_.addBlockout(null));

		// edit a blockout
		Blockout editedBlockout = new Blockout(blockout03);
		editedBlockout.setBlockoutName_("school holiday");
		Blockout oldBlockout = blockouts_.editBlockout(editedBlockout);
		assertTrue(oldBlockout.equals(new Blockout(blockout03)));

		// delete a blockout
		Blockout deleteBlockout = new Blockout(blockout01);
		assertTrue(blockouts_.deleteBlockout(deleteBlockout));

		// delete a deleted blockout, should return false
		assertFalse(blockouts_.deleteBlockout(deleteBlockout));

		// edit a deleted blockout, should return null
		assertNull(blockouts_.editBlockout(deleteBlockout));
	}

}
