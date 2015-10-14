package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderPortal extends RenderEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -3302787196893708282L;
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "a portal.";
	//image file names:
	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	public RenderPortal(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this img not added yet");
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
