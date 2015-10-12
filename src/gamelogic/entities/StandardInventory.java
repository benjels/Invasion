package gamelogic.entities;

import gamelogic.CardinalDirection;

public class StandardInventory extends Carrier {

	//THE CARRIER IS SET IN THE CONSTRUCTOR BECAUSE StandardInventory CANNOT BE DROPPED
	public StandardInventory(CardinalDirection directionFaced, Player initialCarrier) {
		super(directionFaced, 5, Integer.MAX_VALUE, Integer.MAX_VALUE); //the standard inventory has 5 slots. it can hold things of size maximum and takes up maximum space (it cannot be placed inside anything else and anything can be placed in it)
		this.setCurrentHolder(initialCarrier);
	}



	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderStandardInventory(this.getFacingCardinalDirection());
	}
	
	public String toXMLString(){
		return "Standard_Inventory";
	}

}
