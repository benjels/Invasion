package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderPortal extends RenderEntity {

	public RenderPortal(CardinalDirection directionFacing) {
		super(directionFacing, "a portal placed by the sorcerer");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this img not added yet");
	}

	@Override
	public Point getOffset() {
		return new Point(0, 0);//MAX ADDED THIS SO THAT RENDERER DOESNT THROW EXCEPTIONSS.
	}

}
