package gamelogic.entities;

import gamelogic.CardinalDirection;

public class StandardInventory extends Carrier {

	//THE CARRIER IS SET IN THE CONSTRUCTOR BECAUSE StandardInventory CANNOT BE DROPPED
	public StandardInventory(CardinalDirection directionFaced, Player initialCarrier) {
		super(directionFaced, 5); //the standard inventory has 5 slots
		this.setCurrentHolder(initialCarrier);
	}



	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderStandardInventory(this.getFacingCardinalDirection());
	}

}
