package gamelogic.tiles;

import gamelogic.RoomState;
import gamelogic.Traversable;
import gamelogic.entities.MovableEntity;

/**
 * an ordinary door that, if moved onto by a player, transports them to another location in the game world
 *
 * doors basically work as teleporters that change a player's internal location and location in the RoomState objects.
 * @author brownmax1
 *
 */
public class TeleporterTile extends GameRoomTile implements Traversable{

	private final int destinationx;
	private final int destinationy;


	private final RoomState destinationRoom;


	public TeleporterTile(int destinationx, int destinationy, RoomState destinationRoom){
		//srore values that are used when this teleporter taken by an entity
		this.destinationx = destinationx;
		this.destinationy = destinationy;
		this.destinationRoom = destinationRoom;

	}

	@Override
	public
	RenderRoomTile generateDrawableCopy() { //TODO: set public for package divison
		return new RenderTeleporterTile(this.getBloody());
	}

	//MOVES AN ENTITY TO THE DESTINATION
	public boolean teleportEntity(MovableEntity entToMove){
		return this.destinationRoom.attemptToPlaceEntityInRoom(entToMove, this.destinationx, this.destinationy);
	}




}
