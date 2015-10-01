package main;

import storage.GameStateWriter;

public class SavingMain {
	
	public static void main(String args[]){
		GameStateWriter g = new GameStateWriter();
		g.marshall();
	}

}
