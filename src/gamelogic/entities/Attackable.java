package gamelogic.entities;
//SOMETHING THAT CAN MEANINGFULLY DEAL WITH BEING ATTACKED
public interface Attackable {

	/**
	 * does a certain amount of pure damage. Different Attackable entities might deal with this in different ways.
	 * e.g. a pylon that takes 20 damage will lose less health percentage than a zombie that takes 20 damage
	 * @param pureDamagePercentage the pure amount of damage that is taken 
	 */
	abstract void takeDamage(int pureDamageAmount);
}
