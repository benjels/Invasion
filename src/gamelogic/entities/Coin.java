package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;
import gamelogic.renderentities.RenderCoin;
import gamelogic.renderentities.RenderEntity;

/**
 * a coin in the game world that is added to a player's coin count automatically when they step over it
 * @author max Brown
 *
 */
public class Coin extends GameEntity implements Traversable{

	public Coin(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderCoin(this.getFacingCardinalDirection());
	}
	
	@Override
	public String toXMLString(){
		return "Coin";
	}
}
