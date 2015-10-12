package storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;





import ui.GameGui;
import ui.GameSetUpWindow;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import control.DummySlave;
import control.Listener;
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
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.HarmfulTile;
import gamelogic.tiles.InteriorStandardTile;
import graphics.GameCanvas;

public class XMLWriter {
	
	public void saveState(File entitiesFile, File tilesFile ){
		saveEntites(entitiesFile);
		saveTiles(tilesFile);
		
		System.exit(0); //Not sure if needed but the main seems to continue running even after saving
	}
		
	public void saveEntites(File file){
		
		WorldGameState state = createGame();
		
		try {
			OutputStream out = new FileOutputStream(file);
			
			XMLStreamWriter xmlstreamWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(out)); //change between 'out' and System.out for debugging
			
			xmlstreamWriter.writeDTD("");
			
			xmlstreamWriter.writeStartElement("", "worldState","");			
			
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
				xmlstreamWriter.writeCharacters(r.getDescription());
				
				GameEntity[][] entities = r.getEntities();
				
				//Save all of the entities along with their type/class and coordinates
				for (int i = 0; i < entities.length; i++){
					for (int j = 0; j < entities[i].length; j++){
						xmlstreamWriter.writeStartElement("", "entity", "");
						
						xmlstreamWriter.writeCharacters(entities[i][j].toXMLString() + "-"); //write type of entity
						xmlstreamWriter.writeCharacters("" + i + "-" + j + "-"); //write coordinates of entity
						xmlstreamWriter.writeCharacters("" + entities[i][j].getFacingCardinalDirection());
						
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
						xmlstreamWriter.writeCharacters(""+ player.getFacingCardinalDirection() + "-");
						
						xmlstreamWriter.writeCharacters(" " + player.getxInRoom() + " " + player.getyInRoom());
						
						
						//Save the players Inventory
						Carrier inventory = player.getCurrentInventory();
						xmlstreamWriter.writeStartElement("", "inventory", "");
						xmlstreamWriter.writeCharacters(" " + inventory.getCapacity() + " ");
						xmlstreamWriter.writeCharacters(" " + inventory.getFacingCardinalDirection() + " ");
						
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
public void saveTiles(File file){
		
		WorldGameState state = createGame();
		
		try {
			OutputStream out = new FileOutputStream(file);
			
			XMLStreamWriter xmlstreamWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(out)); //change between 'out' and System.out for debugging
			
			xmlstreamWriter.writeDTD("");
			
			xmlstreamWriter.writeStartElement("", "worldState","");			
			
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
				xmlstreamWriter.writeCharacters(r.getDescription());
				
				GameRoomTile[][] tiles = r.getTiles();
				
				//Save all of the tiles along with their type/class and coordinates 
				for (int i = 0; i < tiles.length; i++){
					for (int j = 0; j < tiles[i].length; j++){
						xmlstreamWriter.writeStartElement("", "tile", "");
						
						xmlstreamWriter.writeCharacters(tiles[i][j].toXMLString() + "-"); //write type of tile
						xmlstreamWriter.writeCharacters(i + "-" + j); //write coordinates of tile
						
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


				dummyEntities[4][9] = new MediumCarrier(CardinalDirection.NORTH);
				dummyEntities[4][11] = new SmallCarrier(CardinalDirection.NORTH);
				dummyEntities[4][3] = new KeyCard(CardinalDirection.NORTH);
				
				
				//add the gun
				dummyEntities[5][3] = new Gun(CardinalDirection.NORTH); 
				
				//add the tele gun
				dummyEntities[6][3] = new TeleporterGun(CardinalDirection.NORTH);

				//CREATE THE ENEMIES FOR THE SERVER would prob be done in the actual server constructor
			
				///////////////////////////////////////////////////////////////////////////////////////
				


				//add the night vision goggles
				dummyEntities[10][15] = new NightVisionGoggles(CardinalDirection.NORTH);



		//add some coins tbh
		dummyEntities[10][5] = new Coin(CardinalDirection.NORTH);
		dummyEntities[10][7] = new Coin(CardinalDirection.NORTH);
		dummyEntities[10][9] = new Coin(CardinalDirection.NORTH);
		dummyEntities[10][11] = new Coin(CardinalDirection.NORTH);

		//add a pylon
		Pylon topPylon = new Pylon(CardinalDirection.NORTH);
		dummyEntities[11][11] = topPylon;


		//add some maze walls /impassable colomn
		dummyEntities[5][15] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[6][15] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[7][15] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[9][15] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[10][15] = new MazeWall(CardinalDirection.NORTH);

				RoomState pylonRoom0 = new PylonRoomState(dummyTiles, dummyEntities, width, height, 0, "upper pylon room");








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
						dummyEntities[11][11] = topPylon;
						
						
						RoomState pylonRoom1 = new PylonRoomState(dummyTiles, dummyEntities, width, height, 1, "bottom pylon room");

			



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
						

						
						RoomState mazeRoom2 = new RoomState(dummyTiles, dummyEntities, width, height, 2, "right top maze");

					


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
						RoomState mazeRoom3 = new RoomState(dummyTiles, dummyEntities, width, height, 3, "right bottom maze");

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

						//fill in the corners with null entities for drawing
						dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
						dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);



						RoomState mazeRoom5 = new RoomState(dummyTiles, dummyEntities, width, height, 5,"left top maze");
		//create room 6 secret room


						width = 10;
						height = 10;


						dummyTiles = new GameRoomTile[width][height];
						dummyEntities = new GameEntity[width][height];
						//fill up tha tiles
						for(int i = 0; i < width ; i++){
							for(int j = 0; j < height ; j++){
								dummyTiles[i][j] = new HarmfulTile();
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
						
						RoomState secretRoom = new RoomState(dummyTiles, dummyEntities, width, height, 6,"left top maze");

						
						
						
						//LINK THE ROOMS TOGETHER WITH SOME TELEPORTERS//


						//spawn some teleporters IN THE ROOMS
						pylonRoom0.spawnStandardTeleporter(CardinalDirection.NORTH, 21, 21, 1, 1, mazeRoom2);// pylon0 -> maze2
						mazeRoom2.spawnStandardTeleporter(CardinalDirection.NORTH, 21, 21, 21, 1, mazeRoom3); //mazeroom2 -> mazeroom3
						mazeRoom3.spawnStandardTeleporter(CardinalDirection.NORTH, 1, 21, 21, 1, pylonRoom1); //mazeroom3 -> pylonroom1
						pylonRoom1.spawnStandardTeleporter(CardinalDirection.NORTH, 1, 1, 21, 21, mazeRoom4); //pylon1 -> maze4
						mazeRoom4.spawnStandardTeleporter(CardinalDirection.NORTH, 1, 1, 1, 21, mazeRoom5); //maze4 ->maze5
						mazeRoom5.spawnStandardTeleporter(CardinalDirection.NORTH, 21, 1, 1, 21, pylonRoom0); //maze5 ->pylon0
						//add the locked tele
						pylonRoom0.spawnLockedTeleporter(CardinalDirection.NORTH, 15, 18, 5, 4, secretRoom);// pylon0 -> secret
						secretRoom.spawnLockedTeleporter(CardinalDirection.NORTH, 5, 5, 15, 17, pylonRoom0);// secret -> pylon0



				//create the rooms collection which we will be giving to the world game state

						HashMap<Integer, RoomState> rooms = new HashMap<>();
						rooms.put(0, pylonRoom0); //NOTE THAT THE IDS OF PYLON ROOMS NEED TO STAY AS 0 AND 1 BECAUSE THESE IDS ARE REFERENCED IN THE INDEPENDENT ACTOR MANAGER WHEN SPAWNING PYLON ATTACKERS
						rooms.put(1, pylonRoom1);//PROB JST EASIEST TO NOT CHANGE THESE ROOM IDS AT ALL
						rooms.put(2, mazeRoom2);
						rooms.put(3, mazeRoom3);
						rooms.put(4, mazeRoom4);
						rooms.put(5, mazeRoom5);
						rooms.put(6, secretRoom);




						////////////////////////////////////////////////





				//CREATE THE WORLD GAME STATE FROM THE ROOMS WE MADE
						
				WorldGameState initialState = new WorldGameState(rooms, topPylon, bottomPylon);//this initial state would be read in from an xml file (basically just rooms i think)
				GameWorldTimeClockThread realClock = new GameWorldTimeClockThread(initialState);

				IndependentActorManager enemyManager = new IndependentActorManager(initialState); //incredibly important that ids for zombies will not conflict with ids from players as they both share the MovableEntity map in the worldgamestate object.



				//CREATE SERVER FROM THE GAME STATE WE MADE
				Server theServer = new Server(initialState, enemyManager); //this init state will be read in from xml or json or watev


		//PPLLAAYYEERR''SS SSHHIITT.

			//CREATE A PLAYER AND ADD IT TO THE SERVER
				Player myPlayer = new Player("JOHN CENA", 0, new FighterPlayerStrategy(), CardinalDirection.NORTH); //name, uid, spawnroom SETTING THE PLAYER TO FACE NORTH
			//	todo:
			/*		1)make sure in set up that all the movable entities being added to the worldgamestate and having internal fields set and placed in the map in there
					1.5) review consistency of ids used. should use 10->20 range for ais. use 1 and 2 for players u fucked up using 0
					2) test all this ai shit out fam
					3)implement one of the easy af game object like key or light or s/t
					4)nwen fam*/

				//add the player to the game state. the enemies are registered via the actor manager
				theServer.registerPlayerWithGameState(myPlayer); //!!! atm this method has hardcoded which room it inserts the player in yeah

	return initialState;
	}
}
