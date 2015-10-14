package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderPortal extends RenderEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -3302787196893708282L;
	//image file names:
	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	public RenderPortal(CardinalDirection directionFacing) {
		super(directionFacing, GAME_IMAGE_NAME);
	}


	@Override
	public Point getOffset() {
		return new Point(0, 0);//MAX ADDED THIS SO THAT RENDERER DOESNT THROW EXCEPTIONSS.
	}

	@Override
	public String getName(){
		return "portal";
	}


}
