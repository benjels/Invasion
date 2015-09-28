package gamelogic;

public class NullEntity extends GameEntity implements TraversableEntity{



	public NullEntity(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	public RenderNullEntity generateDrawableCopy() {
		return new RenderNullEntity(this.getFacingCardinalDirection());
	}



}
