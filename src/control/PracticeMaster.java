package control;

import gamelogic.events.PlayerDropEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerMoveDown;
import gamelogic.events.PlayerMoveLeft;
import gamelogic.events.PlayerMoveRight;
import gamelogic.events.PlayerMoveUp;
import gamelogic.events.PlayerNullEvent;
import gamelogic.events.PlayerPickupEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PracticeMaster extends Thread{
	
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private PlayerEvent currentEvent;
	private int id;
	
	public PracticeMaster(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			id = input.readInt();
			String move = (String)input.readObject();
			switch(move){
			case "w":
				currentEvent = new PlayerMoveUp(this.getPlayerUid());
				break;
			case "a":
				currentEvent = new PlayerMoveDown(this.getPlayerUid());
				break;
			case "s":
				currentEvent = new PlayerMoveLeft(this.getPlayerUid());
				break;
			case "d":
				currentEvent = new PlayerMoveRight(this.getPlayerUid());
				break;
			case "pickup":
				currentEvent = new PlayerPickupEvent(this.getPlayerUid());
				break;
			case "drop": 
				currentEvent = new PlayerDropEvent(this.getPlayerUid());
				break;
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public PlayerEvent fetchEvent(){
		PlayerEvent temp = currentEvent;
		currentEvent = new PlayerNullEvent(0);//set buffer to null
		return temp;
	}
	
	public int getPlayerUid() {
		// TODO how to get the players id
		return 0;
	}
}
