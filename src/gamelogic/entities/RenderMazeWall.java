package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderMazeWall extends RenderEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1926617565115723833L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "A maze wall.";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	RenderMazeWall(CardinalDirection directionFaced){
		super(directionFaced, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		offset.x = (GCImageH.width / 4);;
		offset.y = -60;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do this yet");
	}
	
	@Override
	public String toString(){
		return "impassConNS";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
