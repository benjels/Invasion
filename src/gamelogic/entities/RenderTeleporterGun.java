package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderTeleporterGun extends RenderEntity {

	public RenderTeleporterGun(CardinalDirection directionFacing) {
		super(directionFacing, "Use this to make teles");//TODO:static constrant these strings tbh
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this not supported atm");
	}

}
