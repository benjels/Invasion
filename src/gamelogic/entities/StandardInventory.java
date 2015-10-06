package gamelogic.entities;

import gamelogic.CardinalDirection;

public class StandardInventory extends Carrier {

	//THE CARRIER IS SET IN THE CONSTRUCTOR BECAUSE StandardInventory CANNOT BE DROPPED
	public StandardInventory(CardinalDirection directionFaced, Player initialCarrier) {
		super(directionFaced, 5); //the standard inventory has 5 slots
		this.setCurrentHolder(initialCarrier);
	}

	//needs to call this same method on all of the items that it contains in case e.g. one of them is NV goggles
	@Override
	void checkIfPickupChangedPlayerStatus(Player pickUpPlayer) {
	throw new RuntimeException("cannot pick up the top level standard inventory");//it is given to each player at the start and cannot be dropped
	}

	@Override
	void checkIfDropChangedPlayerStatus(Player droppingPlayer) {
		
	throw new RuntimeException("CANNOT DROP THE TOP LEVEL STANDARDINVENTORY!!!");//TODO: not a real exception obvs
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderStandardInventory(this.getFacingCardinalDirection());
	}

}
