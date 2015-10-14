package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.PlayerCharacterStrategy;
import gamelogic.PylonRoomState;
import gamelogic.RoomState;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;
/**
 * any entity that can move around the board (without being carried in a container)
 * can have its location and current room changed etc.
 * //NB: classes that extend MovableEntity should not implement the Traversable interface. That will result in them being placed in the entitiesCache array if they are stepped on. This will become a problem
//if they attempt to move whilst in that array.
 * @author brownmax1
 *
 */

public abstract class MovableEntity extends GameEntity implements Targatable{


	private RoomState currentRoom;
	private int xInRoom;
	private int yInRoom;
	private final int uniqueId; //this is a crucial field. used to associate the same entity between many classes and across the netwerk.
	private PlayerCharacterStrategy myBehaviour;


	public MovableEntity(CardinalDirection directionFacing, int uid, RoomState spawnRoom) {
		super(directionFacing);
		this.uniqueId = uid;
		this.currentRoom = spawnRoom;
	}













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
