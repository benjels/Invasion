package gamelogic;

public abstract class DirectionFacer {

	private CardinalDirection directionFaced; //the direction that the entity is currently facing in the game world

//TODO: are these getters/setters accessible from the implementing classes further down the hierarchy

	public DirectionFacer(CardinalDirection directionFacing) {
		this.directionFaced = directionFacing;
	}


	///UTILITY///
	CardinalDirection getFacingCardinalDirection(){
		return this.directionFaced;
	}


	 void setFacingCardinalDirection(CardinalDirection facingCardinalDirection){
		this.directionFaced = facingCardinalDirection;
	 }

}
