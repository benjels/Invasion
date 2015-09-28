package gamelogic;
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
RenderEntity generateDrawableCopy() {
	return new RenderKeyCard(this.getFacingCardinalDirection());
}


}
