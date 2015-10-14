package gamelogic.entities;

import gamelogic.CardinalDirection;
/**
 * a physical structure in the game that the player must defend for as long as possible
 * @author brownmax1
 *
 */
public class Pylon extends GameEntity implements Damageable{

	private int healthPercentage = 100;//health inited to 100
	private boolean isPylonDead = false;

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
			this.isPylonDead = true;
		}
	}

	public int getHealthPercentage() {
		return healthPercentage;
	}


	public String toXMLString(){
		return "Pylon-" + healthPercentage;
	}

	public boolean isPylonDead(){
		return this.isPylonDead;
	}

}
