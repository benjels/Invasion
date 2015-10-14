package gamelogic.entities;

import gamelogic.CardinalDirection;

/**
 * anything in the game world that can face one of the cardinal directions. e.g. a player
 * @author brownmax1
 *
 */
public abstract class DirectionFacer {


	private CardinalDirection directionFaced; //the direction that the entity is currently facing in the game world



	public DirectionFacer(CardinalDirection directionFacing) {
		this.directionFaced = directionFacing;
	}


	///UTILITY///
	public CardinalDirection getFacingCardinalDirection(){
		return this.directionFaced;
	}




	 public void setFacingCardinalDirection(CardinalDirection facingCardinalDirection){
		this.directionFaced = facingCardinalDirection;
	 }

}
