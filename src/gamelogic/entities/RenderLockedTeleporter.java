package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderLockedTeleporter extends RenderEntity {
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "Some money.";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	public RenderLockedTeleporter(CardinalDirection directionFaced) {
		super(directionFaced , INV_IMAGE_TEXTUAL_DESC,  GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this not supported atm");
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
