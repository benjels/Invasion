package gamelogic;

import gamelogic.entities.GameEntity;
import gamelogic.entities.Locatable;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderStandardTeleporter;
//A TELEPORTER THAN ANYONE CAN ALWAYS WALK THROUGH. IT CANNOT MOVE OR BE TURNED OFF.
public class StandardTeleporter extends Teleporter implements Locatable{

	private final int myX;
	private final int myY;
	
	public StandardTeleporter(int x, int y, CardinalDirection directionFaced,int destinationx, int destinationy, RoomState destinationRoom) {
		super(directionFaced, destinationx, destinationy, destinationRoom);
		this.myX = x;
		this.myY = y;
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderStandardTeleporter(this.getFacingCardinalDirection());
	}

	@Override
	public String toXMLString() {
		return "Standard_Teleporter-" + getDestinationx() + "-" + getDestinationy() + "-" + getDestinationRoom().getId() + "-" + getxInRoom() + "-" + getyInRoom();
	}

	@Override
	public int getyInRoom() {
		return this.myY;
	}

	@Override
	public int getxInRoom() {
		return this.myY;
	}


}
