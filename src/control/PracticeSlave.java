package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import ui.GameGui;

public class PracticeSlave extends Thread {

	private final int id;
	private final GameGui game;
	private DataOutputStream output;
	private DataInputStream input;
	private Socket socket;

	public PracticeSlave(Socket socket, int id, GameGui game) {
		this.id = id;
		this.game = game;
		this.socket = socket;
	}

	public void run() {
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			int movement = -1;
			Scanner sc = new Scanner(System.in);
			while (movement == -1) {//to make sure that we get a movement
				switch (sc.next()) {
				case "w":
					movement = 1;
					break;
				case "s":
					movement = 2;
					break;
				case "a":
					movement = 3;
					break;
				case "d":
					movement = 4;
					break;
				case "pickup":
					movement = 5;
					break;
				case "drop":
					movement = 6;
					break;
				case "pick1":
					movement = 7;
					break;
				case "pick2":
					movement = 8;
					break;
				case "pick3":
					movement = 9;
					break;
				case "pick4":
					movement = 10;
					break;
				case "pick5":
					movement = 11;
					break;
				}
			}
			output.writeInt(id);
			output.writeInt(movement);
			//read stuff to be redrawn for each player
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPlayerUid() {
		return id;
	}
}
