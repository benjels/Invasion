package control;

import gamelogic.events.Action1PushedEvent;
import gamelogic.events.Action2PushedEvent;
import gamelogic.events.CarrierCloseEvent;
import gamelogic.events.CarrierOpenEvent;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.DownPushedEvent;
import gamelogic.events.LeftPushedEvent;
import gamelogic.events.PlayerDropEvent;
import gamelogic.events.PlayerPickupEvent;
import gamelogic.events.PlayerSelectInvSlot1;
import gamelogic.events.PlayerSelectInvSlot2;
import gamelogic.events.PlayerSelectInvSlot3;
import gamelogic.events.PlayerSelectInvSlot4;
import gamelogic.events.PlayerSelectInvSlot5;
import gamelogic.events.RightPushedEvent;
import gamelogic.events.RotateMapClockwise;
import gamelogic.events.UpPushedEvent;

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
	private String moveToSend;

	public PracticeSlave(Socket socket, int id, GameGui game) {
		this.id = id;
		this.game = game;
		this.socket = socket;
		initialiseStreams();
	}

	public void run() {
		try {						
			output.writeInt(id);
			output.writeBytes(moveToSend);
			//read stuff to be redrawn for each player
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initialiseStreams(){
		try {
			output = new DataOutputStream(socket.getOutputStream());			
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void sendEventClientToServer(ClientGeneratedEvent event){
		if(event instanceof LeftPushedEvent){
			moveToSend = "1\n";
		}else if(event instanceof RightPushedEvent){
			moveToSend = "2\n";
		}else if(event instanceof UpPushedEvent){
			moveToSend = "3\n";
		}else if(event instanceof DownPushedEvent){
			moveToSend = "4\n";
		}else if(event instanceof PlayerPickupEvent){
			moveToSend = "5\n";
		}else if(event instanceof PlayerDropEvent){
			moveToSend = "6\n";
		}else if(event instanceof Action1PushedEvent){
			moveToSend = "7\n";
		}else if(event instanceof Action2PushedEvent){
			moveToSend = "8\n";
		}else if(event instanceof PlayerSelectInvSlot1){
			moveToSend = "9\n";
		}else if(event instanceof PlayerSelectInvSlot2){
			moveToSend = "10\n";
		}else if(event instanceof PlayerSelectInvSlot3){
			moveToSend = "11\n";
		}else if(event instanceof PlayerSelectInvSlot4){
			moveToSend = "12\n";
		}else if(event instanceof PlayerSelectInvSlot5){
			moveToSend = "13\n";
		}else if(event instanceof CarrierOpenEvent){
			moveToSend = "14\n";
		}else if(event instanceof CarrierCloseEvent){
			moveToSend = "15\n";
		}else if(event instanceof RotateMapClockwise){
			moveToSend = "16\n";
		}
	}

	public int getPlayerUid() {
		return id;
	}
}
