package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderZombie extends RenderEntity {

	public RenderZombie(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException();
	}

}
