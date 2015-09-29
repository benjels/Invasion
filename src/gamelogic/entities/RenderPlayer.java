package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;

import java.awt.Image;

/**
 * a player to be rendered on the game board. A player can be facing one of the FOUR directions
 * AND they may be one of several characters. (different characters have different sprites on the board)
 * @author brownmax1
 *
 */

public class RenderPlayer extends RenderEntity {

private final CharacterStrategy playerStrategy;//the kind of character that this player is playing as
private final int playerHealth; //the health of this player on the drawable board (will be used to draw health bars of players)



	public RenderPlayer(CharacterStrategy playerStrategy, CardinalDirection directionFaced, int health) {
		super(directionFaced);
		this.playerStrategy = playerStrategy; //necessary to know which character to draw
		this.playerHealth = health; //necessary if we want to draw health bars on screen
	}


	@Override
	public Image getImg() {
		throw new RuntimeException("nah this is not supported yet");
	}






}
