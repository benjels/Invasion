package gamelogic;

import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderLockedTeleporter;
//TELEPORTER THAT WE CAN ONLY GO THROUGH IF WE ARE CARRYING THE KEY CARD
public class LockedTeleporter extends Teleporter{

	public LockedTeleporter(CardinalDirection directionFaced, int destinationx,
			int destinationy, RoomState destinationRoom) {
		super(directionFaced, destinationx, destinationy, destinationRoom);
	}
	
	@Override
	//ONLY MOVES ENTITY TO LOCATION IF ENTITY INSTANCEOF PLAYER AND .hasKey 
	protected boolean teleportEntity(MovableEntity entToMove){
		//check that the attmepted move is by a player
		if(!(entToMove instanceof Player)){
			throw new RuntimeException("cannot move a non player through locked teleporter"); //TODO just return false
		}
		//check that the player hasKey before moving them
		Player movingPlayer = (Player)entToMove;
		if(movingPlayer.hasKeyEnabled()){
			return this.getDestinationRoom().attemptToPlaceEntityInRoom(entToMove, this.getDestinationx(), this.getDestinationy());
		}else{
			throw new RuntimeException("cannot teleport if you dont have the key card");//TODO should just return false
		}
		

	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderLockedTeleporter(this.getFacingCardinalDirection()); //TODO: this should be diff for diff kinds of teles
	}

	@Override
	public String toXMLString() {
		return "Locked_Teleporter";
	}

}
