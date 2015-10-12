package gamelogic;

import gamelogic.entities.GameEntity;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderStandardTeleporter;
//A TELEPORTER THAN ANYONE CAN ALWAYS WALK THROUGH. IT CANNOT MOVE OR BE TURNED OFF.
public class StandardTeleporter extends Teleporter {

	public StandardTeleporter(CardinalDirection directionFaced,
			int destinationx, int destinationy, RoomState destinationRoom) {
		super(directionFaced, destinationx, destinationy, destinationRoom);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderStandardTeleporter(this.getFacingCardinalDirection());
	}

	@Override
	public String toXMLString() {
		return "Standard_Teleporter";
	}


}
