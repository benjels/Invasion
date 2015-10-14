package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

/**
 * the smalles carrier in the game that fits only a couople small items
 * @author brownmax1
 *
 */
public class SmallCarrier extends Carrier implements Traversable{

	public SmallCarrier(CardinalDirection directionFaced) {
		super(directionFaced, 2, 1, 2);//the small inventory has 2 slots, can only fit items with size <= 1. has size 2

	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderSmallCarrier(this.getFacingCardinalDirection());
	}

	@Override
	public String toXMLString(){
		return "Small_Carrier";

	}

}
