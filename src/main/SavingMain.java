package main;

import gamelogic.WorldGameState;

import java.io.File;

import storage.XMLWriter;
/**
 * This class is just used to save the standard-map to an xml file so that it can be loaded up when starting a new game
 * @author gomezjosh
 *
 */
public class SavingMain {

	public static void main(String args[]){
		XMLWriter writer = new XMLWriter();
		WorldGameState forDebugging = writer.createGame();
		writer.saveState(new File("Standard-Entities.xml"),forDebugging);
	}

}
