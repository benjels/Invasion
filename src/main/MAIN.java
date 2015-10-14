package main;

import gamelogic.GameWorldTimeClockThread;
import gamelogic.IndependentActorManager;
import gamelogic.Server;
import graphics.GameCanvas;
import control.Controller;
import control.DummySlave;
import ui.GameGui;
import ui.GameSetUpWindow;

public class MAIN {

	private static Controller controller;

	public static void main(String[] args) {

		GameGui topLevelGui = new GameGui(new GameCanvas());

		//CREATE A SLAVE AND CONNECT IT TO THE SERVER WHICH MAKES A MASTER FOR THEM
		DummySlave mySlave = new DummySlave(0, topLevelGui); //the uid of the dummy player we are using in this hacky shit



		//INIT THE LISTENERa
		Controller theListener = new Controller(topLevelGui, new GameSetUpWindow(), mySlave);
		theListener.addGuiListeners();
	}

}
