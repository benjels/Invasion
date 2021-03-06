package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;
import gamelogic.renderentities.RenderEntity;
import gamelogic.renderentities.RenderGun;

/**
 * a gun that can be used by the fighter strategy to shoot enemies
 * @author brownmax1
 *
 */

public class Gun extends Carryable implements Traversable {
	private static final int CARRY_SIZE = 9;


	public Gun(CardinalDirection directionFacing) {
		super(directionFacing, CARRY_SIZE);//must be carried in top level inventory TODO: refactor these sizes to be declared as static constants?
	}

	@Override
	protected void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer) {
		assert(pickUpPlayer != null);
		//some player just picked this up
		this.setCurrentHolder(pickUpPlayer);
		//make sure the picking up player has night vision
		pickUpPlayer.setHasGun(true);
	}

	@Override
	protected void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
		assert(droppingPlayer != null);
		//some player just dropped this
		this.setCurrentHolder(null);
		//make sure the dropping player has no gun TODO: this causes bug/feature where if you were holding two night visions and you dropped one, you lose nightvision. its ok cause theres only one in the game anyway.
		droppingPlayer.setHasGun(false);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderGun(this.getFacingCardinalDirection(), "This shoots bad men.");//TODO: likewise this can be added to the static constants for initialising super class
	}

	public String toXMLString(){
		return "Gun";
	}

}
