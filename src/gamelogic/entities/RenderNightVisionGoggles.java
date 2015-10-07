package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderNightVisionGoggles extends RenderEntity {

	public RenderNightVisionGoggles(CardinalDirection directionFacing) {
		super(directionFacing, "I can use these to see further in the dark rooms.");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("not supported yet coming soon");
	}

}
