package gamelogic;

import gamelogic.events.PlayerEvent;
import gamelogic.renderentities.RenderEntity;
/**
 * represents an explosion in the game world.
 * This actor doesn't really have any interesting behaviour at the moment because it basically just
 * serves as an indication that a zombie has exploded. It is managed like any other actor though.
 * @author brownmax1
 *
 */
public class ExplosionStrategy extends Thread implements AiStrategy {

	private IndependentActor actorIGenerateEventsFor;
	private int ticker = 100;

	public ExplosionStrategy(IndependentActor actor) {
		this.actorIGenerateEventsFor = actor;
		//set me as the strategy for my author
		this.actorIGenerateEventsFor.setStrategy(this);
	}


	/**
	 *waits a certain amount of time before "dieing" so that it can be cleaned up
	 */
	@Override
	public void run() {
		//leave the explosion in the game world for a moment
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//before we exit the thread, submit and is dead event to clean this up from the room
		this.actorIGenerateEventsFor.killActor();

		//we are exiting this thread...
	}



	@Override
	public void giveEventToParent(PlayerEvent event) {
		//REDUNDANT

	}

	@Override
	public PlayerEvent determineMove(IndependentActor enemyToMove) {
		//REDUNDANT
		return null;
	}

	@Override
	public int determineActualDamage(int pureDamage) {
		//REDUNDANT
		return 0;
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderExplosion(CardinalDirection.NORTH);
	}

}
