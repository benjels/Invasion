package gamelogic;

import gamelogic.entities.GameEntity;
import gamelogic.entities.Targatable;
import gamelogic.renderentities.RenderEntity;
import gamelogic.renderentities.RenderStandardTeleporter;
/**
 * A teleporter that any movable entity can move through to reach its destination
 * @author brownmax1
 *
 */
public class StandardTeleporter extends Teleporter implements Targatable{

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
