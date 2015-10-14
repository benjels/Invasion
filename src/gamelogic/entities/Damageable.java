package gamelogic.entities;
/**
 * anything in the game world that can meaningfully take damagee
 * @author brownmax1
 *
 */
public interface Damageable {

	/**
	 * does a certain amount of pure damage. Different Attackable entities might deal with this in different ways.
	 * e.g. a pylon that takes 20 damage will lose less health percentage than a zombie that takes 20 damage
	 * @param pureDamagePercentage the pure amount of damage that is taken
	 */
	abstract void takeDamage(int pureDamageAmount);
}
