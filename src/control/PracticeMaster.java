package control;

import gamelogic.ClientFrame;
import gamelogic.MiguelServer;
import gamelogic.Server;
import gamelogic.events.PlayerDropEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerMoveDown;
import gamelogic.events.PlayerMoveLeft;
import gamelogic.events.PlayerMoveRight;
import gamelogic.events.PlayerMoveUp;
import gamelogic.events.PlayerNullEvent;
import gamelogic.events.PlayerPickupEvent;
import gamelogic.events.PlayerSelectInvSlot1;
import gamelogic.events.PlayerSelectInvSlot2;
import gamelogic.events.PlayerSelectInvSlot3;
import gamelogic.events.PlayerSelectInvSlot4;
import gamelogic.events.PlayerSelectInvSlot5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PracticeMaster extends Thread{
	
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private PlayerEvent currentEvent = new PlayerNullEvent(0);
	private int id;
	private MiguelServer server;
	private ClientFrame frame;
	
	public PracticeMaster(Socket socket, MiguelServer server){
		this.socket = socket;
		this.server = server;
	}
	
	public void run(){
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			//add this instance off master object into the servers arraylist of masters
			server.addToMasterList(this);
			//read the id of player that made the move
			id = input.readInt();
			int move = input.readInt();
			setCurrentEvent(move);//set current move to be received by the server class to update game state
			//get updated frame from server
			//now need to send over the game state over back to the slave class to be redrawn
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 		
	}
	
	/**
	 * Helper method to be used by Server class to get the current move done by the player
	 * 
	 * @return PlayerEvent that the player wants
	 */
	public PlayerEvent fetchEvent(){
		PlayerEvent temp = currentEvent;
		currentEvent = new PlayerNullEvent(0);
		return temp;
	}
	
	/**
	 * 
	 * @param move - used to decode a move
	 */
	public void setCurrentEvent(int move){
		switch(move){
		case 1:
			currentEvent = new PlayerMoveUp(this.getPlayerUid());
			break;
		case 2:
			currentEvent = new PlayerMoveDown(this.getPlayerUid());
			break;
		case 3:
			currentEvent = new PlayerMoveLeft(this.getPlayerUid());
			break;
		case 4:
			currentEvent = new PlayerMoveRight(this.getPlayerUid());
			break;
		case 5:
			currentEvent = new PlayerPickupEvent(this.getPlayerUid());
			break;
		case 6: 
			currentEvent = new PlayerDropEvent(this.getPlayerUid());
			break;
		case 7:
			currentEvent = new PlayerSelectInvSlot1(this.getPlayerUid());
			break;
		case 8:
			currentEvent = new PlayerSelectInvSlot2(this.getPlayerUid());
			break;
		case 9:
			currentEvent = new PlayerSelectInvSlot3(this.getPlayerUid());
			break;
		case 10:
			currentEvent = new PlayerSelectInvSlot4(this.getPlayerUid());
			break;
		case 11:
			currentEvent = new PlayerSelectInvSlot5(this.getPlayerUid());
			break;
		}
	}
	
	/**
	 * used to determine whether the player has actually done anything since the last
	 * tick.
	 * @return bool false if the event in the buffer is a null event (i.e. we already applied the last requested event from this player) else true.
	 */
	public boolean hasEvent() {
		//sanity check//
		assert(this.currentEvent != null):"if its actual null we have serioius error here";

		if(this.currentEvent instanceof PlayerNullEvent){
			return false;
		}else{
			return true;
		}
	}

	public void setFrame(ClientFrame frame) {
		this.frame = frame;
	}
	
	public int getPlayerUid() {
		return id;
	}
}
