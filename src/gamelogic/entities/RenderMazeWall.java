package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderMazeWall extends RenderEntity{
	private Point offset = new Point();

	RenderMazeWall(CardinalDirection directionFaced){
		super(directionFaced, "I can't move through this.");
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
