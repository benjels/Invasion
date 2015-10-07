package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderTeleporter extends RenderEntity {


	RenderTeleporter(CardinalDirection directionFaced) {
		super(directionFaced, "This will take me somewhere else.");
	}

	@Override
	public Image getImg() {
	throw new RuntimeException("this not supported atm");
	}

}
