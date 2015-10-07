package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderZombie extends RenderEntity {

	public RenderZombie(CardinalDirection directionFacing) {
		super(directionFacing, "This is not a friend.");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException();
	}

}
