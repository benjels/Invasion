package gamelogic.entities;

import gamelogic.CardinalDirection;

public class Pylon extends GameEntity implements Damageable{

	private int healthPercentage = 100;//health inited to 100
	
	public Pylon(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderPylon(this.getFacingCardinalDirection());
	}

	@Override
	public void takeDamage(int pureDamageAmount) {
		//the pylon is very resilient, so it only takes a miniscule amount of the pure damage
		double dmgDone = (double)pureDamageAmount;  //(have to convert to double here because attacks should always do SOME damage. if we just have ints, we might divide the damage and then round down to 0)
		dmgDone = Math.ceil(dmgDone / 25);
		this.healthPercentage -= (int)dmgDone;
		//if the pylon just took damage that caused it to die, it's game over
		if(this.healthPercentage <= 0){
			throw new RuntimeException("GAME OVER: you let one of the pylons reach the following health: " + this.healthPercentage);
		}
	}

	public int getHealthPercentage() {
		return healthPercentage;
	}
	
	@Override
	public String toString(){
		return "Pylon";
	}


}
