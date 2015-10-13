package main;

import gamelogic.WorldGameState;

import java.io.File;

import storage.XMLWriter;

public class SavingMain {
	
	public static void main(String args[]){
		XMLWriter writer = new XMLWriter();
		WorldGameState forDebugging = writer.createGame();
		writer.saveState(forDebugging,new File("Standard-Entities.xml"), new File("Standard-Tiles.xml"));
	}

}
