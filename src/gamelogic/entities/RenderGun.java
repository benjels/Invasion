package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderGun extends  RenderCarryable{
	/**
	 *
	 */
	private static final long serialVersionUID = 8375627365775521507L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "a gun";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "gun";
	private static final String GAME_IMAGE_NAME = "gun";



	public RenderGun(CardinalDirection directionFacing, String textualDesc) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this not added in yet");
	}@Override
	public Point getOffset(){
		return offset;
	}

	@Override
	public String getInventoryImageFileName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName(){
		return "gun";
	}

}
