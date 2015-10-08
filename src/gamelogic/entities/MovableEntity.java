package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.RoomState;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;
/**
 * any entity that can move around the board (without being carried in a container)
 * can have its location and current room changed etc.
 * @author brownmax1
 *
 */
public abstract class MovableEntity extends GameEntity{


	private RoomState currentRoom;
	private int xInRoom;
	private int yInRoom;
	private final int uniqueId; //this is a crucial field. used to associate the same entity between many classes and across the netwerk.
	private PlayerCharacterStrategy myBehaviour;


	public MovableEntity(CardinalDirection directionFacing, int uid) {
		super(directionFacing);
		this.uniqueId = uid;
	}


	
	
	




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





		public int getUniqueId() {
			return uniqueId;
		}







}
