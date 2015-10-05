package gamelogic.entities;

import javax.xml.bind.annotation.XmlRootElement;

import gamelogic.CardinalDirection;

/**
 * a wall around the edge of the room that marks the edge of
 * the arena
 * @author brownmax1
 *
 */
@XmlRootElement(name = "OuterWall")
public class OuterWall extends GameEntity{

	public OuterWall(CardinalDirection directionFacing) {
		super(directionFacing);
	}


	@Override
	public RenderOuterWall generateDrawableCopy() {
		return new RenderOuterWall(this.getFacingCardinalDirection());//TODO: should be a wall
	}
}
