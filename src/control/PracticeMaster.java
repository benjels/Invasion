package control;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.ClientFrame;
import gamelogic.FighterPlayerStrategy;
import gamelogic.MiguelServer;
import gamelogic.Server;
import gamelogic.entities.RenderCoin;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderImpassableColomn;
import gamelogic.entities.RenderKeyCard;
import gamelogic.entities.RenderMediumCarrier;
import gamelogic.entities.RenderNightVisionGoggles;
import gamelogic.entities.RenderNullEntity;
import gamelogic.entities.RenderOuterWall;
import gamelogic.entities.RenderPlayer;
import gamelogic.entities.RenderPylon;
import gamelogic.entities.RenderSmallCarrier;
import gamelogic.entities.RenderStandardInventory;
import gamelogic.entities.RenderTeleporter;
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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PracticeMaster extends Thread{
	
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private PlayerEvent currentEvent = new PlayerNullEvent(0);
	private int id;
	private MiguelServer server;
	
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
	
	/**
	 * receives an event from the server's queue of events to send out to all the clients
	 *
	 */
	public synchronized void sendClientFrameMasterToSlave(ClientFrame frame){
		//encode stuff from DrawableRoomState
		int time = Integer.parseInt(frame.getRoomToDraw().getTimeOfDay());
		int roomId = frame.getRoomToDraw().getRoomId();
		boolean isDark = frame.getRoomToDraw().isDark(); 
		int dir = encodeDirection(frame.getRoomToDraw().getViewingOrientation());
		int xPosRoom = frame.getRoomToDraw().getPlayerLocationInRoom().getX();
		int yPosRoom = frame.getRoomToDraw().getPlayerLocationInRoom().getY();
		int[][] renderTiles = convertToInt(frame.getRoomToDraw().getTiles());
		//int length = renderTiles.length;
		//byte [] byteTiles = intArrayToByteArray(renderTiles);
		int[][] renderEntities = convertEntitiesToInt(frame.getRoomToDraw().getEntities());
		//encoded stuff from DrawablePlayerInfo
		int health = frame.getPlayerInfoToDraw().getHealthPercentage();
		int coins = frame.getPlayerInfoToDraw().getCoinsCollected();
		int playerRoomId = frame.getPlayerInfoToDraw().getPlayerRoomId();
		int playerName = Integer.parseInt(frame.getPlayerInfoToDraw().getPlayerIrlName());
		//TODO: Do i need the score?
		int pylon0 = frame.getPlayerInfoToDraw().getPylon0Health();
		int pylon1 = frame.getPlayerInfoToDraw().getPylon1Health();
		int currentRoom = Integer.parseInt(frame.getPlayerInfoToDraw().getCurrentRoomName());
		int currentTime = Integer.parseInt(frame.getPlayerInfoToDraw().getCurrentTime());
		int strategy = encodeStrategy(frame.getPlayerInfoToDraw().getPlayerCharacter());
		try {
			//stuff for DrwaableRoomState
			output.writeInt(time);
			output.writeInt(roomId);
			output.writeBoolean(isDark);
			output.writeInt(dir);
			output.writeInt(xPosRoom);
			output.writeInt(yPosRoom);
			byte[] byteTiles = intArrayToByteArray(renderTiles);
			output.writeInt(byteTiles.length);
			output.write(byteTiles);
			byte[] byteEntities = intArrayToByteArrayForEntities(renderEntities);
			output.writeInt(byteTiles.length);
			output.write(byteEntities);
			//stuff for DrawablePlayerInfo
			output.writeInt(health);
			output.writeInt(coins);
			output.writeInt(playerRoomId);
			output.writeInt(playerName);
			output.writeInt(pylon0);
			output.writeInt(pylon1);
			output.writeInt(currentRoom);
			output.writeInt(currentTime);
			output.writeInt(strategy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		int[][] array = new int[tiles.length][tiles.length];
		for(int i = 0; i< tiles.length; i++){
			for(int j = 0; j< tiles.length; j++){
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
		int[][] array = new int[entity.length][entity.length];
		for(int i = 0; i < entity.length; i++){
			for(int j = 0; j < entity.length; j++){
				if(entity[i][j] instanceof RenderCoin){
					array[i][j] = 0;
				}else if(entity[i][j] instanceof RenderImpassableColomn){
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
				}else if(entity[i][j] instanceof RenderTeleporter){
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
	
	
}
