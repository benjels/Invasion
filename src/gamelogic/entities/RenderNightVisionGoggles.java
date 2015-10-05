package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderNightVisionGoggles extends RenderEntity {

	public RenderNightVisionGoggles(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("not supported yet coming soon");
	}

}
