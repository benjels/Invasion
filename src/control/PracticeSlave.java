package control;

import gamelogic.ClientFrame;
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
	//private final GameGui game;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;
	private int moveToSend;
	private ClientFrame frame;

	public PracticeSlave(){//Socket socket int id, GameGui game) {
		this.id = 10;
		//this.game = game;
		this.socket = socket;
		//initialiseStreams();
	}

	public void run() {
		try {
			Socket socket = new Socket("localhost", 1234);
			initialiseStreams(socket);
			/*output.writeInt(id);
			output.writeInt(moveToSend);
			frame = (ClientFrame) input.readObject();
			//read stuff to be redrawn for each player
			redrawForClient(frame);*/
			output.writeObject("Slave");
			output.writeInt(100);
			output.writeBoolean(false);
			output.flush();
			
			boolean bool = input.readBoolean();
			System.out.println("Received from master: " + bool);
				
		} catch (IOException e) {
			terminate();
		}
	}
	
	/**
	 * helper method to close out all connections
	 */
	public void terminate(){
		System.out.println("Slave has been terminated");
		try {
			output.close();
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public void redrawForClient(ClientFrame frame2) {
		game.getInvasionCanvas().setDrawableState(frame.getRoomToDraw());
		game.getInvasionCanvas().repaint();
		game.getPlayerCanvas().setDrawableState(frame.getPlayerInfoToDraw());
		game.getPlayerCanvas().repaint();
		
	}*/
	
	/**
	 * helper method that initializes the streams
	 * @param socket
	 */
	private void initialiseStreams(Socket socket){
		try {
			output = new ObjectOutputStream(socket.getOutputStream());			
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	/**
	 * Helper method for the Listener class to send events in to the slave
	 * ClientGeneratedEvents are then "encoded" to be sent in through the
	 * network
	 * @param event ClientGeneratedEvent
	 */
	public void sendEventClientToServer(ClientGeneratedEvent event){
		if(event instanceof LeftPushedEvent){
			moveToSend = 1;
		}else if(event instanceof RightPushedEvent){
			moveToSend = 2;
		}else if(event instanceof UpPushedEvent){
			moveToSend = 3;
		}else if(event instanceof DownPushedEvent){
			moveToSend = 4;
		}else if(event instanceof PlayerPickupEvent){
			moveToSend = 5;
		}else if(event instanceof PlayerDropEvent){
			moveToSend = 6;
		}else if(event instanceof Action1PushedEvent){
			moveToSend = 7;
		}else if(event instanceof Action2PushedEvent){
			moveToSend = 8;
		}else if(event instanceof PlayerSelectInvSlot1){
			moveToSend = 9;
		}else if(event instanceof PlayerSelectInvSlot2){
			moveToSend = 10;
		}else if(event instanceof PlayerSelectInvSlot3){
			moveToSend = 11;
		}else if(event instanceof PlayerSelectInvSlot4){
			moveToSend = 12;
		}else if(event instanceof PlayerSelectInvSlot5){
			moveToSend = 13;
		}else if(event instanceof CarrierOpenEvent){
			moveToSend = 14;
		}else if(event instanceof CarrierCloseEvent){
			moveToSend = 15;
		}else if(event instanceof RotateMapClockwise){
			moveToSend = 16;
		}
	}
	
	/**
	 * gets the players id
	 * @return int id
	 */
	public int getPlayerUid() {
		return id;
	}
	
	//tester main method to start the slave/client
	public static void main(String[] args) {
		new PracticeSlave().start();
	}
}
