package storage;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.FighterPlayerStrategy;
import gamelogic.LockedTeleporter;
import gamelogic.PylonRoomState;
import gamelogic.RoomState;
import gamelogic.SorcererPlayerStrategy;
import gamelogic.StandardTeleporter;
import gamelogic.WorldGameState;
import gamelogic.entities.Coin;
import gamelogic.entities.GameEntity;
import gamelogic.entities.Gun;
import gamelogic.entities.HealthKit;
import gamelogic.entities.KeyCard;
import gamelogic.entities.MazeWall;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.NightVisionGoggles;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Player;
import gamelogic.entities.Pylon;
import gamelogic.entities.SmallCarrier;
import gamelogic.entities.TeleporterGun;
import gamelogic.entities.Treasure;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.HarmfulTile;
import gamelogic.tiles.InteriorStandardTile;
/**
 * Class to load in a WorldGameState from an xml file
 * @author gomezjosh
 *
 */
public class XMLParser {

	private GameRoomTile[][] tiles;
	private String[] roomProperties;
	private HashMap<Integer, RoomState> rooms = new HashMap<Integer, RoomState>();
	private ArrayList<Pylon> pylons = new ArrayList<Pylon>();
	private ArrayList<GameEntity[][]> roomEntities = new ArrayList<GameEntity[][]>();
	private ArrayList<Player> players = new ArrayList<Player>();

	private int score;

	public WorldGameState parse(File entityFile){
		rooms =  parseTiles(new File("Standard-Tiles.xml"));
		parseEntities(entityFile);
		addEntities();
		WorldGameState game = new WorldGameState(rooms,pylons.get(0), pylons.get(1));

		game.setScore(score);

		return game;
	}
	/**
	 * Used for when the loading main needs to place in all of the players because it can only be done in there
	 * due to this class not having access to the server
	 * @return
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}

	public HashMap<Integer,RoomState> parseTiles(File file) {

		HashMap<Integer,RoomState> rooms = new HashMap<Integer,RoomState>();

		try {
			InputStream in = new FileInputStream(file);
			XMLEventReader xmlreader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);

			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elemName = startElement.getName().getLocalPart();

					if (elemName.equals("worldState")) {
						event = xmlreader.nextEvent();
						score = Integer.parseInt(event.asCharacters().getData().substring(0, 1));
						continue;
					}
					if (elemName.equals("rooms")) {
						continue;
					}

					if (elemName.equals("room")) {

						event = xmlreader.nextEvent();
						roomProperties = event.asCharacters().getData().split("-");

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
						continue;
					}
					if (elemName.equals("rooms")) {
						continue;
					}
					if (elemName.equals("room")) {
						//create a new RoomState
						int id = Integer.parseInt(roomProperties[0]);
						int width = Integer.parseInt(roomProperties[1]);
						int height = Integer.parseInt(roomProperties[2]);
						boolean isDark = Boolean.getBoolean(roomProperties[3]);
						String description = roomProperties[4];
						String type = roomProperties[5];
						RoomState room;
						if (type.contains("PylonRoomState")){
							room = new PylonRoomState(tiles, width, height, id, isDark, description);
						}
						else {
							room = new RoomState(tiles, width, height, id, isDark, description);
						}


						rooms.put(id, room);//add it to the rooms Map

					}
				}

				}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

		return rooms;
	}

	/**
	 * Must call the parseTiles first so that the RoomStates are all created and then it can add the entites to each RoomState
	 * @param takes a file that it will load the entities from
	 */

