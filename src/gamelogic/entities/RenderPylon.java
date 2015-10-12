package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderPylon extends RenderEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7103042911521918018L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "Some money.";
	//image file names:
	private static final String GAME_IMAGE_NAME = "joelychangethislol";
	
	public RenderPylon(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		offset.x = 8;
		offset.y = -100;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported at the moment");
	}

	@Override
	public String toString() {
		return "pylon";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
