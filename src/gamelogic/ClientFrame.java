package gamelogic;

import java.io.Serializable;

/**
 * the "container" for the information that will be sent out over the network.
 * basically just a wrapper for the DrawableGameState and DrawablePlayerInfo objects that
 * are sent to each client on each tick
 * @author brownmax1
 *
 */
public class ClientFrame implements Serializable{

	private final DrawableRoomState roomToDraw; //the room state drawn in main canvas
	private final DrawablePlayerInfo playerInfoToDraw; //the state for player's info in hud


	public ClientFrame(DrawableRoomState playerDrawableRoomState, DrawablePlayerInfo playerInfo) {
		this.roomToDraw = playerDrawableRoomState;
		this.playerInfoToDraw = playerInfo;
	}




	///UTILITY///

	public DrawableRoomState getRoomToDraw() {
		return roomToDraw;
	}


	public DrawablePlayerInfo getPlayerInfoToDraw() {
		return playerInfoToDraw;
	}

}
