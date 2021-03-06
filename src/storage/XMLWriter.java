package storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;








import ui.GameGui;


import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import control.DummySlave;
import control.Controller;
import gamelogic.CardinalDirection;
import gamelogic.ClockThread;
import gamelogic.GameWorldTimeClockThread;
import gamelogic.IndependentActor;
import gamelogic.IndependentActorManager;
import gamelogic.PylonRoomState;
import gamelogic.RoomState;
import gamelogic.Server;
import gamelogic.FighterPlayerStrategy;
import gamelogic.WorldGameState;
import gamelogic.entities.Carrier;
import gamelogic.entities.Carryable;
import gamelogic.entities.Coin;
import gamelogic.entities.GameEntity;
import gamelogic.entities.Gun;
import gamelogic.entities.HealthKit;
import gamelogic.entities.KeyCard;
import gamelogic.entities.MazeWall;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.MovableEntity;
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
import graphics.GameCanvas;
/**
 * Class to save an already existing WorldGameState to an xml file.
 * This class was also used to create an xml fiel of the hardcoded worldgame state using the commented out creategame method.
 * @author gomezjosh
 *
 */
public class XMLWriter {

	public void saveState(File file, WorldGameState state){

		try {
			OutputStream out = new FileOutputStream(file);

			XMLStreamWriter xmlstreamWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(out)); //change between 'out' and System.out for debugging

			xmlstreamWriter.writeDTD("");

			xmlstreamWriter.writeStartElement("", "worldState","");
			xmlstreamWriter.writeCharacters("" + state.getScore());

			HashMap<Integer, RoomState> WorldGamerooms = state.getRooms();
			//Saves all of the rooms including their tiles and entities on top of them

			ArrayList<RoomState> ListofRooms = new ArrayList<RoomState>();
			ListofRooms.addAll(WorldGamerooms.values());

			//save all rooms
			xmlstreamWriter.writeStartElement("","rooms","");

			//Iterate through all rooms and write out all of the rooms content
			for (RoomState r: ListofRooms){
				xmlstreamWriter.writeStartElement("","room","");
				xmlstreamWriter.writeCharacters(r.getId()+ "-");
				xmlstreamWriter.writeCharacters(r.getRoomWidth() + "-");
				xmlstreamWriter.writeCharacters(r.getRoomHeight() + "-");
				xmlstreamWriter.writeCharacters(r.isDark() + "-");
				xmlstreamWriter.writeCharacters(r.getDescription()+ "-");
				xmlstreamWriter.writeCharacters(r.getClass().getSimpleName());

				GameEntity[][] entities = r.getEntities();

				//Save all of the entities along with their type/class and coordinates
				for (int i = 0; i < entities.length; i++){
					for (int j = 0; j < entities[i].length; j++){
						xmlstreamWriter.writeStartElement("", "entity", "");

						xmlstreamWriter.writeCharacters(i + "-" + j + "-"); //write coordinates of entity
						xmlstreamWriter.writeCharacters("" + entities[i][j].getFacingCardinalDirection() + "-");
						xmlstreamWriter.writeCharacters("" + entities[i][j].toXMLString()); //write type of entity

						xmlstreamWriter.writeEndElement();
					}
				}
				//Write end of room
				xmlstreamWriter.writeEndElement();

			}

			//Save all of the MovableEntities
			HashMap<Integer, MovableEntity> worldStateMovableEntities = state.getMovableEntites();

				ArrayList<MovableEntity> movableEntites = new ArrayList<MovableEntity>();
				movableEntites.addAll(worldStateMovableEntities.values());

				xmlstreamWriter.writeStartElement("", "players", "");
				for (MovableEntity m : movableEntites){
					if (m instanceof Player){
						Player player = (Player)m;
						xmlstreamWriter.writeStartElement("", "player", "");

						xmlstreamWriter.writeCharacters("" + player.getIrlName() + "-");
						xmlstreamWriter.writeCharacters("" + player.getHealthPercentage() + "-");
						xmlstreamWriter.writeCharacters("" + player.getCoins() + "-");
						xmlstreamWriter.writeCharacters("" + player.getCharacter() + "-");
						xmlstreamWriter.writeCharacters("" + player.hasNightVisionEnabled() + "-");
						xmlstreamWriter.writeCharacters("" + player.hasKeyEnabled() + "-");
						xmlstreamWriter.writeCharacters("" + player.hasGun() + "-");
						xmlstreamWriter.writeCharacters("" + player.hasTeleGun() + "-");
						xmlstreamWriter.writeCharacters("" + player.getHealthKitsAmount() + "-");
						xmlstreamWriter.writeCharacters("" + player.getCurrentRoom().getId()+ "-");
						xmlstreamWriter.writeCharacters(""+ player.getFacingCardinalDirection() + "-");

						xmlstreamWriter.writeCharacters(" " + player.getxInRoom() + " " + player.getyInRoom());


						//Save the players Inventory
						Carrier inventory = player.getCurrentInventory();
						xmlstreamWriter.writeStartElement("", "inventory", "");
						xmlstreamWriter.writeCharacters(" " + inventory.getCapacity() + "-");
						xmlstreamWriter.writeCharacters("" + inventory.getFacingCardinalDirection());

						ArrayList<Carryable> allItems = inventory.getItems();
						for (Carryable c : allItems){
							xmlstreamWriter.writeStartElement("", "item", "");
							xmlstreamWriter.writeCharacters(c.toXMLString());

							xmlstreamWriter.writeEndElement();
						}

						//End of Inventory
						xmlstreamWriter.writeEndElement();

						//End of Player
						xmlstreamWriter.writeEndElement();
					}
				}

			//write end of rooms element
			xmlstreamWriter.writeEndElement();

			//write end of world state element
			xmlstreamWriter.writeEndElement();

			//write end of document
			xmlstreamWriter.writeEndDocument();

			xmlstreamWriter.close();


		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}


	}
	/**
	 * Method that is only used to create an xml file of the standard tiles
	 * @param file
	 * @param state
	 */
