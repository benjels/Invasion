package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderMediumCarrier extends RenderCarryable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7283161122191029024L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "a medium sized bag.";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "maxchangethislol";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";
	
	
	
	public RenderMediumCarrier(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported atm tbhj");
	}
	@Override 
	public Point getOffset(){
		return offset;
	}
}
