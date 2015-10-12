package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

//some kind of item that is used to create teleporters. note that the only strategy
//that can use this item atm is the Sorcerer Strategy
public class TeleporterGun extends Carryable implements Traversable{
	private static final int CARRY_SIZE = 9;
	
	
	public TeleporterGun(CardinalDirection directionFacing) {
		super(directionFacing, CARRY_SIZE); //can only be carried at top level
	}

	@Override
	protected void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer) {
		assert(pickUpPlayer != null);
		//some player just picked these up
		this.setCurrentHolder(pickUpPlayer);
		//make sure the picking up player has night vision
		pickUpPlayer.setHasTeleGun(true);
	}

	@Override
	protected void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
		assert(droppingPlayer != null);
		//some player just picked these up
		this.setCurrentHolder(null);
		//make sure the picking up player has night vision
		droppingPlayer.setHasTeleGun(false);
		
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderTeleporterGun(CardinalDirection.NORTH);
	}
	
	public String toXMLString(){
		return "Teleporter_Gun";
	}

}
