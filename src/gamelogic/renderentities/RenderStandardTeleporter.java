package gamelogic.renderentities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderStandardTeleporter extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = 9172565728284551667L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "standardTelelporter";


	public RenderStandardTeleporter(CardinalDirection directionFaced) {
		super(directionFaced, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}


	@Override
	public Point getOffset() {
		return offset;
	}


	@Override
	public String getName(){
		return "tele";
	}

}
