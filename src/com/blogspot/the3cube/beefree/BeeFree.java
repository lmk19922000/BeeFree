package com.blogspot.the3cube.beefree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.blogspot.the3cube.beefree.ui.BeeFreeCLI;
import com.blogspot.the3cube.beefree.ui.BeeFreeGUI;
import com.blogspot.the3cube.beefree.ui.Interface;
import com.blogspot.the3cube.beefree.util.Variable;

/**
 * The main class of BeeFree.<br>
 * User will run this class to start using BeeFree.<br>
 *
 * @author		The3Cube
 * @since		v0.1
 * @version		v0.2
 * 
 */
public class BeeFree {
	private static final String APPNAME = "BeeFree";
	private static final String GUI_FLAG = "gui";
	
	static BeeFreeGUI gui_;
	
	/**
	 * the main method of BeeFree.<br>
	 * will check if the use choose to enter GUI.<br>
	 * default is to CLI.<br>
	 * 
	 * @since v0.1
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		assert args != null;

		Interface userInterface = checkFlag(args);
		if (userInterface == Interface.GUI){
			runGUI();
		} else {
			runCLI();
		}
		

	}

	/**
	 * this method will check if the user choose GUI else defaulted to CLI.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		16 October 2011
	 * @finish 		16 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since		v0.2
	 * 
	 * @param args	the args passed to the program when it was started.
	 * @return 		the chooen interface type.
	 */
	private static Interface checkFlag(String[] args) {
		assert args != null; 
		
		Interface userInterface = Interface.CLI;
		for (String arg : args) {
			if (arg.trim().equalsIgnoreCase(GUI_FLAG)) {
				userInterface = Interface.GUI;
			}
		}
		return userInterface;
	}

	/**
	 * this method will init the CLI and run it.<br>
	 * 
	 * @since v0.1
	 * 
	 */
	private static void runCLI() {
		BeeFreeCLI cli = new BeeFreeCLI();
		cli.run();

	}

	/**
	 * Run the GUI of FreeBee.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		16 October 2011
	 * @finish 		16 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since		v0.2
	 */
	private static void runGUI() {				
		Display display = new Display();
		
        Shell shell = new Shell(display);
        shell.setText(APPNAME);
        shell.setLayout(new FillLayout());
        
        BeeFreeGUI gui = new BeeFreeGUI(shell, SWT.NONE);
        gui.run();
        gui.pack();
        
		shell.pack();
		shell.setMinimumSize(Variable.MIN_WIDTH, Variable.MIN_HEIGHT);
		shell.setBounds(200, 200, Variable.MIN_WIDTH, Variable.MIN_HEIGHT);
		shell.setBackground(SWTResourceManager.getColor(255, 255, 255));
		
		Image shellIcon = new Image(display, "dist\\img\\icon.png");
        shell.setImage(shellIcon);
		
		shell.open();
        
        while(!shell.isDisposed()){
            if(!display.readAndDispatch()) display.sleep();
        }
	}
}
