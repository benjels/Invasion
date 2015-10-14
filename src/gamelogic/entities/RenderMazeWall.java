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
	private static final String GAME_IMAGE_NAME = "concrete";

	RenderMazeWall(CardinalDirection directionFaced){
		super(directionFaced, GAME_IMAGE_NAME);
		this.setDir(directionFaced);
		offset.x = (GCImageH.width / 4) + 3;
		offset.y = -60;
	}

	@Override
	public String getName(){
		switch (getDir()) {
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

	public CardinalDirection getDir() {
		return dir;
	}

	public void setDir(CardinalDirection dir) {
		this.dir = dir;
	}

}
