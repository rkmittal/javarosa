package org.javarosa.formmanager.view.chatterbox;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.Spacer;

import de.enough.polish.ui.Style;

public class SubmitScreen extends de.enough.polish.ui.Form implements ItemStateListener, CommandListener {

	public SubmitScreen (String s) { super(s); } //temp
	
//	private Controller controller;
//	private ChoiceGroup cg;
//	private ModelMetaData mmd;
//	private static final String SEND_NOW_DEFAULT = "Send Now";
//	private static final String SEND_NOW_SPEC = "Send to new server";
//	private static final String SEND_LATER = "Send Later";
//	
//    //private Command selectCommand;
//    
//	public SubmitScreen (Controller controller, ModelMetaData mmd, Style style) {
//		super("Form Complete", style);
//		this.controller = controller;
//		this.mmd = mmd;
//		//setCommandListener(this);
//		setItemStateListener(this);
//		
//		//selectCommand = new Command("Select", Command.OK, 1);
//		
//		//#style submitYesNo
//		cg = new ChoiceGroup("Send data now?", Choice.EXCLUSIVE);
//		cg.append(SEND_NOW_DEFAULT, null);
//		cg.append(SEND_LATER, null);
//		cg.append(SEND_NOW_SPEC, null);
//		cg.setSelectedIndex(0, true);
//		
//		//addCommand(selectCommand);
//		
//		append(cg);
//		append(new Spacer(80, 0));
//	}
//	
//	public SubmitScreen (Controller controller, ModelMetaData mmd) {
//		this(controller, mmd, null);
//	}
	
	public void commandAction (Command c, Displayable d) {

	}
	
	public void itemStateChanged (Item i) {
//		if (i == cg) {
//			if (cg.getSelectedIndex() == 0) {
//				System.out.println("sending data");
//				controller.submitData(mmd, true);
//			} else if(cg.getSelectedIndex() == 1){
//				controller.closeForm();
//			}
//			else {
//				System.out.println("sending data");
//				controller.submitData(mmd, true,true);
//			}
//		}
	}
}
