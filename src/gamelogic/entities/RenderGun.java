package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderGun extends RenderEntity {

	public RenderGun(CardinalDirection directionFacing, String textualDesc) {
		super(directionFacing, textualDesc);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this not added in yet");
	}

}
