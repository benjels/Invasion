package gamelogic.entities;

import gamelogic.CardinalDirection;

public class Pylon extends GameEntity implements Attackable{

	private int healthPercentage = 100;//health inited to 100
	
	public Pylon(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderPylon(this.getFacingCardinalDirection(), "I need to defend this.");
	}

	@Override
	public void takeDamage(int pureDamageAmount) {
		//for now just deal damage directly
		throw new RuntimeException("damaging not implemented yet");
	}

	public int getHealthPercentage() {
		return healthPercentage;
	}
	
	@Override
	public String toString(){
		return "Pylon";
	}


}
