package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.RoomState;

public abstract class MovableEntity extends GameEntity{





	public MovableEntity(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	private RoomState currentRoom;
	private int xInRoom;
	private int yInRoom;







	///UTILITY///

	 public RoomState getCurrentRoom() {//TODO: set public for package divison
		return this.currentRoom;
	}

	 public void setCurrentRoom(RoomState room){ //TODO: set public for package divison
			this.currentRoom = room;
	 }



	 public int getyInRoom() {
			return yInRoom;
		}


		public void setyInRoom(int yInRoom) {
			this.yInRoom = yInRoom;
		}

		public int getxInRoom() {
			return xInRoom;
		}

		public void setxInRoom(int xInRoom) {
			this.xInRoom = xInRoom;
		}


}