	public void parseEntities(File file) {
		try {
			InputStream in = new FileInputStream(file);
			XMLEventReader xmlreader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);

			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elemName = startElement.getName().getLocalPart();

					if (elemName.equals("worldState")) {
						continue;
					}
					if (elemName.equals("rooms")) {
						continue;
					}

					if (elemName.equals("room")) {
						event = xmlreader.nextEvent();
						String [] currentRoomProperties = event.asCharacters().getData().split("-");


						int width = Integer.parseInt(currentRoomProperties[1]);
						int height = Integer.parseInt(currentRoomProperties[2]);

						//create a new array for the tiles the will be added to the currentRoom
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


									event = xmlreader.nextTag(); //goes to the end tile tag </entity>
								}

							event = xmlreader.nextTag(); //goes to the new start tile tag OR end room tag in which case the while loop will end
						}
						roomEntities.add(entities);
					}

				}

				if (event.isEndElement()){
					EndElement end = event.asEndElement();
					String elemName = end.getName().getLocalPart();

					if (elemName.equals("worldState")) {
						continue;
					}
					if (elemName.equals("rooms")) {
						continue;
					}
					if (elemName.equals("room")) {
						//RoomStates are already created so nothing to do here
					}
					if (elemName.equals("players")){
						continue;
					}
					if (elemName.equals("player")){
						continue;
					}
				}

			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

	}
	/**
	 * Looking at the type of the entity from the array of strings this method determines what type of entity the object is and creates a new entity and returns it
	 * @param properties. Needs to send the whole array because different entities have different amounts of fields to save and use in their constructors
	 * @return the new entity that will be assigned to the GameEntity[][]
	 */
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

		case "NightVision_Goggles":
			return new NightVisionGoggles(dir);

		case "HealthKit":
			return new HealthKit(dir);

		case "Coin":
			return new Coin(dir);

		case "Player":
			players.add(createPlayer(properties, dir));
			return new NullEntity(dir);
			//return createPlayer(properties, dir);

		case "Pylon":
			Pylon p = new Pylon(dir);
			pylons.add(p);	//need to save pylons for the WorldGameState Constructor
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
			int xLocator = Integer.parseInt(properties[7]);
			int yLocator = Integer.parseInt(properties[8]);
			return new StandardTeleporter(xLocator, yLocator, dir, xDestination, yDestination, rooms.get(roomStateID));

		case "Treasure":
			return new Treasure(dir);
		}
		//If it doesn't go into any cases in the switch statement then I don't know what Entity it is meant to be so we return a Null_Entity
		return new NullEntity(dir);
	}

	/**
	 *
	 * @param type: Takes the type property from reading the xml file that is the type of tile it will be.
	 * @return GameRoomTile correct tile depending on what was read from the xml file
	 */
	//No longer really needed because we only use InteriorStandardTiles now. Will keep it in here for if at somestage we include
	//different types of tiles
	public GameRoomTile parseTile(String type){
		switch (type){
		case "Interior_Tile":
			return new InteriorStandardTile();
		case "Harmful_Tile":
			return new HarmfulTile();
		}

		return new InteriorStandardTile(); //to make it compile even though all possible cases are covered in switch statement

	}

	/**
	 * After the Roomstates are all made without the entities and the entities are loaded then you add
	 * each entity to its corresponding Room using the id.
	 */
	public void addEntities(){
		List<RoomState> roomTiles = new ArrayList<RoomState> (rooms.values());
		for (int i = 0; i < roomTiles.size(); i++){
			roomTiles.get(i).setEntities(roomEntities.get(i));
			rooms.put(i, roomTiles.get(i));
		}
	}

	/**
	 * Creates a player using the string[] of properties from the parser
	 * @param Takes the String[] read from the xml file between the player tags
	 * @param dir: direction player is facing
	 * @return new player read from the save file
	 */
	public Player createPlayer(String[] properties, CardinalDirection dir){
		String irlName = properties[4];
		int uniqueID = Integer.parseInt(properties[5]);
		int healthPercentage = Integer.parseInt(properties[6]);
		int coins = Integer.parseInt(properties[7]);
		CharacterStrategy strat = null;
		if (properties[8].equals("Tank_Strategy")){
			strat = new FighterPlayerStrategy();
		}
		else{
			strat = new SorcererPlayerStrategy(); //character strategy must be Sorcerer_Strategy
		}
		boolean hasNightVision = Boolean.parseBoolean(properties[9]);
		boolean hasKey = Boolean.parseBoolean(properties[10]);
		boolean hasGun = Boolean.parseBoolean(properties[11]);
		boolean hasTeleGun = Boolean.parseBoolean(properties[12]);
		int healthKitAmount = Integer.parseInt(properties[13]);
		int roomID = Integer.parseInt(properties[14]);

		Player player = new Player (irlName, uniqueID, strat, dir, rooms.get(roomID));
		player.setHealthPercentage(healthPercentage);
		player.setCoins(coins);
		player.setNightVision(hasNightVision);
		player.setKeyEnabled(hasKey);
		player.setHasGun(hasGun);
		player.setHasTeleGun(hasTeleGun);
		player.setHealthKit(healthKitAmount);

		return player;
	}

}
