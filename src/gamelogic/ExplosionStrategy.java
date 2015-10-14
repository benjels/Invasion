package gamelogic;

import gamelogic.entities.RenderEntity;
import gamelogic.events.PlayerEvent;

public class ExplosionStrategy extends Thread implements AiStrategy {

	private IndependentActor actorIGenerateEventsFor;

	public ExplosionStrategy(IndependentActor actor) {
		this.actorIGenerateEventsFor = actor;
		//set me as the strategy for my author
		this.actorIGenerateEventsFor.setStrategy(this);
	}

	@Override
	public void giveEventToParent(PlayerEvent event) {
		//unused here

	}

	@Override
	public PlayerEvent determineMove(IndependentActor enemyToMove) {
		//unused here
		return null;
	}

	@Override
	public int determineActualDamage(int pureDamage) {
		//not used
		return 0;
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderExplosion(CardinalDirection.NORTH);
	}

}
