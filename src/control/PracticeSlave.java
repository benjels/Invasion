package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import ui.GameGui;

public class PracticeSlave extends Thread {

	private final int id;
	private final GameGui game;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;

	public PracticeSlave(Socket socket, int id, GameGui game) {
		this.id = id;
		this.game = game;
		this.socket = socket;
	}

	public void run() {
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			String movement = null;
			Scanner sc = new Scanner(System.in);
			while (movement == null) {//to make sure that we get a movement
				switch (sc.next()) {
				case "w":
					movement = "w";
					break;
				case "s":
					movement = "s";
					break;
				case "a":
					movement = "a";
					break;
				case "d":
					movement = "d";
					break;
				case "pickup":
					movement = "pickup";
					break;
				case "drop":
					movement = "drop";
					break;
				}
			}
			output.writeInt(id);
			output.writeObject(movement);//send event to master to be created 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPlayerUid() {
		return id;
	}
}
