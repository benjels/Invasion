package main;

import java.io.File;

import storage.XMLWriter;

public class SavingMain {
	
	public static void main(String args[]){
		XMLWriter writer = new XMLWriter();
		writer.saveState(new File("Standard-Entities.xml"), new File("Standard-Tiles.xml"));
	}

}
