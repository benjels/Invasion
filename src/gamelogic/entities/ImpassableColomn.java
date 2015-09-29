package gamelogic.entities;

import gamelogic.CardinalDirection;

/**
 * a colomn in a room that is an obstruction
 * will be removed in final game prob but ok for testing
 * @author brownmax1
 *
 */
public class ImpassableColomn extends GameEntity{


	public ImpassableColomn(CardinalDirection directionFacing){
		//just pass the initial direction to enclosing parent class
		super(directionFacing);
	}



	@Override
	public RenderImpassableColomn generateDrawableCopy() {
		return new RenderImpassableColomn(this.getFacingCardinalDirection());
	}
}
