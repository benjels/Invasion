package gamelogic.entities;

import gamelogic.events.PlayerEvent;

/**
 * this is the strategy for the most basic kind of enemy that pursues the player and tries to kill them
 * the run() thread in this class continuosly works to place an event in the enemy it belongs to's eventBuffer so that
 * @author brownmax1
 *
 */
public class ZombieStrategy extends Thread implements AiStrategy {

	private IndependentActor actorIGenerateEventsFor;

	/**
	 *
	 * @param zombie needs this zombie so that it can place events that are generated into its buffer
	 */
	public ZombieStrategy(IndependentActor actorIAmStrategyFor) {
		this.actorIGenerateEventsFor = actorIAmStrategyFor;
	}
will need to restructure on the WorldGameState side of things because atm it is just configured for player events rather than for actor events

	@Override
	public void run() {
		while (1 == 1) {
			// we are looping ucontinuosly to generate a relevant event for the enemy that this strategy is attached to
			try {
				Thread.sleep(1000);//TODO: thats a p lazy enemy tbh
				 this.giveEventToParent(this.determineMove(this.actorIGenerateEventsFor));
			} catch (InterruptedException e) {
			//dead code tbh
			}
		}
	}


	@Override
	public void giveEventToParent(PlayerEvent event) {
		// TODO Auto-generated method stub

	}


	@Override
	public PlayerEvent determineMove(IndependentActor enemyToMove) {
		// TODO Auto-generated method stub
		return null;
	}


}
