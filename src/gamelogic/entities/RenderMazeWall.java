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
	private CardinalDirection dir;
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "A maze wall.";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	RenderMazeWall(CardinalDirection directionFaced){
		super(directionFaced, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		this.dir = directionFaced;
		offset.x = (GCImageH.width / 4) + 3;
		offset.y = -60;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do this yet");
	}

	@Override
	public String toString(){
		switch (dir) {
		case NORTH:
			return "impassConNS";
		case SOUTH:
			return "impassConNS";
		case EAST:
			return "impassConEW";
		case WEST:
			return "impassConEW";
		}
		return "";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
