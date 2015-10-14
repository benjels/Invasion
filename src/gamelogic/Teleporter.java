package gamelogic;

import gamelogic.entities.GameEntity;
import gamelogic.entities.Targatable;
import gamelogic.entities.MovableEntity;

/**
 * a teleporter that, if moved onto by a player, transports them to another location in the game world
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




	/**
	 * attempts to mvoe the entity that stepped onto this tele to the teleporter's destination
	 * @param entToMove the entity that we are moving
	 * @return true if the entity was moved, else false (likely there is something blocking the teleporter exit if it returns false)
	 */
	protected boolean teleportEntity(MovableEntity entToMove){
		System.out.println("ATTEMPTING TO PLACE THE ENTITY: " + entToMove + " with a teleporter that goes to:" + this.destinationx +"." + this.destinationy);
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
