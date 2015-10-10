package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderCoin extends RenderEntity {
	private Point offset = new Point();

	public RenderCoin(CardinalDirection directionFacing) {
		super(directionFacing, "Some money.");
		offset.x = GCImageH.width/3;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		 throw new RuntimeException("that is not supported aym");
	}
	
	@Override
	public String toString(){
		return "coin";
	}
	
	@Override 
	public Point getOffset(){
		return offset;
	}

}
