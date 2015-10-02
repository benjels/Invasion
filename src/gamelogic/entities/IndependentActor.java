package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.events.PlayerEvent;

/**
 * the prototype enemy that has a dumb asf ai that just moves back and forth
 * @author brownmax1
 *
 */


//NOTE ON ALL THIS SHIT CAUSE ITS KINDA HARD TO KEEP A TRACK OF:
//INDEPENDENT ACTOR: any kind of GameEntity that performs its own actions in the game without a player's input
//this class needs an AI strategy to determine how it should behave/what events it should perform.
//AIStrategy: the strategy that runs in a thread constantly generating events for this actor to perform when its buffer is next scraped .
//we can conceivably have thse for like any kind of entity we want in the game world. we could easily implement npcs that help us just by swapping out this
//strategy so that that IndependentActor pathfinds to the closest enemy instead of the closest Player
//ZombieStrategy: just the prototype AIstrategy that moves up and down for now
//IndependentActorManager: essentially just a container/manager for all of the different IndependetACtors that are currently in the game. gets a pulse() method called upon it when
//the main clock ticks and then goes and scrapes all of the events from all of the IndependentActors. those events are then placed back into the eventQueue with the appropriate ID
//(note that every MovableEntity has a unique id). Then when the events are applied, they are given to the WorldGameState class to apply. that WorldGameState object has a map for
//identifying Movable entities from their id, so it knows what room they are in and how to apply their action etc. Obviously it is critical that this map has all of the Movable entities in it.
//this is easy because the ids (just 1 and 2 for player1 and player2) are passed from the connecting clients/main method and then the IndependentActors are all made and passed to the
//WorldGameStateObject when it is instantiated (we never need to re-create/destroy these objects because their behvaoiur/state is mostly just determined by what their strategy is up to).
//when they "die" or watever, we just move them back to their spawn point.
//so the life cycle of an IndependentActor's attack event goes:
//created by run() method in AiStrategy's thread -> passed back to IndependentActor buffer -> scraped/gathered along with other actor's events in Server tick() -> given to
//WorldGameState object to be applied to game state -> applied to the 2d array/players or watev. then the new version is sent back



public class IndependentActor extends MovableEntity{//this should implement some interface "independentActor" which is the type that has its buffer scraped after the players'. this interface would also define some public placeEventInBuffer() method so that the enemy's behaviour strategy can place events into the buffer to be scraped

	AiStrategy currentBehaviour;

	public IndependentActor(CardinalDirection directionFacing) {
		super(directionFacing);
		this.currentBehaviour = new ZombieStrategy(this);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderZombie(this.getFacingCardinalDirection());
	}

	public void beginAi() {
		// TODO Auto-generated method stub

	}

	public PlayerEvent scrapeEnemyEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
