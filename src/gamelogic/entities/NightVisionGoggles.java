package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;
/**
 * night vision goggles. When they are in a player's inventory, that player can see much
 * further inside dark rooms
 * @author Max Brown
 *
 */

public class NightVisionGoggles extends Carryable implements Traversable {
	private static final int CARRY_SIZE = 1;
	
	
	public NightVisionGoggles(CardinalDirection directionFacing) {
		super(directionFacing, CARRY_SIZE); 
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderNightVisionGoggles(this.getFacingCardinalDirection());
	}

	@Override
	//if we picked up nv goggles, the player has nv now
	void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer) {
		assert(pickUpPlayer != null);
		//some player just picked these up
		this.setCurrentHolder(pickUpPlayer);
		//make sure the picking up player has night vision
		pickUpPlayer.setNightVision(true);
	}

	@Override
	//if we dropped nv goggles, the player has no nv now
	void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
		assert(droppingPlayer != null);
		//some player just dropped these
		this.setCurrentHolder(null);
		//make sure the dropping player has no night vision TODO: this causes bug where if you were holding two night visions and you dropped one, you lose nightvision. its ok cause theres only one in the game anyway.
		droppingPlayer.setNightVision(false);
	}
	
	@Override
	public String toXMLString(){
		return "NightVision_Goggles";
	}

}
