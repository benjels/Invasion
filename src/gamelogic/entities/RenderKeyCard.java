package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderKeyCard extends RenderEntity {

	public RenderKeyCard(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("unsupoorted atm tbh");
	}

}
