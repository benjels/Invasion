package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderMediumCarrier extends RenderEntity {

	public RenderMediumCarrier(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported atm tbhj");
	}

}
