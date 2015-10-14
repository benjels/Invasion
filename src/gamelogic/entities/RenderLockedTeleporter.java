package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderLockedTeleporter extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = -7871169892711542051L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "lockedTeleport";

	public RenderLockedTeleporter(CardinalDirection directionFaced) {
		super(directionFaced , GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Point getOffset() {
		return offset;
	}

	@Override
	public String getName(){
		return "lockedTele";
	}
}
