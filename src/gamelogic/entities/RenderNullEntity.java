package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderNullEntity extends RenderCarryable {
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "this slot is empty";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "maxchangethislol IDK THO THIS NEVER ACTUALLY DRAWS SO DO WATEV";
	//joely's one goes here...


	public RenderNullEntity(CardinalDirection directionFacing){
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do thisyet fame");
	}

	@Override 
	public Point getOffset(){
		return offset;
	}

}
