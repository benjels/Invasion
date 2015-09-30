package gamelogic.entities;

import gamelogic.CardinalDirection;

/**
 * used to identify entities that can be picked up and stored in a player's inventory
 * @author Max Brown
 *
 */
public abstract class Carryable extends GameEntity{

	public Carryable(CardinalDirection directionFacing) {
		super(directionFacing);
	}


}
