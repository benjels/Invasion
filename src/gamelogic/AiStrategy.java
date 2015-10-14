package gamelogic;

import gamelogic.events.PlayerEvent;
import gamelogic.renderentities.RenderEntity;

/**
 * Classes the implement this interface define a certain set of behaviour of how they interact with the game world. It is in here
 * that we determine what an independent actor should be doing on each tick and how they respond to damage etc
 *
 *
 * @author brownmax1
 *
 */


public interface AiStrategy {


	/**
	 * used to submit an event that this behaviour deemed appropriate to the actor that this behavioour strategy
	 * belongs to
	 * @param event the event that this ai determined the enemy should perform next
	 */
	abstract void giveEventToParent(PlayerEvent event);

	/**
	 * used to generate the event that the attached enemy should perform next
	 * @param enemyToMove the enemy that is performing the event
	 * @return PlayerEvent the event that should be placed in the event buffer of the attached enemy //NOTE THAT OBVS FOR NOW ZOMBIE IS THE ONLY KIND OF ENEMY BUT WE WILL NEED SOME KIND OF IndependentActor interface as this type
	 */
	abstract PlayerEvent determineMove(IndependentActor enemyToMove);


	/**
	 * used to determine what proportion of "pure" damage should be subtracted from the actor who this strategy belongs to
	 * @param pureDamage the amount of pure damage taken
	 * @return the amount of health that should be subtracted
	 */
	abstract int determineActualDamage(int pureDamage);

	abstract RenderEntity generateDrawableCopy();



}
