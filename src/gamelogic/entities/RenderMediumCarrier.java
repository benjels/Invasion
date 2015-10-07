package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderMediumCarrier extends RenderEntity {

	public RenderMediumCarrier(CardinalDirection directionFacing) {
		super(directionFacing, "I can store a few things in here.");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported atm tbhj");
	}

}
