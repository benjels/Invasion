package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderCoin extends RenderCarryable{
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "Some money.";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "maxchangethislol";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	public RenderCoin(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = GCImageH.width/3;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		 throw new RuntimeException("that is not supported aym");
	}
	
	@Override
	public String toString(){
		return "coin";
	}
	
	@Override 
	public Point getOffset(){
		return offset;
	}


}
