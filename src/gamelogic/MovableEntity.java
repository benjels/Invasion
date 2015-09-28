package gamelogic;

public abstract class MovableEntity extends GameEntity{





	public MovableEntity(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	private RoomState currentRoom;
	private int xInRoom;
	private int yInRoom;







	///UTILITY///

	 RoomState getCurrentRoom() {
		return this.currentRoom;
	}

	 void setCurrentRoom(RoomState room){
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
