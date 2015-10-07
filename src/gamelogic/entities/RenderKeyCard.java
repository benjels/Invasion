package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderKeyCard extends RenderEntity {

	public RenderKeyCard(CardinalDirection directionFacing) {
		super(directionFacing, "I can use this to open locked areas.");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("unsupoorted atm tbh");
	}

}
