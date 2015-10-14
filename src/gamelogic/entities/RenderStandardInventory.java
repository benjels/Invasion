package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderStandardInventory extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = 5007705979278006330L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "I shouldnt drop this...";
	//image file names:
	private static final String GAME_IMAGE_NAME = "joelychangethislol";


	public RenderStandardInventory(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported just yet tbh");
	}

	@Override
	public Point getOffset() {
		return offset;
	}

	@Override
	public String getName(){
		return "";
	}


}