public void saveTiles(File file, WorldGameState state){

		try {
			OutputStream out = new FileOutputStream(file);

			XMLStreamWriter xmlstreamWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(out)); //change between 'out' and System.out for debugging

			xmlstreamWriter.writeDTD("");

			xmlstreamWriter.writeStartElement("", "worldState","");
			xmlstreamWriter.writeCharacters("" + state.getScore());

			HashMap<Integer, RoomState> WorldGamerooms = state.getRooms();
			//Saves all of the rooms including their tiles and entities on top of them

			ArrayList<RoomState> ListofRooms = new ArrayList<RoomState>();
			ListofRooms.addAll(WorldGamerooms.values());

			//save all rooms
			xmlstreamWriter.writeStartElement("","rooms","");

			//Iterate through all rooms and write out all of the rooms content
			for (RoomState r: ListofRooms){
				xmlstreamWriter.writeStartElement("","room","");
				xmlstreamWriter.writeCharacters(r.getId()+ "-");
				xmlstreamWriter.writeCharacters(r.getRoomWidth() + "-");
				xmlstreamWriter.writeCharacters(r.getRoomHeight() + "-");
				xmlstreamWriter.writeCharacters(r.isDark() + "-");
				xmlstreamWriter.writeCharacters(r.getDescription() +"-");
				xmlstreamWriter.writeCharacters(r.getClass().getSimpleName());

				GameRoomTile[][] tiles = r.getTiles();

				//Save all of the tiles along with their type/class and coordinates
				for (int i = 0; i < tiles.length; i++){
					for (int j = 0; j < tiles[i].length; j++){
						xmlstreamWriter.writeStartElement("", "tile", "");

						xmlstreamWriter.writeCharacters(i + "-" + j + "-"); //write coordinates of tile
						xmlstreamWriter.writeCharacters(tiles[i][j].toXMLString()); //write type of tile

						xmlstreamWriter.writeEndElement();
					}
				}
				//Write end of room
				xmlstreamWriter.writeEndElement();

			}
			//write end of rooms element
			xmlstreamWriter.writeEndElement();

			//write end of world state element
			xmlstreamWriter.writeEndElement();

			//write end of document
			xmlstreamWriter.writeEndDocument();

			xmlstreamWriter.close();


		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}

	}
