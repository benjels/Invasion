package gamelogic;

import java.io.Serializable;

import gamelogic.entities.RenderEntity;
import gamelogic.tiles.RenderRoomTile;

/**
 * a version of a RoomState that is rendered by a user's display.
 * These objects are generated for each player and sent out over the network on each clock tick.
 * the information in the fields of this object should be enough to draw everything that is visible on the player's
 * renderer canvas correctly.
 * @author brownmax1
 *
 */
public class DrawableRoomState implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -7253375657325590356L;
	private final RenderRoomTile[][] tiles; //the squares/tiles in this room
	private final RenderEntity[][] entities; //the "directioned" items/characters in this room
	private final CardinalDirection viewingOrientation; //the cardinal direction that is treated as "up" for viewing this room
	private final String timeOfDay; //the current time of day in 24hour/military time. Will be used for lighting etc.
	private final RoomLocation playerLocationInRoom; //where the player is in this room. Rendering should be centred on this location.
	private final int roomId; //the integer id that is used to identify this room (THIS WILL BE USED BY RENDERER TO DETERMINE WHETHER WE ARE DRAWING A NEW ROOM OR NOT).
	private final boolean isDark; //set to true if we need to draw the transparent blackness over the rooms
	private final boolean isGameOver;




	DrawableRoomState(RenderRoomTile[][] tiles, RenderEntity[][] entities, String timeOfDay2, CardinalDirection viewingOrientation, RoomLocation playerLocation, int id, boolean isDark, boolean isGameOver){
		this.tiles = tiles;
		this.entities = entities;
		this.timeOfDay = timeOfDay2;
		this.viewingOrientation = viewingOrientation;
		this.playerLocationInRoom = playerLocation;
		this.roomId = id;
		this.isDark = isDark;
		this.isGameOver = isGameOver;
	}



	///UTILITY///

	public RenderRoomTile[][] getTiles() {
		return tiles;
	}


	public RenderEntity[][] getEntities() {
		return entities;
	}





	public String getTimeOfDay() {
		return timeOfDay;
	}




	public CardinalDirection getViewingOrientation() {
		return viewingOrientation;
	}




	public RoomLocation getPlayerLocationInRoom() {
		return playerLocationInRoom;
	}






	public int getRoomId() {
		return roomId;
	}






	public boolean isDark() {
		return isDark;
	}



	public boolean isGameOver() {
		return isGameOver;
	}









}
