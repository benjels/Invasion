package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.Imagehelper;

import java.awt.Image;
import java.awt.Point;

public class RenderKeyCard extends RenderCarryable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2404309219798888125L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "use to go through locked teles";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "maxchangethislol";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";
	
	
	
	
	public RenderKeyCard(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("unsupoorted atm tbh");
	}
	
	@Override
	public String toString(){
		return "key";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

	@Override
	public String getInventoryImageFileName() {
		// TODO Auto-generated method stub
		return null;
	}

}
