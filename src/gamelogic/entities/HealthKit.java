package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

public class HealthKit extends Carryable implements Traversable{
	
	
	private static final int CARRY_SIZE = 1;
	

	public HealthKit(CardinalDirection directionFacing) {
		super(directionFacing, CARRY_SIZE);
	}

	//picking this up should change the player's amount of health kits
	@Override
	protected void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer) {
		assert(pickUpPlayer != null);
		//some player just picked this up
		this.setCurrentHolder(pickUpPlayer);
		//make sure the picking up player has an extra health kit
		pickUpPlayer.addHealthKit();
	}

	//dropping this should change the player's amount of health kits
	@Override
	protected void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
		assert(droppingPlayer != null);
		//some player just dropped this
		this.setCurrentHolder(null);
		//make sure the dropping player loses a health kit
		droppingPlayer.removeHealthKit();		
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderHealthKit(this.getFacingCardinalDirection());
	}

	@Override
	public String toXMLString() {
		// TODO Auto-generated method stub
		return null;
	}

}
