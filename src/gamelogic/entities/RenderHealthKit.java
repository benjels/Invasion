package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderHealthKit extends RenderEntity {




	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "Used to heal things.";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "healthkit";
	private static final String GAME_IMAGE_NAME = "healthkit";


	public RenderHealthKit(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
	}



	@Override
	public Image getImg() {
		throw new RuntimeException("not using this tbh");
	}

	@Override
	public Point getOffset() {
		//throw new RuntimeException("p sure that the offset is stored in the imagehelper map now but need to check with joely");
		return new Point(0, 0);
	}

}
