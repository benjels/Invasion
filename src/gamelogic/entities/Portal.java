package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.RoomState;
import gamelogic.Traversable;
//SIMILAR TO A TELEPORTER BUT PLACED BY THE PLAYER AND IS TWO WAY. needs to store information about both entrances/exits.
public class Portal extends GameEntity implements Traversable{
	
	//info for the first gate
	private int x1;
	private int y1;
	private RoomState room1;
	//info for the second gate
	private int x2;
	private int y2;
	private RoomState room2; //note that this may well be same RoomState as room1
	//which gate should be placed next
	private boolean placeGate1Next = true; //(when this is true, if the player places a gate, the x1 vars etc get changed)
	

	public Portal(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderPortal(this.getFacingCardinalDirection());
	}

	
	//USED TO CREATE A GATE WHEN THE SORCERER TRIES TO PLACE
	//A PORTAL. OVERWRITES LEAST RECENTLY USED PORTAL IF ALREADY
	//TWO PORTALS PLACED
	public void createANewGate(int xToPlaceGate, int yToPlaceGate, RoomState roomToPlaceGate) {
		//have to determine which gate we are changing
		if(placeGate1Next){
			//clean up the old gate from the board
			if(room1 != null){
				room1.removeOldPortalGate(x1, y1);
			}
			//update the gate fields
			System.out.println("updating gate 1 ...");
			this.x1 = xToPlaceGate;
			this.y1 = yToPlaceGate;
			this.room1 = roomToPlaceGate;
			//we changed gate 1, so next time the player uses portal gun we change gate 2
			this.placeGate1Next = false;
		}else{//we are placing second gate
			//clean up the old gate from the board
			if(room2 != null){
				room2.removeOldPortalGate(x2, y2);
			}
			//update the gate
			System.out.println("updating gate 2 ...");
			this.x2 = xToPlaceGate;
			this.y2 = yToPlaceGate;
			this.room2 = roomToPlaceGate;
			//we changed gate 2, so next time the player uses portal gun we change gate 1
			this.placeGate1Next = true;
		}
	}
	
	//USED WHEN A MOVABLE ENTITY STEPS ON ONE OF THE GATES OF THIS PORTAL
	public boolean attemptToTakePortal(int xSteppedOn, int ySteppedOn, RoomState roomSteppedOn, MovableEntity entToMove){
		//check that both gates have been created
		if(this.room1 == null || this.room2 == null){
			throw new RuntimeException("havent created both of the gates yet fam");
			//return false;
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
	
	//helper method to clean up old gates when we go through them
	private void cleanUpGates(){
		//gate 1
		room1.removeOldPortalGate(x1, y1);
		//reset gate fields
		this.x1 = 0;
		this.y1 = 0;
		this.room1 = null;
		//////////////////////////////////////////////
		//gate 2
		room2.removeOldPortalGate(x2, y2);
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
