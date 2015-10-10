package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderTeleporterGun extends RenderEntity {
	private Point offset = new Point();
	public RenderTeleporterGun(CardinalDirection directionFacing) {
		super(directionFacing, "Use this to make teles");//TODO:static constrant these strings tbh
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this not supported atm");
	}
	@Override 
	public Point getOffset(){
		return offset;
	}
}
