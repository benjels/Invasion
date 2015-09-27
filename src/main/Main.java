package main;

import ui.GameGui;
import control.DummySlave;
import gamelogic.CardinalDirection;
import gamelogic.ClockThread;
import gamelogic.GameEntity;
import gamelogic.GameRoomTile;
import gamelogic.ImpassableColomn;
import gamelogic.NullEntity;
import gamelogic.OuterWall;
import gamelogic.Player;
import gamelogic.RoomState;
import gamelogic.Server;
import gamelogic.SpaceShipInteriorStandardTile;
import gamelogic.TankStrategy;
import gamelogic.WorldGameState;
import graphics.GameCanvas;

public class Main {

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

		//add in some colomns so that we can practice drawing
		dummyEntities[3][5] = new ImpassableColomn(CardinalDirection.NORTH);
		dummyEntities[3][6] = new ImpassableColomn(CardinalDirection.NORTH);
		dummyEntities[3][7] = new ImpassableColomn(CardinalDirection.NORTH);

		dummyEntities[4][8] = new ImpassableColomn(CardinalDirection.NORTH);
		dummyEntities[5][8] = new ImpassableColomn(CardinalDirection.NORTH);




		//printing out the room that we just made
		for(int i = 0; i < height ; i ++){
			for(int j = 0; j < width ; j ++){
				if(dummyEntities[j][i] instanceof NullEntity){
					System.out.print("n  ");
				}else if(dummyEntities[j][i] instanceof ImpassableColomn){
					System.out.print("r  ");
				}else if(dummyEntities[j][i] instanceof OuterWall){
					System.out.print("x  ");
				}else if (dummyEntities[j][i] instanceof Player){
					System.out.print("p  ");
				}else{
					throw new RuntimeException("that is not a known kind of entity");
				}
			}
			System.out.println("\n");
		}



		//CREATE THE DUMMY serverside ROOM WE JUST HAVE ONE ROOM FOR NOW
		RoomState DummyRoom = new RoomState(dummyTiles, dummyEntities, width, height);



		//CREATE THE SERVER with init state and room (will actually be an xml file eventually obvs)
		WorldGameState initialState = new WorldGameState();//this initial state would be read in from an xml file (basically just rooms i think)
		initialState.setSpawnRoom(DummyRoom); //in the real game this will be created set in the world's game state when the game is created (rooms have an isSpawnRoom bool)
		Server theServer = new Server(initialState); //this init state will be read in from xml or json or watev


	//CREATE A PLAYER AND ADD IT TO THE SERVER
		Player myPlayer = new Player("myname", 0, new TankStrategy(), CardinalDirection.NORTH); //name, uid, spawnroom SETTING THE PLAYER TO FACE NORTH
		theServer.addPlayer(myPlayer);//add the player to the server's map of uid --> Player so that when this palyer's master sends an event to the server, the server can attempt taht event


	//CREATE A SLAVE AND CONNECT IT TO THE SERVER WHICH MAKES A MASTER FOR THEM
		DummySlave mySlave = new DummySlave(0, topLevelGui); //the uid of the dummy player we are using in this hacky shit
		mySlave.connectToServer(theServer);



		//...AND START THE CLOCK SO THAT THE SERVER SENDS THINGS BACK ON TICK
		ClockThread clock = new ClockThread(100, theServer);
		clock.start();




	}



}
