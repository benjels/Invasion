package gamelogic.entities;


import gamelogic.CardinalDirection;

/**
 * a wall around the edge of the room that marks the edge of
 * the arena
 * @author brownmax1
 *
 */
public class OuterWall extends GameEntity{

	public OuterWall(CardinalDirection directionFacing) {
		super(directionFacing);
	}


	@Override
	public RenderOuterWall generateDrawableCopy() {
		return new RenderOuterWall(this.getFacingCardinalDirection());//TODO: should be a wall
	}

	@Override
	public String toXMLString(){
		return "Outer_Wall";
	}
}
