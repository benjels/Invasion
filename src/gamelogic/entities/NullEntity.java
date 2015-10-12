package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;


public class NullEntity extends Carryable implements Traversable{



	public NullEntity(CardinalDirection directionFacing) {
		super(directionFacing, 0);//has size 0 so that it can be carried by anything
	}

	@Override
	public RenderNullEntity generateDrawableCopy() {
		return new RenderNullEntity(this.getFacingCardinalDirection());
	}

	public String toXMLString(){
		return "Null_Entity";
	}

	@Override
	void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
		// TODO Auto-generated method stub

	}

}
