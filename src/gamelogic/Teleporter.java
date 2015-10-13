package gamelogic;

import gamelogic.entities.GameEntity;
import gamelogic.entities.Locatable;
import gamelogic.entities.MovableEntity;

/**
 * a portal that, if moved onto by a player, transports them to another location in the game world
 *
 * change a movable entity's internal location and location in the RoomState objects.
 * @author brownmax1
 *
 */
public abstract class Teleporter extends GameEntity implements Traversable{

	private final int destinationx;
	private final int destinationy;


	private final RoomState destinationRoom;


	public Teleporter(CardinalDirection directionFaced, int destinationx, int destinationy, RoomState destinationRoom){
		super(directionFaced);
		//srore values that are used when this teleporter taken by an entity
		this.destinationx = destinationx;
		this.destinationy = destinationy;
		this.destinationRoom = destinationRoom;

	}


	

	//MOVES AN ENTITY TO THE DESTINATION
	protected boolean teleportEntity(MovableEntity entToMove){
		return this.destinationRoom.attemptToPlaceEntityInRoom(entToMove, this.getDestinationx(), this.getDestinationy());
	}
	
	@Override
	public String toString(){
		return "Teleporter";
	}

	protected RoomState getDestinationRoom(){
		return this.destinationRoom;
	}

	protected int getDestinationx() {
		return destinationx;
	}

	protected int getDestinationy() {
		return destinationy;
	}


}
