package gamelogic.renderentities;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.FighterPlayerStrategy;
import gamelogic.SorcererPlayerStrategy;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

/**
 * a player to be rendered on the game board. A player can be facing one of the
 * FOUR directions AND they may be one of several characters. (different
 * characters have different sprites on the board)
 *
 * @author brownmax1
 *
 */

public class RenderPlayer extends RenderEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 5018632356969170522L;
	private final CharacterStrategy playerStrategy;// the kind of character that
													// this player is playing as
	private final int playerHealth; // the health of this player on the drawable
									// board (will be used to draw health bars
									// of players)
	private CardinalDirection dir;
	private Point offset = new Point();

	// image file names:
	private static final String GAME_IMAGE_NAME = "wall";

	public RenderPlayer(CharacterStrategy playerStrategy,
			CardinalDirection directionFaced, int health) {
		super(directionFaced, GAME_IMAGE_NAME);
		this.playerStrategy = playerStrategy; // necessary to know which
												// character to draw
		this.playerHealth = health; // necessary if we want to draw health bars
									// on screen
		this.dir = directionFaced;
		offset.x = 0;
		offset.y = -(int) (1.5 * GCImageH.height);
	}

	@Override
	public String getName() {
		if (playerStrategy instanceof FighterPlayerStrategy) {
			switch (dir) {
			case NORTH:
				return "pNorth";
			case SOUTH:
				return "pSouth";
			case EAST:
				return "pEast";
			case WEST:
				return "pWest";
			}
		} else if (playerStrategy instanceof SorcererPlayerStrategy) {
			switch (dir) {
			case NORTH:
				return "SNorth";
			case SOUTH:
				return "SSouth";
			case EAST:
				return "SEast";
			case WEST:
				return "SWest";
			}
		}
		return "";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

	public CharacterStrategy getPlayerStrategy() {
		return playerStrategy;
	}

}
