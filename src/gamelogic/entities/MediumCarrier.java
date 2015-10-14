package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

/**
 * a carrier with 3 slots of medium size
 * @author brownmax1
 *
 */
public class MediumCarrier extends Carrier implements Traversable{

	public MediumCarrier(CardinalDirection directionFaced) {
		super(directionFaced, 3, 5, 6);//3 slots, max size 5 for each slot, has size 6

	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderMediumCarrier(this.getFacingCardinalDirection());
	}

	public String toXMLString(){
		return "Medium_Carrier";
	}

}
