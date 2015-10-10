package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderGun extends RenderEntity {
	private Point offset = new Point();
	public RenderGun(CardinalDirection directionFacing, String textualDesc) {
		super(directionFacing, textualDesc);
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

}
