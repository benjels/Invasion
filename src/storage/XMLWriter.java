package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import gamelogic.CardinalDirection;
import gamelogic.GameWorldTimeClockThread;
import gamelogic.RoomState;
import gamelogic.WorldGameState;
import gamelogic.entities.Coin;
import gamelogic.entities.GameEntity;
import gamelogic.entities.IndependentActor;
import gamelogic.entities.KeyCard;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.NightVisionGoggles;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Pylon;
import gamelogic.entities.SmallCarrier;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.InteriorStandardTile;

public class XMLWriter {
		
	public void saveState(){
		
		WorldGameState state = createGame();
		
		try {
			OutputStream out = new FileOutputStream(new File("test.xml"));
			
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
				xmlstreamWriter.writeCharacters(" " + r.getId()+ " ");
				xmlstreamWriter.writeCharacters(r.isDark() + " ");
				
				GameRoomTile[][] tiles = r.getTiles();
				GameEntity[][] entities = r.getEntities();
				
				//Save all of the tiles along with their type/class and coordinates 
				for (int i = 0; i < tiles.length; i++){
					for (int j = 0; j < tiles[i].length; j++){
						xmlstreamWriter.writeStartElement("", "tile", "");
						
						xmlstreamWriter.writeCharacters(tiles[i][j].toString()); //write type of tile
						xmlstreamWriter.writeCharacters(" " + i + " " + j); //write coordinates of tile
						
						xmlstreamWriter.writeEndElement();
					}
				}
				
				//Save all of the entities along with their type/class and coordinates
				for (int i = 0; i < entities.length; i++){
					for (int j = 0; j < entities[i].length; j++){
						xmlstreamWriter.writeStartElement("", "entity", "");
						
						xmlstreamWriter.writeCharacters(entities[i][j].toString()); //write type of entity
						xmlstreamWriter.writeCharacters(" " + i + " " + j); //write coordinates of entity
						
						xmlstreamWriter.writeEndElement();					
					}
				}
				
				//Save all of the MovableEntities
				HashMap<Integer, MovableEntity> worldStateMovableEntities = state.getMovableEntites();
				
				ArrayList<MovableEntity> movableEntites = new ArrayList<MovableEntity>();
				movableEntites.addAll(worldStateMovableEntities.values());
				
				
				
				
				//Write end of room element
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

	///WE ARE ADDING IN A WHOLE LOT OF THE GAME ENTITIES INTO THE SPAWN ROOM SO THAT WE DONT HAVE TO WALK AROUND THE MAP TO TEST SHIT
	//add the key card ent


	dummyEntities[4][9] = new MediumCarrier(CardinalDirection.NORTH);
	dummyEntities[4][11] = new SmallCarrier(CardinalDirection.NORTH);
	dummyEntities[4][3] = new KeyCard(0, CardinalDirection.NORTH);
	
	//CREATE THE ENEMIES FOR THE SERVER would prob be done in the actual server constructor
	HashMap<Integer, IndependentActor> enemyMapSet = new HashMap<>();
	enemyMapSet.put(10, new IndependentActor(CardinalDirection.NORTH, 10));




//add the night vision goggles
	dummyEntities[10][15] = new NightVisionGoggles(CardinalDirection.NORTH);



//add some coins tbh
dummyEntities[10][5] = new Coin(CardinalDirection.NORTH);
dummyEntities[10][7] = new Coin(CardinalDirection.NORTH);
dummyEntities[10][9] = new Coin(CardinalDirection.NORTH);
dummyEntities[10][11] = new Coin(CardinalDirection.NORTH);

//add a pylon
dummyEntities[11][11] = new Pylon(CardinalDirection.NORTH);
	
	
	
	RoomState pylonRoom0 = new RoomState(dummyTiles, dummyEntities, width, height, 0, false, "upper pylon room");
	

	



	
	
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
			RoomState pylonRoom1 = new RoomState(dummyTiles, dummyEntities, width, height, 1, false, "bottom pylon room");
			
		
	
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
			RoomState mazeRoom2 = new RoomState(dummyTiles, dummyEntities, width, height, 2, false, "right top maze");
			
			
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
			RoomState mazeRoom3 = new RoomState(dummyTiles, dummyEntities, width, height, 3, false, "right bottom maze");
			
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
			RoomState mazeRoom4 = new RoomState(dummyTiles, dummyEntities, width, height, 4, true, "left bottom maze");
			
			
			
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
			RoomState mazeRoom5 = new RoomState(dummyTiles, dummyEntities, width, height, 5, true, "left top maze");
	
	
	
			//LINK THE ROOMS TOGETHER WITH SOME TELEPORTERS//
			

			//spawn some teleporters IN THE ROOMS
			pylonRoom0.spawnTeleporter(CardinalDirection.NORTH, 21, 21, 1, 1, mazeRoom2);// pylon0 -> maze2
			mazeRoom2.spawnTeleporter(CardinalDirection.NORTH, 21, 21, 21, 1, mazeRoom3); //mazeroom2 -> mazeroom3
			mazeRoom3.spawnTeleporter(CardinalDirection.NORTH, 1, 21, 21, 1, pylonRoom1); //mazeroom3 -> pylonroom1
			pylonRoom1.spawnTeleporter(CardinalDirection.NORTH, 1, 1, 21, 21, mazeRoom4); //pylon1 -> maze4
			mazeRoom4.spawnTeleporter(CardinalDirection.NORTH, 1, 1, 1, 21, mazeRoom5); //maze4 ->maze5
			mazeRoom5.spawnTeleporter(CardinalDirection.NORTH, 21, 1, 1, 21, pylonRoom0); //maze5 ->pylon0




	//create the rooms collection which we will be giving to the world game state

			HashMap<Integer, RoomState> rooms = new HashMap<>();
			rooms.put(0, pylonRoom0);
			rooms.put(1, pylonRoom1);
			rooms.put(2, mazeRoom2);
			rooms.put(3, mazeRoom3);
			rooms.put(4, mazeRoom4);
			rooms.put(5, mazeRoom5);
		
			
			
			
			////////////////////////////////////////////////
	




	//CREATE THE WORLD GAME STATE FROM THE ROOMS WE MADE
	GameWorldTimeClockThread realClock = new GameWorldTimeClockThread();
	WorldGameState initialState = new WorldGameState(rooms, realClock);//this initial state would be read in from an xml file (basically just rooms i think)
	
	return initialState;
	}
}
