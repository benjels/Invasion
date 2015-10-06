package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderStandardInventory extends RenderEntity {

	public RenderStandardInventory(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported just yet tbh");
	}

}
