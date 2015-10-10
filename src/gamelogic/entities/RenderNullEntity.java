package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderNullEntity extends RenderEntity {
	private Point offset = new Point();


	public RenderNullEntity(CardinalDirection directionFaced){
		super(directionFaced, "null tbh");
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
