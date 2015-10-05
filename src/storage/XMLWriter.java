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
import gamelogic.RoomState;
import gamelogic.WorldGameState;
import gamelogic.entities.Coin;
import gamelogic.entities.GameEntity;
import gamelogic.entities.KeyCard;
import gamelogic.entities.NightVisionGoggles;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.InteriorStandardTile;

public class XMLWriter {
	
	
	public void saveState(){
		
		WorldGameState state = createGame();
		
		try {
			OutputStream out = new FileOutputStream(new File("test.xml"));
			XMLOutputFactory xmlFactory = XMLOutputFactory.newInstance();
			
			XMLEventWriter eventWriter = xmlFactory.createXMLEventWriter(System.out);
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent newline = eventFactory.createDTD("\n");
			
			XMLEvent start = eventFactory.createStartElement("", "","worldState");
			eventWriter.add(start);
			eventWriter.add(newline);
			
			
			HashMap<Integer, RoomState> WorldGamerooms = state.getRooms();
			ArrayList<RoomState> ListofRooms = new ArrayList<RoomState>();
			ListofRooms.addAll(WorldGamerooms.values());

			//save all rooms
			XMLEvent rooms = eventFactory.createStartElement("","","rooms");
			eventWriter.add(rooms);
			eventWriter.add(newline);
			
			for (RoomState r: ListofRooms){
				XMLEvent roomStart = eventFactory.createStartElement("", "", "room");
				eventWriter.add(roomStart);
				eventWriter.add(newline);
				
				XMLEvent roomEnd = eventFactory.createEndElement("", "", "room");
				eventWriter.add(roomEnd);
				eventWriter.add(newline);
			}
			
			
			
			XMLEvent end = eventFactory.createEndElement("" ,"", "worldState");
			eventWriter.add(end);
			eventWriter.add(newline);
			
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
			
			System.out.println(start);
			
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);			
		}
	}
		
	
	
	public void saveState2(){
		
		WorldGameState state = createGame();
		
		try {
			OutputStream out = new FileOutputStream(new File("test.xml"));
			XMLOutputFactory xmlFactory = XMLOutputFactory.newInstance();
			
			XMLStreamWriter eventWriter = new IndentingXMLStreamWriter(xmlFactory.createXMLStreamWriter(System.out)); //change between 'out' and System.out for debugging
			
			eventWriter.writeDTD("");
			
			eventWriter.writeStartElement("", "worldState","");			
			
			HashMap<Integer, RoomState> WorldGamerooms = state.getRooms();
			ArrayList<RoomState> ListofRooms = new ArrayList<RoomState>();
			ListofRooms.addAll(WorldGamerooms.values());

			//save all rooms
			eventWriter.writeStartElement("","rooms","");
			
			for (RoomState r: ListofRooms){
				eventWriter.writeStartElement("","room","");
				eventWriter.writeCharacters(" " + r.getId()+ " ");
				eventWriter.writeCharacters(r.isDark() + " ");
				
				GameRoomTile[][] tiles = r.getTiles();
				GameEntity[][] entities = r.getEntities();
				
				for (int i = 0; i < tiles.length; i++){
					for (int j = 0; j < tiles[i].length; j++){
						eventWriter.writeStartElement("", "tile", "");
						
						eventWriter.writeCharacters(tiles[i][j].toString()); //write type of tile
						eventWriter.writeCharacters(" " + i + " " + j); //write coordinates of tile
						
						eventWriter.writeEndElement();
					}
				}
								
				eventWriter.writeEndElement();
			}
			
			eventWriter.writeEndElement();
			
			
			eventWriter.writeEndElement();
			
			eventWriter.writeEndDocument();
			
			eventWriter.close();
			
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);			
		}
		
		
	}
	public WorldGameState createGame(){
		///CREATE ROOM 1///

				//CREATE DUMMY VERSIONS OF THE 2D ARRAY THAT ARE USED TO CREATE THE DUMMY ROOM

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


				RoomState DummyRoom1 = new RoomState(dummyTiles, dummyEntities, width, height, 0, false);


		///CREATE ROOM 2///

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






				//add the key card ent

						dummyEntities[2][2] = new KeyCard(0, CardinalDirection.NORTH);
						dummyEntities[2][3] = new KeyCard(0, CardinalDirection.NORTH);
						dummyEntities[2][4] = new KeyCard(0, CardinalDirection.NORTH);
						dummyEntities[2][5] = new KeyCard(0, CardinalDirection.NORTH);
						dummyEntities[2][6] = new KeyCard(0, CardinalDirection.NORTH);
						dummyEntities[2][7] = new KeyCard(0, CardinalDirection.NORTH);

				RoomState DummyRoom2 = new RoomState(dummyTiles, dummyEntities, width, height, 1, true);


				///CREATE ROOM 3///

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








				//add the key card ent

						dummyEntities[2][2] = new KeyCard(0, CardinalDirection.NORTH);
						

				//add the night vision goggles
						dummyEntities[10][15] = new NightVisionGoggles(CardinalDirection.NORTH);
						
						
				RoomState DummyRoom3 = new RoomState(dummyTiles, dummyEntities, width, height, 2, false);
				
				//add some coins tbh
				dummyEntities[10][5] = new Coin(CardinalDirection.NORTH);
				dummyEntities[10][7] = new Coin(CardinalDirection.NORTH);
				dummyEntities[10][9] = new Coin(CardinalDirection.NORTH);
				dummyEntities[10][11] = new Coin(CardinalDirection.NORTH);


				///CREATE ROOM 4///

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








				//add the key card ent

						dummyEntities[2][3] = new KeyCard(0, CardinalDirection.NORTH);
						
				


				RoomState DummyRoom4 = new RoomState(dummyTiles, dummyEntities, width, height, 3, true);



				//spawn some teleporters IN THE ROOMS
				DummyRoom2.spawnTeleporter(CardinalDirection.NORTH, 5, 5, 8, 8, DummyRoom1);// 2 -> 1
				DummyRoom1.spawnTeleporter(CardinalDirection.NORTH, 5, 5, 3, 3, DummyRoom2); //1 -> 2
				DummyRoom2.spawnTeleporter(CardinalDirection.NORTH, 5, 5, 3, 6, DummyRoom3); //2-> 3
				DummyRoom3.spawnTeleporter(CardinalDirection.NORTH, 2, 2, 2, 2, DummyRoom4); //3 ->4
				DummyRoom4.spawnTeleporter(CardinalDirection.NORTH, 3, 3, 4, 4, DummyRoom1); //4 ->1





		//create the rooms collection which we will be giving to the world game state

				HashMap<Integer, RoomState> rooms = new HashMap<>();
				rooms.put(0, DummyRoom1);
				rooms.put(1, DummyRoom2);
				rooms.put(2, DummyRoom3);
				rooms.put(3, DummyRoom4);



				//CREATE THE WORLD GAME STATE FROM THE ROOMS WE MADE
				WorldGameState initialState = new WorldGameState(rooms);//this initial state would be read in from an xml file (basically just rooms i think)
				
				return initialState;
	}
}
