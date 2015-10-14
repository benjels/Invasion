package gamelogic;

import gamelogic.entities.RenderEntity;
import gamelogic.events.PlayerEvent;
/**
 * represents an explosion in the game world.
 * This actor doesn't really have any interesting behaviour at the moment because it basically just
 * serves as an indication that a zombie has exploded. It is managed like any other actor though.
 * @author brownmax1
 *
 */
public class ExplosionStrategy extends Thread implements AiStrategy {

	private IndependentActor actorIGenerateEventsFor;

	public ExplosionStrategy(IndependentActor actor) {
		this.actorIGenerateEventsFor = actor;
		//set me as the strategy for my author
		this.actorIGenerateEventsFor.setStrategy(this);
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
