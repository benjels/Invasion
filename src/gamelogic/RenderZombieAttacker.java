package gamelogic;

import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

import gamelogic.entities.RenderEntity;

/**
 * visual representation of a zombie
 * @author brownmax1
 *
 */
public class RenderZombieAttacker extends RenderEntity {

	private static final String GAME_IMAGE_NAME = "zombie";
	private Point offset = new Point();

	private CardinalDirection dir;

	public RenderZombieAttacker(CardinalDirection directionFacing) {
		super(directionFacing,GAME_IMAGE_NAME);
		this.dir = directionFacing;
		offset.x = 0;
		offset.y = -(int) (1.5 * GCImageH.height);
	}

	@Override
	public Point getOffset() {
		return offset;
	}

	@Override
	public String getName() {
		switch (this.getFacingCardinalDirection()) {
		case NORTH:
			return "burningN";
		case SOUTH:
			return "burningS";
		case EAST:
			return "burningW";
		case WEST:
			return "burningW";
		}
		return "";
	}
}
