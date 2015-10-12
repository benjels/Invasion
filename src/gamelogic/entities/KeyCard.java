package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

/**
 * prototype of carryable items abstractclass/interface
 * if the carrier has this in their inventory, they can do some shit idk
 * @author brownmax1
 *
 */

public class KeyCard extends Carryable implements Traversable{
	private static final int CARRY_SIZE = 1;
	

public KeyCard(CardinalDirection directionFaced){
super(directionFaced, CARRY_SIZE);
}


@Override
public
RenderEntity generateDrawableCopy() { //TODO: set public for package divison
	return new RenderKeyCard(this.getFacingCardinalDirection());
}

@Override
//if we picked up the key, the player can take locked teleporters now
protected void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer) {
	assert(pickUpPlayer != null);
	//some player just picked this up
	this.setCurrentHolder(pickUpPlayer);
	//make sure the picking up player has key
	pickUpPlayer.setKeyEnabled(true);
}

@Override
//if we dropped the key, the player cannot take locked teleporters now
protected void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
	assert(droppingPlayer != null);
	//some player just dropped these
	this.setCurrentHolder(null);
	//make sure the dropping player has no key TODO: this causes bug where if you were holding two night visions and you dropped one, you lose nightvision. its ok cause theres only one in the game anyway.
	droppingPlayer.setKeyEnabled(false);
}


public String toXMLString(){
	return "KeyCard";
}

}
