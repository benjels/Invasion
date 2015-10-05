package gamelogic.entities;

import gamelogic.events.PlayerEvent;

public interface AiStrategy {
//THIS THE STRATEGY SHIT FOR ENEMIES FAMMMM. SO ALL THE INDIVIDUAL ACTUAL STRATEGY CLASSES WILL EXTEND THREAD AND IMPLEMENT THIS INTERFACE
	//SO THIS INTERFACE WILL JUST DEFINE SOME BEHAVIOUR THAT THE run() METHOD WILL DRAW UPON TO GENERATE THE ENEMY'S EVENTS.
	//E.G. doMovementAlgorithm() will be stipulated in this interface and individual strategies will call their version of it to return an event and
	//that event will be submitted back to reach the server's queue of events. e.g. Zombie's strategy implementation might run a path find towards closest player
	//whereas the PylonKiller enemy might just simply move towards the closest pylon


	/**
	 * used to give an event back to this strategy's enemy object
	 * @param event the event that this ai determined the enemy should perform next
	 */
	abstract void giveEventToParent(PlayerEvent event);

	/**
	 * used to generate the event that the attached enemy should perform next
	 * @param enemyToMove the enemy that is performing the event
	 * @return PlayerEvent the event that should be placed in the event buffer of the attached enemy //NOTE THAT OBVS FOR NOW ZOMBIE IS THE ONLY KIND OF ENEMY BUT WE WILL NEED SOME KIND OF IndependentActor interface as this type
	 */
	abstract PlayerEvent determineMove(IndependentActor enemyToMove);



}
