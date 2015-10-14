package gamelogic;

import java.io.Serializable;

import gamelogic.entities.GameEntity;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderPortal;

/**
 * a portal that takes the movable entity that steps on it from one of the gates to the other.
 * this is similar to the Teleporter, but it is placed by the sorcerer when he is wielding the warp gun
 * there is a maximum of two gates in the game world at any one time and the portal is not activated until two
 * gates have been placed in the game world.
 *
 * when a player takes the portal, both the gates are removed. This is to prevent players just placing a portal in each of the pylon rooms
 * and not going through the maze
 * @author brownmax1
 *
 */
public class Portal extends GameEntity implements Traversable{

	//info for the first gate
	private int x1;
	private int y1;
	private RoomState room1;

	//info for the second gate
	private int x2;
	private int y2;
	private RoomState room2;

	//which gate should be placed next
	private boolean placeGate1Next = true; //(when this is true, if the player places a gate, the x1 vars etc get changed)


	public Portal(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderPortal(this.getFacingCardinalDirection());
	}


	/**
	 * used to spawn a portal gate into the game world as an entity. if two gates are already place, it overwrites the least recently placed
	 * one
	 * @param xToPlaceGate the x location of the gate
	 * @param yToPlaceGate the y location of the gate
	 * @param roomToPlaceGate the room that the gate was placed in
	 */
	protected void createANewGate(int xToPlaceGate, int yToPlaceGate, RoomState roomToPlaceGate) {

		//have to determine which gate we are changing
		if(placeGate1Next){

			//clean up the old gate from the board
			if(room1 != null){
				room1.removeRedundantGameEntity(x1, y1);
			}
			//update the gate fields
			this.x1 = xToPlaceGate;
			this.y1 = yToPlaceGate;
			this.room1 = roomToPlaceGate;
			//we changed gate 1, so next time the player uses portal gun we change gate 2
			this.placeGate1Next = false;
		}else{//we are placing second gate
			//clean up the old gate from the board
			if(room2 != null){
				room2.removeRedundantGameEntity(x2, y2);
			}
			//update the gate
			this.x2 = xToPlaceGate;
			this.y2 = yToPlaceGate;
			this.room2 = roomToPlaceGate;
			//we changed gate 2, so next time the player uses portal gun we change gate 1
			this.placeGate1Next = true;
		}
	}

	/**
	 * used to attempt to transport an entity that stepped on one of the gates of this portal
	 * @param xSteppedOn the x coordinate that was stepped on
	 * @param ySteppedOn the y coordinate that was stepped on
	 * @param roomSteppedOn the room of the gate that was stepped on
	 * @param entToMove the entity that stepped on a gate
	 * @return true if the entity is successfully transported, else false
	 */
	protected boolean attemptToTakePortal(int xSteppedOn, int ySteppedOn, RoomState roomSteppedOn, MovableEntity entToMove){

		//check that both gates have been created
		if(this.room1 == null || this.room2 == null){
			return false;
		}

		//we need to find which gate the entity is standing on and transport them to the other one
		if(xSteppedOn == this.x1 && ySteppedOn == this.y1 && roomSteppedOn == this.room1){// YES == for object comparison because interested in actual instance identity

			//gates are single use entry so clean up afterwards
			RoomState tempRoom2 = this.room2;
			int tempX = this.x2;
			int tempY = this.y2;
			cleanUpGates();
			return tempRoom2.attemptToPlaceEntityInRoom(entToMove, tempX, tempY);
		}else if(xSteppedOn == this.x2 && ySteppedOn == this.y2 && roomSteppedOn == this.room2){

			//gates are single use entry so clean up afterwards
			RoomState tempRoom1 = this.room1;
			int tempX = this.x1;
			int tempY = this.y1;
			cleanUpGates();
			return tempRoom1.attemptToPlaceEntityInRoom(entToMove, tempX, tempY);
		}else{
			throw new RuntimeException("attempted to transport entity via portal but their coordinates do not match those of this portal.");
		}
	}

	/**
	 * used to clean up the gates from the game logic when the player has warped through them
	 */
	private void cleanUpGates(){
		//gate 1
		room1.removeRedundantGameEntity(x1, y1);
		//reset gate fields
		this.x1 = 0;
		this.y1 = 0;
		this.room1 = null;
		//////////////////////////////////////////////
		//gate 2
		room2.removeRedundantGameEntity(x2, y2);
		//reset gate fields
		this.x2 = 0;
		this.y2 = 0;
		this.room2 = null;
		//////////////////////////////////////////////

	}

	//UTITLITY//
	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public RoomState getRoom1() {
		return room1;
	}

	public void setRoom1(RoomState room1) {
		this.room1 = room1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public RoomState getRoom2() {
		return room2;
	}

	public void setRoom2(RoomState room2) {
		this.room2 = room2;
	}

	public String toXMLString(){
		return "Portal";
	}





}
