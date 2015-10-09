package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

//A CARRIER IN THE GAME THAT IS SMALLER THAN THE STANDARD INVENTORY AND LARGER THAN THE SMALLCARRIER
public class MediumCarrier extends Carrier implements Traversable{

	public MediumCarrier(CardinalDirection directionFaced) {
		super(directionFaced, 3, 5, 6);//3 slots, max size 5 for each slot, has size 6
		
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderMediumCarrier(this.getFacingCardinalDirection());
	}
	
	@Override
	public String toString(){
		return "Medium_Carrier";
	}

}
