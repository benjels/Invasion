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

	public OuterWall(){
		super(CardinalDirection.NORTH);
	}

	private CardinalDirection directionFaced; //TODO: not sure how this will work. imo a good idea would be to have a separate object for WallCorner too.
	//imo the directionFaced for a wall is the direction that


	@Override
	public CardinalDirection getFacingCardinalDirection() {
		return this.directionFaced;
	}

	@Override
	public void setFacingCardinalDirection(CardinalDirection facingCardinalDirection) {
		this.directionFaced = facingCardinalDirection;
	}

	@Override
	public RenderImpassableColomn generateDrawableCopy() {
		return new RenderImpassableColomn(this.getFacingCardinalDirection());//TODO: should be a wall
	}
}
