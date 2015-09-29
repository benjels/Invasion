package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

public class NullEntity extends GameEntity implements Traversable{



	public NullEntity(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	public RenderNullEntity generateDrawableCopy() {
		return new RenderNullEntity(this.getFacingCardinalDirection());
	}



}
