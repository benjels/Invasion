package control;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.ClientFrame;
import gamelogic.FighterPlayerStrategy;
import gamelogic.MiguelServer;
import gamelogic.Server;
import gamelogic.entities.RenderCoin;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderMazeWall;
import gamelogic.entities.RenderKeyCard;
import gamelogic.entities.RenderMediumCarrier;
import gamelogic.entities.RenderNightVisionGoggles;
import gamelogic.entities.RenderNullEntity;
import gamelogic.entities.RenderOuterWall;
import gamelogic.entities.RenderPlayer;
import gamelogic.entities.RenderPylon;
import gamelogic.entities.RenderSmallCarrier;
import gamelogic.entities.RenderStandardInventory;
import gamelogic.entities.RenderStandardTeleporter;
import gamelogic.entities.RenderZombie;
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
import gamelogic.tiles.RenderHarmfulTile;
import gamelogic.tiles.RenderInteriorStandardTile;
import gamelogic.tiles.RenderRoomTile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PracticeMaster extends Thread{
	private final int port = 1234;	
	private Socket socket;
	private ObjectInputStream input;
	//private BufferedReader reader;
	private ObjectOutputStream output;
	private PlayerEvent currentEvent = new PlayerNullEvent(0);
	private int id;
	private MiguelServer server;
	private ClientFrame frame;
	//private ClientFrame updatedFrame;
	private boolean isReady = false;
	
	public PracticeMaster(Socket socket){//, MiguelServer server){
		this.socket = socket;
		//this.server = server;
		initialiseStreams();
	}
	
	public void run(){
		try {			
			//add this instance off master object into the servers arraylist of masters
		/*	server.addToMasterList(this);
			//read the id of player that made the move
			id = input.readInt();
			int move = input.readInt();
			setCurrentEvent(move);//set current move to be received by the server class to update game state
			//get updated frame from server
			//now need to send over the game state over back to the slave class to be redrawn
			if(isReady){
				synchronized(frame){
					output.writeObject(frame);
				}				
			}
			isReady = false;*/
			String word = (String)input.readObject();
			System.out.println("Word received from slave: " + word);
			int number = input.readInt();
			System.out.println("Number received from slave: " + number);
			boolean bool = input.readBoolean();
			if(bool){
				System.out.println("Going into if in master");
				output.writeBoolean(true);				
			}			
			output.flush();
			//receiving the updated frame
			//frame = (ClientFrame) input.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			terminate();
		} 		
	}
	
	public void terminate(){
		System.out.println("Master has been terminated");
		try {
			output.close();
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initialiseStreams(){
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			
			//reader = new BufferedReader(new InputStreamReader(input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	/**
	 * receives an event from the server's queue of events to send out to all the clients
	 *
	 */
	public synchronized void sendClientFrameMasterToSlave(ClientFrame frame){
		this.frame = frame;
		isReady = true;
		//encode stuff from DrawableRoomState
		/*String time = frame.getRoomToDraw().getTimeOfDay() + "\n";
		String roomId = String.valueOf(frame.getRoomToDraw().getRoomId()) + "\n";
		boolean isDark = frame.getRoomToDraw().isDark(); 
		String dir = String.valueOf(encodeDirection(frame.getRoomToDraw().getViewingOrientation()))+ "\n";
		String xPosRoom = String.valueOf(frame.getRoomToDraw().getPlayerLocationInRoom().getX())+ "\n";
		String yPosRoom = String.valueOf(frame.getRoomToDraw().getPlayerLocationInRoom().getY())+ "\n";
		int[][] renderTiles = convertToInt(frame.getRoomToDraw().getTiles());
		//int length = renderTiles.length;
		//byte [] byteTiles = intArrayToByteArray(renderTiles);
		int[][] renderEntities = convertEntitiesToInt(frame.getRoomToDraw().getEntities());
		//encoded stuff from DrawablePlayerInfo
		String health = String.valueOf(frame.getPlayerInfoToDraw().getHealthPercentage())+ "\n";
		String coins = String.valueOf(frame.getPlayerInfoToDraw().getCoinsCollected())+ "\n";
		String playerRoomId = String.valueOf(frame.getPlayerInfoToDraw().getPlayerRoomId())+ "\n";
		String playerName = frame.getPlayerInfoToDraw().getPlayerIrlName() + "\n";
		String score = String.valueOf(frame.getPlayerInfoToDraw().getScore())+ "\n";
		String pylon0 = String.valueOf(frame.getPlayerInfoToDraw().getPylon0Health())+ "\n";
		String pylon1 = String.valueOf(frame.getPlayerInfoToDraw().getPylon1Health())+ "\n";
		String currentRoom = String.valueOf(frame.getPlayerInfoToDraw().getCurrentRoomName())+ "\n";
		String currentTime = frame.getPlayerInfoToDraw().getCurrentTime()+ "\n";
		String strategy = String.valueOf(encodeStrategy(frame.getPlayerInfoToDraw().getPlayerCharacter()))+ "\n";
		try {
			//stuff for DrawableRoomState
			output.writeBytes(time);
			output.writeBytes(roomId);
			output.writeBoolean(val);
			output.writeBytes(dir);
			output.writeBytes(xPosRoom);
			output.writeBytes(yPosRoom);
			
			//TODO: byte stuff unsure
			byte[] byteTiles = intArrayToByteArray(renderTiles);
			output.writeInt(byteTiles.length);
			output.write(byteTiles);
			byte[] byteEntities = intArrayToByteArrayForEntities(renderEntities);
			output.writeInt(byteTiles.length);
			output.write(byteEntities);
			
			//stuff for DrawablePlayerInfo
			output.writeBytes(health);
			output.writeBytes(coins);
			output.writeBytes(playerRoomId);
			output.writeBytes(playerName);
			output.writeBytes(score);
			output.writeBytes(pylon0);
			output.writeBytes(pylon1);
			output.writeBytes(currentRoom);
			output.writeBytes(currentTime);
			output.writeBytes(strategy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//this.slave.sendGameStateMasterToSlave(gameToPaint);
	}
	
	public int getPlayerUid() {
		return id;
	}
	
	/**
	 * Helper method to convert the 2D array of RenderRoomTiles into
	 * integers which will then be converted into bytes into a byte array 
	 * @param tiles - type of tile
	 * @return int[][]
	 */
	private synchronized int[][] convertToInt(RenderRoomTile[][] tiles){
		int[][] array = new int[tiles.length][tiles[0].length];
		for(int i = 0; i< tiles.length; i++){
			for(int j = 0; j< tiles[i].length; j++){
				if(tiles[i][j] instanceof RenderHarmfulTile){
					array[i][j] = 0;
				}else if(tiles[i][j] instanceof RenderInteriorStandardTile){
					array[i][j] = 1;
				}
			}
		}
		return array;		
	}
	/**
	 * Helper method to convert the 2D array of ints into a byte array which will be
	 * sent over the network
	 * @param array
	 * @return
	 * @throws IOException
	 */
	private synchronized byte[] intArrayToByteArray (int [][] array ) throws IOException {  	
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(bos);
	    for(int i = 0; i< array.length;i++){
	    	for(int j = 0; j < array.length; j++){
	    		dos.writeInt(array[i][j]);
	    	}
	    }	    
	    dos.flush();
	    return bos.toByteArray();
	}
	
	/*private byte[] intToByteArray ( final int i ) throws IOException {  	
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(bos);
	    dos.writeInt(i);
	    dos.flush();
	    return bos.toByteArray();
	}*/
	
	/**
	 * Helper method to decode the direction the player is facing into an
	 * int which will be sent over the network
	 * @param direction - direction player is facing
	 * @return int
	 */
	private synchronized int encodeDirection(CardinalDirection direction){
		switch(direction){
		case NORTH:
			return 0;
		case SOUTH:
			return 1;
		case EAST:
			return 2;
		default://WEST
			return 3;
		}
	}
	
	private synchronized int[][] convertEntitiesToInt(RenderEntity[][] entity){
		int[][] array = new int[entity.length][entity[0].length];
		for(int i = 0; i < entity.length; i++){
			for(int j = 0; j < entity[i].length; j++){
				if(entity[i][j] instanceof RenderCoin){
					array[i][j] = 0;
				}else if(entity[i][j] instanceof RenderMazeWall){
					array[i][j] = 1;
				}else if(entity[i][j] instanceof RenderKeyCard){
					array[i][j] = 2;
				}else if(entity[i][j] instanceof RenderMediumCarrier){
					array[i][j] = 3;
				}else if(entity[i][j] instanceof RenderNightVisionGoggles){
					array[i][j] = 4;
				}else if(entity[i][j] instanceof RenderNullEntity){
					array[i][j] = 5;
				}else if(entity[i][j] instanceof RenderOuterWall){
					array[i][j] = 6;
				}else if(entity[i][j] instanceof RenderPlayer){
					array[i][j] = 7;
				}else if(entity[i][j] instanceof RenderPylon){
					array[i][j] = 8;
				}else if(entity[i][j] instanceof RenderSmallCarrier){
					array[i][j] = 9;
				}else if(entity[i][j] instanceof RenderStandardInventory){
					array[i][j] = 10;
				}else if(entity[i][j] instanceof RenderStandardTeleporter){
					array[i][j] = 11;
				}else if(entity[i][j] instanceof RenderZombie){
					array[i][j] = 12;
				}
			}
		}
		return array;
	}
	
	private synchronized byte[] intArrayToByteArrayForEntities (int [][] array ) throws IOException {  	
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(bos);
	    for(int i = 0; i< array.length;i++){
	    	for(int j = 0; j < array.length; j++){
	    		dos.writeInt(array[i][j]);
	    	}
	    }	    
	    dos.flush();
	    return bos.toByteArray();
	}
	
	private synchronized int encodeStrategy(CharacterStrategy strategy){
		if(strategy instanceof FighterPlayerStrategy){
			return 0;
		}
		return 1;
	}
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSock = new ServerSocket(1234);
			System.out.println("Started running server");
			while(true){
				Socket socket = serverSock.accept();
				new PracticeMaster(socket).start();
			}
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
