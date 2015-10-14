package gamelogic;

import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

import gamelogic.entities.RenderEntity;

public class RenderExplosion extends RenderEntity {
	private static final String GAME_IMAGE_NAME = "explosion";
	private Point offset = new Point();

	private CardinalDirection dir;

	public RenderExplosion(CardinalDirection directionFacing) {
		super(directionFacing,GAME_IMAGE_NAME);
		this.dir = directionFacing;
		offset.x = -40;
		offset.y = -100;
	}


	@Override
	public Point getOffset() {
		return offset;
	}

	@Override
	public String getName() {
		return "explosion";
	}
}
