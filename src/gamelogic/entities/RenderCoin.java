package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderCoin extends RenderEntity {

	public RenderCoin(CardinalDirection directionFacing) {
		super(directionFacing, "Some money.");
	}

	@Override
	public Image getImg() {
		 throw new RuntimeException("that is not supported aym");
	}

}
