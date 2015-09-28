package gamelogic;
/**
 * an ordinary door that, if moved onto by a player, transports them to another location in the game world
 *
 * doors basically work as teleporters that change a player's internal location and location in the RoomState objects.
 * @author brownmax1
 *
 */
public class DoorTile extends GameRoomTile implements TraversableTile{

	private final int destinationx;
	private final int destinationy;

	private final RoomState destinationRoom;

	public DoorTile(int x, int y, RoomState destinationRoom){
		this.destinationx = x;
		this.destinationy = y;
		this.destinationRoom = destinationRoom;
	}

	@Override
	RenderRoomTile generateDrawableCopy() {
		return new RenderDoorTile(this.getBloody());
	}

	//MOVES AN ENTITY TO THE DESTINATION
	public boolean teleportEntity(GameEntity entToMove){
		this.destinationRoom.attemptToPlaceEntityInRoom(entToMove);
	}




}
