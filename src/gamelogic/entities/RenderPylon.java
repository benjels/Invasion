package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderPylon extends RenderEntity {

	public RenderPylon(CardinalDirection directionFacing, String textualDesc) {
		super(directionFacing, textualDesc);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported at the moment");
	}

}
