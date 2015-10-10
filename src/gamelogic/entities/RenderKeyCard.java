package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.Imagehelper;

import java.awt.Image;
import java.awt.Point;

public class RenderKeyCard extends RenderEntity {
	private Point offset = new Point();
	public RenderKeyCard(CardinalDirection directionFacing) {
		super(directionFacing, "I can use this to open locked areas.");
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("unsupoorted atm tbh");
	}
	
	@Override
	public String toString(){
		return "key";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
