package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import gamelogic.RoomState;
import gamelogic.WorldGameState;
import gamelogic.entities.GameEntity;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.HarmfulTile;
import gamelogic.tiles.InteriorStandardTile;

public class XMLParser {
	
	public GameRoomTile[][] tiles;
	public GameEntity[][] entities;
	public RoomState currentRoom;
	public String[] roomProperties;
	public WorldGameState gameState;
	
	public WorldGameState parse(File file){
		ArrayList<RoomState> rooms =  parseTiles();
		//for debugging purposes
		for (RoomState r: rooms){
			System.out.println(r.getId());
			System.out.println(r.getDescription());
			System.out.println("=====================");
			GameRoomTile[][] tiles = r.getTiles();
			for (int i = 0; i < tiles.length; i++){
				for (int j = 0; j < tiles[i].length; j++){
					System.out.println(tiles[i][j].toXMLString());
				}
			}
			System.out.println();
		}
		parseEntities(file, rooms);
		
		return null;
	}

	public ArrayList<RoomState> parseTiles() {
		
		ArrayList<RoomState> rooms = new ArrayList<RoomState>();
	
		try {
			InputStream in = new FileInputStream(new File("Standard-Tiles.xml"));
			XMLEventReader xmlreader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);

			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elemName = startElement.getName().getLocalPart();

					if (elemName.equals("worldState")) {
						System.out.println("worldState");
						continue;
					}
					if (elemName.equals("rooms")) {
						System.out.println("rooms");
						continue;
					}
					
					if (elemName.equals("room")) {
						
						event = xmlreader.nextEvent();						
						roomProperties = event.asCharacters().getData().split("-");
						
						for (int i = 0; i < roomProperties.length; i++) {
								System.out.print("I: " + i + "   " + roomProperties[i] + " ");
						}
						System.out.println();
						
						int width = Integer.parseInt(roomProperties[1]);
						int height = Integer.parseInt(roomProperties[2]);
						
						//create a new array for the tiles the will be added to the currentRoom
						tiles = new GameRoomTile[width][height];
						
						
						
						//moves the event to the first starting tile element
						event = xmlreader.nextEvent();
						
						//while the event isn't the end room tag
						while (event.isStartElement()){
								startElement = event.asStartElement();
								elemName = startElement.getName().getLocalPart();				
								if (elemName.equals("tile")){ //should always be tile anyway
									
									//moves the event on to the text between the tags and then put that information into an array
									event = xmlreader.nextEvent();
									String[] tileProperties = event.asCharacters().getData().split("-");
									
									int xCoordinate = Integer.parseInt(tileProperties[1]);
									int yCoordinate = Integer.parseInt(tileProperties[2]);
									
									//populate the tiles array which will later be assigned to the currentRoom
									tiles[xCoordinate][yCoordinate] = parseTile(tileProperties[0]); 
					
									//tileProperties[0] = Type of tile it is
									//tileProperties[1] = x coordinates of tile
									//tileProperties[2] = y coordinates of tile
									
									
									event = xmlreader.nextTag(); //goes to the end tile tag </tile
									//System.out.println(event);
								}
								
							//end of loop
							event = xmlreader.nextTag(); //goes to the new start tile tag 
						}
					}

				}
				if (event.isEndElement()){
					EndElement end = event.asEndElement();
					String elemName = end.getName().getLocalPart();
					
					if (elemName.equals("worldState")) {
						System.out.println("/worldState");
						continue;
					}
					if (elemName.equals("rooms")) {
						System.out.println("/rooms");
						continue;
					}
					if (elemName.equals("room")) {
						//create a new roomstate
						int id = Integer.parseInt(roomProperties[0]);
						int width = Integer.parseInt(roomProperties[1]);
						int height = Integer.parseInt(roomProperties[2]);
						boolean isDark = Boolean.getBoolean(roomProperties[3]);
						String description = roomProperties[4];
						RoomState room = new RoomState(tiles, width, height, id, isDark, description);
						
						
						rooms.add(room);//add it to the rooms arraylist
						
					}
				}
					
				}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

		return rooms;
	}
	
	public void parseEntities(File file, ArrayList<RoomState> rooms) {
		ArrayList<RoomState> entitiesRooms = new ArrayList<RoomState>();
		try {
			//InputStream in = new FileInputStream(file);
			InputStream in = new FileInputStream(file);
			XMLEventReader xmlreader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);

			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elemName = startElement.getName().getLocalPart();

					if (elemName.equals("worldState")) {
						System.out.println("worldState");
						continue;
					}
					if (elemName.equals("rooms")) {
						System.out.println("rooms");
						continue;
					}
					if (elemName.equals("room")) {
						// RoomState current = new RoomState();
						event = xmlreader.nextEvent();
						roomProperties = event.asCharacters().getData().split("-");
						
						for (int i = 0; i < roomProperties.length; i++) {
							System.out.print("I: " + i + "   " + roomProperties[i] + " ");
						}
						System.out.println();

					}
				}
				
				if (event.isEndElement()){
					EndElement end = event.asEndElement();
					String elemName = end.getName().getLocalPart();
					
					if (elemName.equals("worldState")) {
						System.out.println("/worldState");
						continue;
					}
					if (elemName.equals("rooms")) {
						System.out.println("/rooms");
						continue;
					}
					if (elemName.equals("room")) {
						//create a new roomstate
						int id = Integer.parseInt(roomProperties[0]);
						int width = Integer.parseInt(roomProperties[1]);
						int height = Integer.parseInt(roomProperties[2]);
						boolean isDark = Boolean.getBoolean(roomProperties[3]);
						String description = roomProperties[4];
						RoomState room = new RoomState(tiles, width, height, id, isDark, description);
						
						
						entitiesRooms.add(room);//add it to the rooms arraylist
						
					}
				}

			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

	}
	
	public GameRoomTile parseTile(String type){
		switch (type){
		case "Interior_Tile":
			return new InteriorStandardTile();		
		case "Harmful_Tile":
			return new HarmfulTile();
		}
		
		return new InteriorStandardTile(); //to make it compile even though all possible cases are covered in switch statement
		
		
	}

}
