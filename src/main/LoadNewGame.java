package main;

import gamelogic.CardinalDirection;
import gamelogic.ClockThread;
import gamelogic.FighterPlayerStrategy;
import gamelogic.GameWorldTimeClockThread;
import gamelogic.IndependentActorManager;
import gamelogic.RoomState;
import gamelogic.Server;
import gamelogic.WorldGameState;
import gamelogic.entities.GameEntity;
import gamelogic.entities.Player;
import gamelogic.tiles.GameRoomTile;
import graphics.GameCanvas;

import java.io.File;
import java.util.ArrayList;

import control.DummySlave;
import control.Listener;
import storage.XMLParser;
import ui.GameGui;
import ui.GameSetUpWindow;

public class LoadNewGame {

	public static void main(String[] args){
		XMLParser parser = new XMLParser();
		WorldGameState game = parser.parse(new File ("Standard-Tiles.xml"), new File("Standard-Entities.xml"));

		GameWorldTimeClockThread realClock = new GameWorldTimeClockThread(game);

		IndependentActorManager enemyManager = new IndependentActorManager(game);

		//CREATE SERVER FROM THE GAME STATE WE MADE
		Server theServer = new Server(game, enemyManager);

		GameGui topLevelGui = new GameGui(new GameCanvas());


		//CREATE A SLAVE AND CONNECT IT TO THE SERVER WHICH MAKES A MASTER FOR THEM
		DummySlave mySlave = new DummySlave(0, topLevelGui); //the uid of the dummy player we are using in this hacky shit



		//INIT THE LISTENERa
		Listener theListener = new Listener(topLevelGui, new GameSetUpWindow(), mySlave);

		ArrayList<Player> players = parser.getPlayers();
		for (Player p : players){
			//add the player to the map of entities
			theServer.getWorldGameState().addMovableToMap(p);

			//add the player to a room
			theServer.getWorldGameState().getRooms().get(0).attemptToPlaceEntityInRoom(p, 20, 20);

		}
		//connect the slave to the server which creates/spawns the player too
		mySlave.connectToServer(theServer);

		ClockThread clock = new ClockThread(35, theServer);
		realClock.start();
		clock.start();

	}

}
