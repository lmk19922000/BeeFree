package com.blogspot.the3cube.beefree.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import com.blogspot.the3cube.beefree.util.Variable;

/**
 * A interface that talk to the underlying text file for the other class.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.1
 * @version 	v0.2
 */
public class FileSystem {

	private static final boolean DO_NOT_APPEND = false;
	private static final boolean APPEND = true;
	private static final String EMPTY_STRING = "";
	
	/**
	 * this method will append a single line to the text file.<br>
	 * the method also parse the string, char by char to remove white spaces.<br>
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
	 * @param text 	the line to be appended.
	 * @return 		if the append is completed successfully.
	 */
	public static boolean appendToTextFile(String filename, String text) {
		boolean completedSuccessfully = true;

		String parsedText = "";
		for (char c : text.toCharArray()) {
			if (c == ' ') {
				parsedText += c;
			} else if (!Character.isWhitespace(c)) {
				parsedText += c;
			}
		}
		
		File file = new File(filename);
		FileWriter writer = null;

		try {
			writer = new FileWriter(file, APPEND);

			writer.write(parsedText);
			writer.write(Variable.NEW_LINE);

			writer.flush();
			writer.close();
		} catch (IOException e) {
			completedSuccessfully = false;
		}

		return completedSuccessfully;
	}
	
	/**
	 * this method will check if the file exists.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			17 September 2011
	 * @finish			20 September 2011
	 * 
	 * @reviewer		The3Cube
	 * @reviewdate		20 September 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param filename 	the file to check if exists.
	 * @return 			if the file exists.
	 */
	public static boolean checkIfExists(String filename) {
		File file = new File(filename);
		return file.exists();
	}

	/**
	 * this method will clear the text file.<br>
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
	 * @return 		if text file is cleared successfully;
	 */
	public static boolean clearTextFile(String filename) {
		boolean clearSuccessfully = true;

		File file = new File(filename);
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, DO_NOT_APPEND);

			writer.write(EMPTY_STRING);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			clearSuccessfully = false;
		}
		return clearSuccessfully;
	}

	/**
	 * this method create the file with the provided filename.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			17 September 2011
	 * @finish			20 September 2011
	 * 
	 * @reviewer		The3Cube
	 * @reviewdate		20 September 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param filename 	the name of the file to create.
	 * @return 			if the file is create.
	 */
	public static boolean createFile(String filename) {
		File newFile = new File(filename);
		boolean isCreated = false;

		try {
			isCreated = newFile.createNewFile();
		} catch (IOException e) {
			isCreated = false;
		}
		return isCreated;
	}

	/**
	 * this method will read string seperated by lines from the filename.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start			17 September 2011
	 * @finish			20 September 2011
	 * 
	 * @reviewer		The3Cube
	 * @reviewdate		20 September 2011
	 * 
	 * @since			v0.1
	 * 
	 * @param filename 	the file to read from.
	 * @return 			the line read from the file.
	 */
	public static Vector<String> readFromFile(String filename) {
		Vector<String> read = new Vector<String>();

		try {
			File file = new File(filename);
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				read.add(line);
			}
		} catch (FileNotFoundException e) {
			read = null;
		} catch (IOException e) {
			read = null;
		}
		return read;
	}
}
