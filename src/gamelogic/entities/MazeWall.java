package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.renderentities.RenderMazeWall;

/**
 * a colomn in a room that is an obstruction
 *
 * @author brownmax1
 *
 */
public class MazeWall extends GameEntity{


	public MazeWall(CardinalDirection directionFacing){
		//just pass the initial direction to enclosing parent class
		super(directionFacing);
	}



	@Override
	public RenderMazeWall generateDrawableCopy() {
		return new RenderMazeWall(this.getFacingCardinalDirection());
	}



	@Override
	public String toXMLString() {
		return "Maze_Wall";
	}
}
