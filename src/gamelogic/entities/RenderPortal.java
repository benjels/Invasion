package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderPortal extends RenderEntity {

	public RenderPortal(CardinalDirection directionFacing) {
		super(directionFacing, "a portal placed by the sorcerer");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this img not added yet");
	}

}
