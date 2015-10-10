package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerNullEvent;

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

//note that the IndependentActorManager is responsible for spawning the ais. the server is responsible for spawning players. At the highest level that is.


public class IndependentActor extends MovableEntity implements Damageable{//this should implement some interface "independentActor" which is the type that has its buffer scraped after the players'. this interface would also define some public placeEventInBuffer() method so that the enemy's behaviour strategy can place events into the buffer to be scraped

	AiStrategy currentBehaviour;//the strategy being used to generate events for this MovableEntity
	//TODO: maybe have another kind of strategy for taking damage? nah prob just keep it simple as fuck and just keep strategies for which event performed (this goes for Players too) and then just declare an abstract takeHit(int dmg) method in MovableEntity)
	private PlayerEvent bufferedEvent = new PlayerNullEvent(0);
	private int healthPercentage = 100;

	public IndependentActor(CardinalDirection directionFacing, int uid) {
		super(directionFacing, uid);
		this.currentBehaviour = new PylonAttackerStrategy(this);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderZombie(this.getFacingCardinalDirection());
	}

	/**
	 * used to start the strategy for this entity that just keeps on generating events
	 */
	public void beginAi() {
		if(this.currentBehaviour instanceof PylonAttackerStrategy){
			((PylonAttackerStrategy) this.currentBehaviour).start();
		}else{
			throw new RuntimeException("no other strats supported atm");
		}
	}

	/**
	 * return the event currently in the buffer
	 * used by the main game thread to get the event that this entity should perform
	 * @return the event to be performed
	 */
	public PlayerEvent scrapeEnemyEvent() {
		//if the event isnt null, return it
		if(!(this.bufferedEvent instanceof PlayerNullEvent)){
			PlayerEvent temp = this.bufferedEvent;
			this.bufferedEvent = new PlayerNullEvent(0);
			return temp;
		}else{
			return this.bufferedEvent;
		}
		
	}

	/**
	 * set the event in this object's buffer to some MovableEntityEvent
	 * @param event the event to place in the buffer
	 */
	public void setBufferedEvent(PlayerEvent event) {
		this.bufferedEvent = event;
		
	}

	/**
	 * determines whether this actor has generated an event to be scraped
	 * @return true if the event in the buffer is non NullEvent event, else false
	 */
	public boolean hasEvent() {
		return !(this.bufferedEvent instanceof PlayerNullEvent);
	}

	@Override
	public void takeDamage(int pureDamageAmount) {
		//the actual damage taken is dependent on the strategy of this Actor
		this.healthPercentage -= this.currentBehaviour.determineActualDamage(pureDamageAmount);
		//if we killed this actor, we need to remove them from the game or some shit eh
		if(this.healthPercentage <= 0){
			throw new RuntimeException("just killed this actor by setting their health to: " + this.healthPercentage);
		}
		}


}
