package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

//A CARRIER IN THE GAME THAT IS SMALLER THAN THE STANDARD INVENTORY AND LARGER THAN THE SMALLCARRIER
public class SmallCarrier extends Carrier implements Traversable{

	public SmallCarrier(CardinalDirection directionFaced) {
		super(directionFaced, 2);//the small inventory has 2 slots
		
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderSmallCarrier(this.getFacingCardinalDirection());
	}

}
