package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.LockedTeleporter;
import gamelogic.RoomState;
import gamelogic.StandardTeleporter;
import gamelogic.WorldGameState;
import gamelogic.entities.Coin;
import gamelogic.entities.GameEntity;
import gamelogic.entities.Gun;
import gamelogic.entities.KeyCard;
import gamelogic.entities.MazeWall;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Pylon;
import gamelogic.entities.SmallCarrier;
import gamelogic.entities.TeleporterGun;
import gamelogic.entities.Treasure;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.HarmfulTile;
import gamelogic.tiles.InteriorStandardTile;

public class XMLParser {
	
	public GameRoomTile[][] tiles;
	//public GameEntity[][] entities;
	public RoomState currentRoom;
	public String[] roomProperties;
	public WorldGameState gameState;
	HashMap<Integer, RoomState> rooms = new HashMap<Integer, RoomState>();
	ArrayList<Pylon> pylons = new ArrayList<Pylon>();
	//ArrayList<RoomState> rooms = new ArrayList<RoomState>();
	ArrayList<GameEntity[][]> roomEntities = new ArrayList<GameEntity[][]>();
	
	public WorldGameState parse(File file){
		rooms =  parseTiles();
		parseEntities(file);
		addEntities();
		//for debugging purposes
		for (RoomState r: rooms.values()){
			System.out.println(r.getId());
			System.out.println(r.getDescription());
			System.out.println("=====================");
			/*GameRoomTile[][] tiles = r.getTiles();
			for (int i = 0; i < tiles.length; i++){
				for (int j = 0; j < tiles[i].length; j++){
					System.out.println(tiles[i][j].toXMLString());
				}
			}*/
			GameEntity[][] entities = r.getEntities();
			for (int i = 0; i < entities.length; i++){
				for (int j = 0; j < entities[i].length; j++){
					System.out.println(entities[i][j].toXMLString());
				}
			}
			System.out.println();
		}
		WorldGameState game = new WorldGameState(rooms,pylons.get(0), pylons.get(1));
		
		return game;
	}

	public HashMap<Integer,RoomState> parseTiles() {
		
		HashMap<Integer,RoomState> rooms = new HashMap<Integer,RoomState>();
	
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
						/*
						for (int i = 0; i < roomProperties.length; i++) {
								System.out.print("I: " + i + "   " + roomProperties[i] + " ");
						}
						System.out.println();
						*/
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
									
									int xCoordinate = Integer.parseInt(tileProperties[0]);
									int yCoordinate = Integer.parseInt(tileProperties[1]);
									
									//populate the tiles array which will later be assigned to the currentRoom
									tiles[xCoordinate][yCoordinate] = parseTile(tileProperties[2]); 
					
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
						
						
						rooms.put(id, room);//add it to the rooms arraylist
						
					}
				}
					
				}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

		return rooms;
	}
	
	public void parseEntities(File file) {
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
						event = xmlreader.nextEvent();
						String [] currentRoomProperties = event.asCharacters().getData().split("-");
						
//						for (int i = 0; i < currentRoomProperties.length; i++) {
//							System.out.print("I: " + i + "   " + currentRoomProperties[i] + " ");
//						}
//						System.out.println();
						int id = Integer.parseInt(currentRoomProperties[0]);
						int width = Integer.parseInt(currentRoomProperties[1]);
						int height = Integer.parseInt(currentRoomProperties[2]);
						
						//create a new array for the tiles the will be added to the currentRoom
						//roomEntities = new ArrayList<GameEntity[][]>();
						GameEntity[][] entities = new GameEntity[width][height];
						
						
						
						
						//moves the event to the first starting tile element
						event = xmlreader.nextEvent();
						
						//while the event isn't the end room tag
						while (event.isStartElement()){
								startElement = event.asStartElement();
								elemName = startElement.getName().getLocalPart();				
								if (elemName.equals("entity")){ //should always be entity anyway
									//moves the event on to the text between the tags and then put that information into an array
									event = xmlreader.nextEvent();
									String[] entityProperties = event.asCharacters().getData().split("-");
									
									
									int xCoordinate = Integer.parseInt(entityProperties[0]);
									int yCoordinate = Integer.parseInt(entityProperties[1]);
									
									//populate the tiles array which will later be assigned to the currentRoom
									entities[xCoordinate][yCoordinate] = parseEntity(entityProperties); 
					
									//entityProperties[0] = Type of Entity it is
									//entityProperties[1] = x coordinates of entity
									//entityProperties[2] = y coordinates of entity
									
									
									event = xmlreader.nextTag(); //goes to the end tile tag </entity>
									//System.out.println(event);
								}
								
							//end of loop
							event = xmlreader.nextTag(); //goes to the new start tile tag 
						}
						roomEntities.add(entities);
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
					}
				}

			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

	}
	
	private GameEntity parseEntity(String[] properties) {
		String type = properties[3];
		
		/*Only used for the teleporters*/
		int xDestination;
		int yDestination;
		int roomStateID;
		
		CardinalDirection dir = CardinalDirection.valueOf(properties[2]);
		switch (type){
		case "Null_Entity":
			return new NullEntity(dir);
		case "Outer_Wall":
			return new OuterWall(dir);
		case "Independent_Actor":
			return new NullEntity(dir); //we wont load up the Independent Actors
		case "KeyCard":
			return new KeyCard(dir);
		case "Medium_Carrier":
			return new MediumCarrier(dir);
		case "Small_Carrier":
			return new SmallCarrier(dir);
		case "Gun":
			return new Gun(dir);
		case "Maze_Wall":
			return new MazeWall(dir);
		case "Teleporter_Gun":
			return new TeleporterGun(dir);
		case "Coin":
			return new Coin(dir);
		case "Player":
			String irlName = properties[4];
			int healthPercentage = Integer.parseInt(properties[5]);
			int coins = Integer.parseInt(properties[6]);
			CharacterStrategy strategy = properties[7];
		case "Pylon":
			Pylon p = new Pylon(dir);
			pylons.add(p);
			return p;
		case "Locked_Teleporter":
			xDestination = Integer.parseInt(properties[4]);
			yDestination = Integer.parseInt(properties[5]);
			roomStateID = Integer.parseInt(properties[6]);
			return new LockedTeleporter(dir, xDestination, yDestination, rooms.get(roomStateID));
		case "Standard_Teleporter":
			xDestination = Integer.parseInt(properties[4]);
			yDestination = Integer.parseInt(properties[5]);
			roomStateID = Integer.parseInt(properties[6]);
			return new StandardTeleporter(dir, xDestination, yDestination, rooms.get(roomStateID));
		case "Treasure":
			return new Treasure(dir);
		}
		return new NullEntity(dir);
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
	
	public void addEntities(){
		List<RoomState> roomTiles = new ArrayList<RoomState> (rooms.values());
		System.out.println(roomEntities.size());
		
		for (int i = 0; i < roomTiles.size(); i++){
			roomTiles.get(i).setEntities(roomEntities.get(i));
			rooms.put(i, roomTiles.get(i));
		}
		
	}

}
