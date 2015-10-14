package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderMediumCarrier extends RenderCarryable{
	/**
	 *
	 */
	private static final long serialVersionUID = 7283161122191029024L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "mbag";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "mbag";
	private static final String GAME_IMAGE_NAME = "mbag";



	public RenderMediumCarrier(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = GCImageH.smallC.getWidth(null) / 2;
		offset.y = -GCImageH.smallC.getHeight(null) / 4;

	}
	@Override
	public String getName(){
		return "medC";
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
