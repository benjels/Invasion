package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderNightVisionGoggles extends RenderCarryable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6749289290022645417L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "Use to see further at night.";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "maxchangethislol";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";


	public RenderNightVisionGoggles(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("not supported yet coming soon");
	}
	@Override
	public Point getOffset(){
		return offset;
	}

	@Override
	public String getInventoryImageFileName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toString(){
		return "nightV";
	}
}
