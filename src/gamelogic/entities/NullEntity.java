package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;


public class NullEntity extends Carryable implements Traversable{

	public NullEntity(){
		super(CardinalDirection.NORTH);
	}

	public NullEntity(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderNullEntity generateDrawableCopy() {
		return new RenderNullEntity(this.getFacingCardinalDirection());
	}



}
