package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderSmallCarrier extends RenderEntity {

	public RenderSmallCarrier(CardinalDirection directionFacing) {
		super(directionFacing, "I can put a couple of things in here.");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cant do this atm");
	}

}
