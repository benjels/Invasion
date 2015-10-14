package gamelogic.entities;

import gamelogic.CardinalDirection;

//DRAWABLE VERSION OF ALL CARRYABLES
public abstract class RenderCarryable extends RenderEntity {


	/**
	 *
	 */
	private static final long serialVersionUID = 7074159435458503312L;

	public RenderCarryable(CardinalDirection directionFacing,String gameImageName) {
		super(directionFacing, gameImageName);
	}
}
