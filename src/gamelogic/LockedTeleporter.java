package gamelogic;

import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderLockedTeleporter;
/**
 * teleporter that can only be travelled through if MovableEntity has the keycard in their inventory
 * @author brownmax1
 *
 */
public class LockedTeleporter extends Teleporter{

	public LockedTeleporter(CardinalDirection directionFaced, int destinationx,
			int destinationy, RoomState destinationRoom) {
		super(directionFaced, destinationx, destinationy, destinationRoom);
	}

	/**
	 * attempts to place an entity at the location that this teleporter leads to.
	 * returns true if the player is successfully placed, else returns false (might fail if the would be traveller is not a Player or is not carrying the keycard)
	 */
	@Override
	protected boolean teleportEntity(MovableEntity entToMove){

		//check that the attmepted move is by a player
		if(!(entToMove instanceof Player)){
			return false;
		}

		//check that the player hasKey before moving them
		Player movingPlayer = (Player)entToMove;
		if(movingPlayer.hasKeyEnabled()){
			return this.getDestinationRoom().attemptToPlaceEntityInRoom(entToMove, this.getDestinationx(), this.getDestinationy());
		}else{
			return false;
		}


	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderLockedTeleporter(this.getFacingCardinalDirection()); //TODO: this should be diff for diff kinds of teles
	}

	@Override
	public String toXMLString() {
		return "Locked_Teleporter-" + getDestinationx() + "-" + getDestinationy() + "-" + getDestinationRoom().getId();
	}

}
