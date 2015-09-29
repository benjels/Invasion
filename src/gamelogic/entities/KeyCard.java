package gamelogic.entities;

import gamelogic.CardinalDirection;

/**
 * prototype of carryable items abstractclass/interface
 * if the carrier has this in their inventory, they can do some shit idk
 * @author brownmax1
 *
 */
public class KeyCard extends GameEntity implements TraversableEntity{

private final int cardId; //used to identify what "locked" game feature this card is used on

public KeyCard(int cardId, CardinalDirection directionFaced){
super(directionFaced);
this.cardId = cardId;
}

@Override
public
RenderEntity generateDrawableCopy() { //TODO: set public for package divison
	return new RenderKeyCard(this.getFacingCardinalDirection());
}


}
