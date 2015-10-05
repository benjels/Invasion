package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.RoomState;
import gamelogic.Traversable;

/**
 * an ordinary door that, if moved onto by a player, transports them to another location in the game world
 *
 * doors basically work as teleporters that change a player's internal location and location in the RoomState objects.
 * @author brownmax1
 *
 */
public class Teleporter extends GameEntity implements Traversable{

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

	@Override
	public
	RenderEntity generateDrawableCopy() {
		return new RenderTeleporter(this.getFacingCardinalDirection()); //TODO: set public for package divison
		
	}

	//MOVES AN ENTITY TO THE DESTINATION
	public boolean teleportEntity(MovableEntity entToMove){
		return this.destinationRoom.attemptToPlaceEntityInRoom(entToMove, this.destinationx, this.destinationy);
	}




}
