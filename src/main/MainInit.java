package main;

import ui.GameGui;
import control.DummySlave;
import gamelogic.CardinalDirection;
import gamelogic.ClockThread;
import gamelogic.RoomState;
import gamelogic.Server;
import gamelogic.TankStrategy;
import gamelogic.WorldGameState;
import gamelogic.entities.GameEntity;
import gamelogic.entities.ImpassableColomn;
import gamelogic.entities.KeyCard;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Player;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.SpaceShipInteriorStandardTile;
import graphics.GameCanvas;

public class MainInit {

	public static void main(String[] args) {
		System.out.println("running this shit");
		System.out.println("im helping too");

		System.out
				.println("welcome to the program. There should be an initialisation dialog that is shown now that\n lets you create a server and then create a new player and join that server separately\n we will pretend that is done automagically for now so that i dont have to create those dialogs");

		//IMPORTANT NOTE ON CURRENT OVERALL SETUP IMPLEMENTATION:
		//at the moment I am just using a dummy slave and dummy master to simulate how the information flows throughout the program.
		//in reality, the process of connecting clients to the server will be quite different and the whole slave/master system will be more complex
		//we should probably make this connection sequence happen in similar way to pacman. I dont want to think about how that will work tho
		//will find out what's needed there from miguel




		//CREATE THE GUI AND CANVAS SHITS
		GameGui topLevelGui = new GameGui(new GameCanvas());


///CREATE ROOM 1///

		//CREATE DUMMY VERSIONS OF THE 2D ARRAY THAT ARE USED TO CREATE THE DUMMY ROOM

		int width = 20;
		int height = 20;


		GameRoomTile[][] dummyTiles = new GameRoomTile[width][height];
		GameEntity[][] dummyEntities = new GameEntity[width][height];

		//fill up tha tiles
		for(int i = 0; i < width ; i++){
			for(int j = 0; j < height ; j++){
				dummyTiles[i][j] = new SpaceShipInteriorStandardTile();
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
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
			}
		}


		RoomState DummyRoom1 = new RoomState(dummyTiles, dummyEntities, width, height, 0);


///CREATE ROOM 2///

		 width = 40;
		 height = 40;

		 dummyTiles = new GameRoomTile[width][height];
		 dummyEntities = new GameEntity[width][height];

		//fill up tha tiles
		for(int i = 0; i < width ; i++){
			for(int j = 0; j < height ; j++){
				dummyTiles[i][j] = new SpaceShipInteriorStandardTile();
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
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
			}
		}






		//add the walls to edge locations
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//insert walls at the top and bottom
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
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

		RoomState DummyRoom2 = new RoomState(dummyTiles, dummyEntities, width, height, 1);


		///CREATE ROOM 3///

		 width = 10;
		 height = 10;

		 dummyTiles = new GameRoomTile[width][height];
		 dummyEntities = new GameEntity[width][height];

		//fill up tha tiles
		for(int i = 0; i < width ; i++){
			for(int j = 0; j < height ; j++){
				dummyTiles[i][j] = new SpaceShipInteriorStandardTile();
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
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
			}
		}






		//add the walls to edge locations
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//insert walls at the top and bottom
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
			}
		}



		//add the key card ent

				dummyEntities[2][2] = new KeyCard(0, CardinalDirection.NORTH);


		RoomState DummyRoom3 = new RoomState(dummyTiles, dummyEntities, width, height, 1);


		///CREATE ROOM 4///

		 width = 5;
		 height = 5;

		 dummyTiles = new GameRoomTile[width][height];
		 dummyEntities = new GameEntity[width][height];

		//fill up tha tiles
		for(int i = 0; i < width ; i++){
			for(int j = 0; j < height ; j++){
				dummyTiles[i][j] = new SpaceShipInteriorStandardTile();
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
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
			}
		}






		//add the walls to edge locations
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//insert walls at the top and bottom
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
			}
		}



		//add the key card ent

				dummyEntities[2][3] = new KeyCard(0, CardinalDirection.NORTH);


		RoomState DummyRoom4 = new RoomState(dummyTiles, dummyEntities, width, height, 1);








		//CREATE THE SERVER with init state and room (will actually be an xml file eventually obvs)
		WorldGameState initialState = new WorldGameState();//this initial state would be read in from an xml file (basically just rooms i think)
		initialState.setSpawnRoom(DummyRoom2); //in the real game this will be created set in the world's game state when the game is created (rooms have an isSpawnRoom bool)
		Server theServer = new Server(initialState); //this init state will be read in from xml or json or watev


		//spawn some teleporters IN THE ROOMS
		DummyRoom2.spawnTeleporter(CardinalDirection.NORTH, 5, 5, 8, 8, DummyRoom1);// 2 -> 1
		DummyRoom1.spawnTeleporter(CardinalDirection.NORTH, 5, 5, 3, 3, DummyRoom2); //1 -> 2
		DummyRoom2.spawnTeleporter(CardinalDirection.NORTH, 5, 5, 3, 6, DummyRoom3); //2-> 3
		DummyRoom3.spawnTeleporter(CardinalDirection.NORTH, 2, 2, 2, 2, DummyRoom4); //3 ->4
		DummyRoom4.spawnTeleporter(CardinalDirection.NORTH, 3, 3, 4, 4, DummyRoom1); //4 ->1



	//CREATE A PLAYER AND ADD IT TO THE SERVER
		Player myPlayer = new Player("CoolMax;);)", 0, new TankStrategy(), CardinalDirection.NORTH); //name, uid, spawnroom SETTING THE PLAYER TO FACE NORTH
		theServer.addPlayer(myPlayer);//add the player to the server's map of uid --> Player so that when this palyer's master sends an event to the server, the server can attempt taht event

	//CREATE A SLAVE AND CONNECT IT TO THE SERVER WHICH MAKES A MASTER FOR THEM
		DummySlave mySlave = new DummySlave(0, topLevelGui); //the uid of the dummy player we are using in this hacky shit
		mySlave.connectToServer(theServer);



		//...AND START THE CLOCK SO THAT THE SERVER SENDS THINGS BACK ON TICK
		ClockThread clock = new ClockThread(100, theServer);
		clock.start();




	}



}
