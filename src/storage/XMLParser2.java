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
import gamelogic.IndependentActor;
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
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.HarmfulTile;
import gamelogic.tiles.InteriorStandardTile;

public class XMLParser2 {
	
	//public RoomState currentRoom;
	public WorldGameState gameState;
	
	public HashMap<Integer, RoomState> rooms = new HashMap<Integer, RoomState>();
	
	public ArrayList<GameRoomTile[][]> tiles;
	public GameRoomTile[][] currentTiles;
	public ArrayList<GameEntity[][]> entities;
	public GameEntity[][] currentEntities;
	
	public HashMap<Integer,String[]> roomProperties;
	public String[] currentRoomProperties;
	
	public WorldGameState parse(File file){
		parseTiles();
		parseEntities(file);
		
		printProperties();
		
		return null;
	}
	
	public void printProperties(){
		System.out.println(rooms.size());
		for (RoomState r: rooms.values()){
			System.out.println("room");
			r.debugDraw();
		}
	}

	
	/**
	 * Should add all of the room properties to the roomProperties and add all of the tiles
	 */
	public void parseTiles() {
		
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
						roomProperties = new HashMap<Integer,String[]>();
						continue;
					}
					
					if (elemName.equals("room")) {
						
						event = xmlreader.nextEvent();						
						currentRoomProperties = event.asCharacters().getData().split("-");
						
//						for (int i = 0; i < currentRoomProperties.length; i++) {
//								System.out.print("I: " + i + "   " + currentRoomProperties[i] + " ");
//						}
//						System.out.println();
						
						int width = Integer.parseInt(currentRoomProperties[1]);
						int height = Integer.parseInt(currentRoomProperties[2]);
						
						//create a new array for the tiles the will be added to the currentRoom
						tiles = new ArrayList<GameRoomTile[][]>();
						currentTiles = new GameRoomTile[width][height];
						
						
						
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
									currentTiles[xCoordinate][yCoordinate] = parseTile(tileProperties[0]); 
					
									//tileProperties[0] = Type of tile it is
									//tileProperties[1] = x coordinates of tile
									//tileProperties[2] = y coordinates of tile
									
									
									event = xmlreader.nextTag(); //goes to the end tile tag </tile
									//System.out.println(event);
								}
								
							//end of loop
							event = xmlreader.nextTag(); //goes to the new start tile tag 
						}
						System.out.println("tiles adding");
						tiles.add(currentTiles);
						System.out.println("tiles size" + tiles.size());
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
						int index = Integer.parseInt(currentRoomProperties[0]);
						roomProperties.put(index, currentRoomProperties);
						
					}
				}
					
				}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}
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
						// RoomState current = new RoomState();
						event = xmlreader.nextEvent();
						currentRoomProperties = event.asCharacters().getData().split("-");
						
//						for (int i = 0; i < currentRoomProperties.length; i++) {
//							System.out.print("I: " + i + "   " + currentRoomProperties[i] + " ");
//						}
//						System.out.println();

						int width = Integer.parseInt(currentRoomProperties[1]);
						int height = Integer.parseInt(currentRoomProperties[2]);
						
						//create a new array for the tiles the will be added to the currentRoom
						entities = new ArrayList<GameEntity[][]>();
						currentEntities = new GameEntity[width][height];
						
						
						
						
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
									
									String type = entityProperties[0];
									int xCoordinate = Integer.parseInt(entityProperties[1]);
									int yCoordinate = Integer.parseInt(entityProperties[2]);
									String direction = entityProperties[3];
									
									//populate the tiles array which will later be assigned to the currentRoom
									currentEntities[xCoordinate][yCoordinate] = parseEntity(type, direction); 
					
									//entityProperties[0] = Type of Entity it is
									//entityProperties[1] = x coordinates of entity
									//entityProperties[2] = y coordinates of entity
									
									
									event = xmlreader.nextTag(); //goes to the end tile tag </entity>
									//System.out.println(event);
								}
								
							//end of loop
							event = xmlreader.nextTag(); //goes to the new start tile tag 
						}
						entities.add(currentEntities);
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
						int index = Integer.parseInt(currentRoomProperties[0]);
						roomProperties.put(index, currentRoomProperties);
						
					}
				}

			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

	}
	
	private GameEntity parseEntity(String type, String direction) {
		CardinalDirection dir = CardinalDirection.valueOf(direction);
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
			//this could be difficult.....
			//Moveable Entity anyway so should be loaded seperately
			break;
		case "Pylon":
			return new Pylon(dir);
		case "Locked_Teleporter":
			//return new LockedTeleporter(directionFaced, destinationx, destinationy, destinationRoom)
			//Come back to this to sort out
			System.out.println("Locked Teleporter");
			break;
		case "Standard_Teleporter":
			//return new StandardTeleporter(directionFaced, destinationx, destinationy, destinationRoom)
			//Again Come back to this and sort out later
			System.out.println("Standard Teleporter");
			break;
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
	
	//PRECONDITION:: tiles.size() == entites.size() == roomProperties.size()
	public void createRoomStates(){
		for(int i = 0; i < tiles.size(); i++){
			System.out.println("entered" + i);
			//int width, int height, int roomId,  String roomName
			int roomID =  Integer.parseInt(roomProperties.get(i)[0]);
			int width = Integer.parseInt(roomProperties.get(i)[1]);
			int height = Integer.parseInt(roomProperties.get(i)[2]);
			boolean isDark;
			String roomName =  roomProperties.get(i)[4];
			rooms.put(roomID, new RoomState(tiles.get(i),entities.get(i), width, height, roomID, roomName));
		}
	}

}
