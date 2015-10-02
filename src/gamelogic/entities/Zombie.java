package gamelogic.entities;

import gamelogic.CardinalDirection;

/**
 * the prototype enemy that has a dumb asf ai that just moves back and forth
 * @author brownmax1
 *
 */
public class Zombie extends MovableEntity{

	public Zombie(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderZombie(this.getFacingCardinalDirection());
	}

}
