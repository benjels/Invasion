package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

//A CARRIER IN THE GAME THAT IS SMALLER THAN THE STANDARD INVENTORY AND LARGER THAN THE SMALLCARRIER
public class MediumCarrier extends Carrier implements Traversable{

	public MediumCarrier(CardinalDirection directionFaced) {
		super(directionFaced, 3);//the medium inventory has 3 slots
		
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderMediumCarrier(this.getFacingCardinalDirection());
	}

}
