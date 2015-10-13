package main;

import gamelogic.WorldGameState;

import java.io.File;

import storage.XMLParser;
import storage.XMLParser2;

public class LoadingMain {
	
	public static void main(String[] args){
		XMLParser parser = new XMLParser();
		WorldGameState game = parser.parse(new File("Standard-Entities.xml"), new File("Standard-Tile.xml"));
		
		System.out.println(game.getRooms().get(0));
	}

}