/**
 * This method is a hardcoded version of the game that we create and use to save that hardcoded version into the Standard-Entites.xml file
 * Was only used for creating new maps
 */
/*
	public WorldGameState createGame(){

		//create pylon room 0 (also the spawn room) which still has a whole lot of entities spawned in it for testing purposes
				int width = 23;
				int height = 23;


				GameRoomTile[][] dummyTiles = new GameRoomTile[width][height];
				GameEntity[][] dummyEntities = new GameEntity[width][height];
				//fill up tha tiles
				for(int i = 0; i < width ; i++){
					for(int j = 0; j < height ; j++){
						dummyTiles[i][j] = new InteriorStandardTile();
					}
				}
				//fill up the entities with null
				for(int i = 0; i < width; i++){
					for(int j = 0; j < height ; j++){
						dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
					}
				}
				//add the walls to edge locations
				for(int i = 0; i < width; i++){
					for(int j = 0; j < height; j++){
						//insert walls at the top and bottom
						//insert walls at the top and bottom
						if( i == 0   ){//walls on right with standard orientation
							dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
						}
						if(i == width - 1){//walls on the left with standard orientation
							dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
						}
						if(j == 0 ){//walls up top
							dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
						}
						if(j == height - 1){//walls at bottom
							dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
						}
					}
				}


				//fill in the corners with null entities for drawing
				dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
				dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
				dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
				dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);


				///WE ARE ADDING IN A WHOLE LOT OF THE GAME ENTITIES INTO THE SPAWN ROOM SO THAT WE DONT HAVE TO WALK AROUND THE MAP TO TEST SHIT
				//add the key card ent







				//add the gun
				dummyEntities[18][15] = new Gun(CardinalDirection.NORTH);

				//add the tele gun
				dummyEntities[18][16] = new TeleporterGun(CardinalDirection.NORTH);

				//add the night vision goggles
				dummyEntities[18][14] = new NightVisionGoggles(CardinalDirection.NORTH);



				//CREATE THE ENEMIES FOR THE SERVER would prob be done in the actual server constructor

				///////////////////////////////////////////////////////////////////////////////////////






		//add some coins tbh
		dummyEntities[10][5] = new Coin(CardinalDirection.NORTH);
		dummyEntities[10][7] = new Coin(CardinalDirection.NORTH);
		dummyEntities[10][9] = new Coin(CardinalDirection.NORTH);
		dummyEntities[10][11] = new Coin(CardinalDirection.NORTH);

		//add a health kit
		dummyEntities[8][2] = new HealthKit(CardinalDirection.NORTH);

		//add a pylon
		Pylon topPylon = new Pylon(CardinalDirection.NORTH);
		dummyEntities[11][11] = topPylon;


		//add some maze walls /impassable colomn
		//below

		dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

		dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[13][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);




		//above

		dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[9][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

		dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[14][7] = new MazeWall(CardinalDirection.NORTH);


		//left
		dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][9] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

		dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);



		//right

		dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][9] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

		dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);









				PylonRoomState pylonRoom0 = new PylonRoomState(dummyTiles, dummyEntities, width, height, 0, "upper pylon room");








				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





				//create pylon room 1
						width = 23;
						height = 23;


						dummyTiles = new GameRoomTile[width][height];
						dummyEntities = new GameEntity[width][height];
						//fill up tha tiles
						for(int i = 0; i < width ; i++){
							for(int j = 0; j < height ; j++){
								dummyTiles[i][j] = new InteriorStandardTile();
							}
						}
						//fill up the entities with null
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height ; j++){
								dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
							}
						}
						//add the walls to edge locations
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height; j++){
								//insert walls at the top and bottom
								//insert walls at the top and bottom
								if( i == 0   ){//walls on right with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
								}
								if(i == width - 1){//walls on the left with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
								}
								if(j == 0 ){//walls up top
									dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
								}
								if(j == height - 1){//walls at bottom
									dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
								}
							}
						}

						//fill in the corners with null entities for drawing
						dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);


						//add a pylon
						Pylon bottomPylon = new Pylon(CardinalDirection.NORTH);
						dummyEntities[11][11] = bottomPylon;



						//add some maze walls /impassable colomn
						//below

						dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);




						//above

						dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][7] = new MazeWall(CardinalDirection.NORTH);


						//left
						dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][9] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);



						//right

						dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);



						PylonRoomState pylonRoom1 = new PylonRoomState(dummyTiles, dummyEntities, width, height, 5, "bottom pylon room");





				//create maze room 2

						width = 23;
						height = 23;


						dummyTiles = new GameRoomTile[width][height];
						dummyEntities = new GameEntity[width][height];
						//fill up tha tiles
						for(int i = 0; i < width ; i++){
							for(int j = 0; j < height ; j++){
								dummyTiles[i][j] = new InteriorStandardTile();
							}
						}
						//fill up the entities with null
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height ; j++){
								dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
							}
						}
						//add the walls to edge locations
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height; j++){
								//insert walls at the top and bottom
								//insert walls at the top and bottom
								if( i == 0   ){//walls on right with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
								}
								if(i == width - 1){//walls on the left with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
								}
								if(j == 0 ){//walls up top
									dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
								}
								if(j == height - 1){//walls at bottom
									dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
								}
							}
						}
						//fill in the corners with null entities for drawing
						dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);



						//center walls
						dummyEntities[11][11] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[11][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[11][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[11][14] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[11][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[11][9] = new MazeWall(CardinalDirection.EAST);

						//add the treasure
						dummyEntities[12][12] = new Treasure(CardinalDirection.NORTH);

						dummyEntities[4][9] = new MediumCarrier(CardinalDirection.NORTH);


						//add a health kit
						dummyEntities[8][2] = new HealthKit(CardinalDirection.NORTH);
						//add a health kit
						dummyEntities[18][7] = new HealthKit(CardinalDirection.NORTH);
						//add a health kit
						dummyEntities[20][2] = new HealthKit(CardinalDirection.NORTH);
						//add a health kit
						dummyEntities[5][11] = new HealthKit(CardinalDirection.NORTH);



						dummyEntities[11][19] = new Coin(CardinalDirection.NORTH);
						dummyEntities[11][20] = new Coin(CardinalDirection.NORTH);
						dummyEntities[11][18] = new Coin(CardinalDirection.NORTH);
						dummyEntities[11][17] = new Coin(CardinalDirection.NORTH);

						//INNER LAYER
						//below

						dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);
						//above

						dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
						//left
						dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);
						//right

						dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);


						//MIDDLE LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[8][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[12][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][18] = new MazeWall(CardinalDirection.SOUTH);

						//above

						dummyEntities[8][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][5] = new MazeWall(CardinalDirection.NORTH);


						//left
						dummyEntities[5][8] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][9] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][10] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][14] = new MazeWall(CardinalDirection.WEST);


						//right
						dummyEntities[18][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][14] = new MazeWall(CardinalDirection.EAST);


						//OUTERMOST LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[6][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[15][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[16][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][20] = new MazeWall(CardinalDirection.NORTH);
						//above
						dummyEntities[6][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[15][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[16][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][3] = new MazeWall(CardinalDirection.NORTH);





						//left
						dummyEntities[3][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][14] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][15] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][16] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][12] = new MazeWall(CardinalDirection.EAST);


						//right
						dummyEntities[20][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][14] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][15] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][16] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);


						RoomState mazeRoom2 = new RoomState(dummyTiles, dummyEntities, width, height, 1, "right top maze");




				//create maze room 3

						width = 23;
						height = 23;


						dummyTiles = new GameRoomTile[width][height];
						dummyEntities = new GameEntity[width][height];
						//fill up tha tiles
						for(int i = 0; i < width ; i++){
							for(int j = 0; j < height ; j++){
								dummyTiles[i][j] = new InteriorStandardTile();
							}
						}
						//fill up the entities with null
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height ; j++){
								dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
							}
						}
						//add the walls to edge locations
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height; j++){
								//insert walls at the top and bottom
								//insert walls at the top and bottom
								if( i == 0   ){//walls on right with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
								}
								if(i == width - 1){//walls on the left with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
								}
								if(j == 0 ){//walls up top
									dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
								}
								if(j == height - 1){//walls at bottom
									dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
								}
							}
						}


						//center walls
						dummyEntities[9][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[11][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[12][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][11] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[12][12] = new Treasure(CardinalDirection.NORTH);

						dummyEntities[4][11] = new SmallCarrier(CardinalDirection.NORTH);

						//add a health kit
						dummyEntities[10][10] = new HealthKit(CardinalDirection.NORTH);
						//add a health kit
						dummyEntities[20][15] = new HealthKit(CardinalDirection.NORTH);
						//add a health kit
						dummyEntities[3][2] = new HealthKit(CardinalDirection.NORTH);
						//add a health kit
						dummyEntities[5][11] = new HealthKit(CardinalDirection.NORTH);

						dummyEntities[11][19] = new Coin(CardinalDirection.NORTH);
						dummyEntities[11][20] = new Coin(CardinalDirection.NORTH);
						dummyEntities[11][18] = new Coin(CardinalDirection.NORTH);
						dummyEntities[11][17] = new Coin(CardinalDirection.NORTH);

						//INNER LAYER
						//below

						dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);
						//above

						dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
						//left
						dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);
						//right

						dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);


						//MIDDLE LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[8][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[12][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][18] = new MazeWall(CardinalDirection.SOUTH);

						//above

						dummyEntities[8][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][5] = new MazeWall(CardinalDirection.NORTH);


						//left
						dummyEntities[5][8] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][9] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][10] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][14] = new MazeWall(CardinalDirection.WEST);


						//right
						dummyEntities[18][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][14] = new MazeWall(CardinalDirection.EAST);


						//OUTERMOST LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[6][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][20] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[15][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[16][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][20] = new MazeWall(CardinalDirection.NORTH);
						//above
						dummyEntities[6][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[16][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][3] = new MazeWall(CardinalDirection.NORTH);





						//left
						dummyEntities[3][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][10] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[3][14] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[3][16] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][12] = new MazeWall(CardinalDirection.EAST);


						//right
						dummyEntities[20][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);


						RoomState mazeRoom3 = new RoomState(dummyTiles, dummyEntities, width, height, 2, "right bottom maze");

						//fill in the corners with null entities for drawing
						dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);

					//create maze room 4 dark


						width = 23;
						height = 23;


						dummyTiles = new GameRoomTile[width][height];
						dummyEntities = new GameEntity[width][height];
						//fill up tha tiles
						for(int i = 0; i < width ; i++){
							for(int j = 0; j < height ; j++){
								dummyTiles[i][j] = new InteriorStandardTile();
							}
						}
						//fill up the entities with null
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height ; j++){
								dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
							}
						}
						//add the walls to edge locations
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height; j++){
								//insert walls at the top and bottom
								//insert walls at the top and bottom
								if( i == 0   ){//walls on right with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
								}
								if(i == width - 1){//walls on the left with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
								}
								if(j == 0 ){//walls up top
									dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
								}
								if(j == height - 1){//walls at bottom
									dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
								}
							}
						}




						dummyEntities[9][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[11][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[12][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][11] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[12][12] = new Treasure(CardinalDirection.NORTH);

						dummyEntities[4][11] = new SmallCarrier(CardinalDirection.NORTH);

						//INNER LAYER
						//below

						dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);
						//above

						dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
						//left
						dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);
						//right

						dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[4][9] = new MediumCarrier(CardinalDirection.NORTH);


						//MIDDLE LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[8][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[12][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][18] = new MazeWall(CardinalDirection.SOUTH);

						//above

						dummyEntities[8][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][5] = new MazeWall(CardinalDirection.NORTH);


						//left
						dummyEntities[5][8] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][9] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][10] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][14] = new MazeWall(CardinalDirection.WEST);


						//right
						dummyEntities[18][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][14] = new MazeWall(CardinalDirection.EAST);


						//OUTERMOST LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[6][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[16][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][20] = new MazeWall(CardinalDirection.NORTH);
						//above
						dummyEntities[6][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[15][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][3] = new MazeWall(CardinalDirection.NORTH);





						//left
						dummyEntities[3][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][14] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][15] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][16] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][12] = new MazeWall(CardinalDirection.EAST);


						//right
						dummyEntities[20][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][16] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);



						RoomState mazeRoom4 = new RoomState(dummyTiles, dummyEntities, width, height, 4,"left bottom maze");

						//fill in the corners with null entities for drawing
						dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);







				//create maze room 5 dark


						width = 23;
						height = 23;


						dummyTiles = new GameRoomTile[width][height];
						dummyEntities = new GameEntity[width][height];
						//fill up tha tiles
						for(int i = 0; i < width ; i++){
							for(int j = 0; j < height ; j++){
								dummyTiles[i][j] = new InteriorStandardTile();
							}
						}
						//fill up the entities with null
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height ; j++){
								dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
							}
						}
						//add the walls to edge locations
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height; j++){
								//insert walls at the top and bottom
								//insert walls at the top and bottom
								if( i == 0   ){//walls on right with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
								}
								if(i == width - 1){//walls on the left with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
								}
								if(j == 0 ){//walls up top
									dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
								}
								if(j == height - 1){//walls at bottom
									dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
								}
							}
						}


						dummyEntities[9][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[11][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[12][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][11] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][11] = new MazeWall(CardinalDirection.SOUTH);


						//fill in the corners with null entities for drawing
						dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);

						//INNER LAYER
						//below

						dummyEntities[9][9] = new KeyCard(CardinalDirection.NORTH);

						dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);

						dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);
						//above

						dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

						dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
						//left
						dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

						dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);
						//right

						dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

						dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);


						//MIDDLE LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[8][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[9][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[10][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[12][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[13][18] = new MazeWall(CardinalDirection.SOUTH);
						dummyEntities[14][18] = new MazeWall(CardinalDirection.SOUTH);

						//above

						dummyEntities[8][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][5] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][5] = new MazeWall(CardinalDirection.NORTH);


						//left
						dummyEntities[5][8] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][9] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][10] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][12] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][13] = new MazeWall(CardinalDirection.WEST);
						dummyEntities[5][14] = new MazeWall(CardinalDirection.WEST);


						//right
						dummyEntities[18][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[18][14] = new MazeWall(CardinalDirection.EAST);


						//OUTERMOST LAYER
						//add some maze walls /impassable colomn
						//below
						dummyEntities[6][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[16][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][20] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][20] = new MazeWall(CardinalDirection.NORTH);
						//above
						dummyEntities[6][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[7][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[8][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[9][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[10][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[12][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[13][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[15][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[17][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[18][3] = new MazeWall(CardinalDirection.NORTH);
						dummyEntities[19][3] = new MazeWall(CardinalDirection.NORTH);





						//left
						dummyEntities[3][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][14] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][15] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][16] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[3][12] = new MazeWall(CardinalDirection.EAST);


						//right
						dummyEntities[20][4] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][5] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][6] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][7] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][8] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][9] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][10] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][13] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][16] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][17] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][18] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][19] = new MazeWall(CardinalDirection.EAST);
						dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);

						RoomState mazeRoom5 = new RoomState(dummyTiles, dummyEntities, width, height, 3,"left top maze");
		//create room 6 secret room


						width = 10;
						height = 10;


						dummyTiles = new GameRoomTile[width][height];
						dummyEntities = new GameEntity[width][height];
						//fill up tha tiles
						for(int i = 0; i < width ; i++){
							for(int j = 0; j < height ; j++){
								dummyTiles[i][j] = new InteriorStandardTile();
							}
						}
						//fill up the entities with null
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height ; j++){
								dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
							}
						}
						//add the walls to edge locations
						for(int i = 0; i < width; i++){
							for(int j = 0; j < height; j++){
								//insert walls at the top and bottom
								//insert walls at the top and bottom
								if( i == 0   ){//walls on right with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
								}
								if(i == width - 1){//walls on the left with standard orientation
									dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
								}
								if(j == 0 ){//walls up top
									dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
								}
								if(j == height - 1){//walls at bottom
									dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
								}
							}
						}

						//fill in the corners with null entities for drawing
						dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);

						RoomState secretRoom = new RoomState(dummyTiles, dummyEntities, width, height, 6,"secret room");




						//LINK THE ROOMS TOGETHER WITH SOME TELEPORTERS//


						//spawn some teleporters IN THE ROOMS
						pylonRoom0.spawnStandardTeleporter(CardinalDirection.NORTH, 21, 21, 1, 1, mazeRoom2);
						mazeRoom2.spawnStandardTeleporter(CardinalDirection.NORTH, 21, 21, 21, 1, mazeRoom3);
						mazeRoom3.spawnStandardTeleporter(CardinalDirection.NORTH, 1, 21, 21, 1, pylonRoom1); //maze room 3 has id 2
						pylonRoom1.spawnStandardTeleporter(CardinalDirection.NORTH, 1, 1, 21, 21, mazeRoom4);
						mazeRoom4.spawnStandardTeleporter(CardinalDirection.NORTH, 1, 1, 1, 21, mazeRoom5);
						mazeRoom5.spawnStandardTeleporter(CardinalDirection.NORTH, 21, 1, 1, 21, pylonRoom0);
						//add the locked tele
						pylonRoom0.spawnLockedTeleporter(CardinalDirection.NORTH, 15, 18, 5, 4, secretRoom);// pylon0 -> secret
						secretRoom.spawnLockedTeleporter(CardinalDirection.NORTH, 5, 5, 15, 17, pylonRoom0);// secret -> pylon0



				//create the rooms collection which we will be giving to the world game state

						HashMap<Integer, RoomState> rooms = new HashMap<>();
						rooms.put(0, pylonRoom0); //NOTE THAT THE IDS OF PYLON ROOMS NEED TO STAY AS 0 AND 1 BECAUSE THESE IDS ARE REFERENCED IN THE INDEPENDENT ACTOR MANAGER WHEN SPAWNING PYLON ATTACKERS
						rooms.put(5, pylonRoom1);//PROB JST EASIEST TO NOT CHANGE THESE ROOM IDS AT ALL
						rooms.put(1, mazeRoom2);//these ids important for: map drawing current room, id of pylon rooms
						rooms.put(2, mazeRoom3);//in the entity manager, id of the secret room for treasure drop
						rooms.put(3, mazeRoom4);
						rooms.put(4, mazeRoom5);
						rooms.put(6, secretRoom);

		//CREATE THE WORLD GAME STATE FROM THE ROOMS WE MADE

		WorldGameState initialState = new WorldGameState(rooms, topPylon, bottomPylon);//this initial state would be read in from an xml file (basically just rooms i think)
		GameWorldTimeClockThread realClock = new GameWorldTimeClockThread(initialState);

		IndependentActorManager enemyManager = new IndependentActorManager(initialState); //incredibly important that ids for zombies will not conflict with ids from players as they both share the MovableEntity map in the worldgamestate object.



		//CREATE SERVER FROM THE GAME STATE WE MADE
		Server theServer = new Server(initialState, enemyManager); //this init state will be read in from xml or json or watev


		//CREATE A PLAYER AND ADD IT TO THE SERVER
		Player myPlayer = new Player("JOHN CENA", 0, new FighterPlayerStrategy(), CardinalDirection.NORTH, pylonRoom0); //name, uid, spawnroom SETTING THE PLAYER TO FACE NORTH

		//add the player to the map of entities
		theServer.getWorldGameState().addMovableToMap(myPlayer);

		//add the player to a room
		theServer.getWorldGameState().getRooms().get(0).attemptToPlaceEntityInRoom(myPlayer, 20, 20); //!!! atm this method has hardcoded which room it inserts the player in yeah


	return initialState;
	}
*/
}
