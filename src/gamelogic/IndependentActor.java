package gamelogic;

import gamelogic.entities.Damageable;
import gamelogic.entities.MovableEntity;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerNullEvent;
import gamelogic.renderentities.RenderEntity;
import gamelogic.renderentities.RenderPylonAttacker;

/**
 * a MovableEntity that exists in the game world but is not controlled directly by any of the players. This actor has a currentBehaviour field which generates events
 * for this actor to submit to the game world. This use of the Strategy pattern makes it very easy to create and implement new behaviours for NPCs in the game. it is even easily
 * possible to switch the kind of behaviour that an actor has on the fly (although this is not currently used in our game)
 * @author brownmax1
 *
 */


public class IndependentActor extends MovableEntity implements Damageable{

	private AiStrategy currentBehaviour = null;//the strategy currently being used to generate events for this MovableEntity
	private PlayerEvent bufferedEvent = new PlayerNullEvent(0); //the event currently in this actor's buffer. it will be scraped when the server ticks and we will try to add it to the game state.
	private int healthPercentage = 100;
	private boolean isDead = false;



	public IndependentActor(CardinalDirection directionFacing, int uid, RoomState spawnRoom) {
		super(directionFacing, uid, spawnRoom);

	}


	/**
	 * sets the strategy of this actor which defines its behaviour
	 * @param strat the strategy that this actor should use to generate its events
	 *
	 */
	public void setStrategy(AiStrategy strat){
		assert(getCurrentBehaviour() == null):"we are only setting each behaviour once atm but it is already set to: " + getCurrentBehaviour().getClass();
		this.setCurrentBehaviour(strat);
	}



	/**
	 * starts the run thread inside this actor's ai which will make it start generating events to be applied to the game state
	 */
	protected void beginAi() {
		assert(getCurrentBehaviour() != null):"tried to start an ai when our ai is set to null";
		if(this.getCurrentBehaviour() instanceof PylonAttackerStrategy){
			((PylonAttackerStrategy) this.getCurrentBehaviour()).start();
		}else if(this.getCurrentBehaviour() instanceof ZombieStrategy){
			((ZombieStrategy) this.getCurrentBehaviour()).start();
		}else if(this.getCurrentBehaviour() instanceof ExplosionStrategy){
			((ExplosionStrategy) this.getCurrentBehaviour()).start();
		}else{
			throw new RuntimeException("no other strats supported atm");
		}
	}

	/**
	 * set the event in this object's buffer.
	 * used by the current ai strategy to set which event the actor sohuld perform next.
	 * @param event the event to place in the buffer
	 */
	public void setBufferedEvent(PlayerEvent event) {
		this.bufferedEvent = event;

	}



	/**
	 * determines whether this actor has generated an event to be scraped
	 * used by the actor manager to determine which events to apply.
	 * @return true if the event in the buffer is non NullEvent event, else false
	 */
	protected boolean hasEvent() {
		return !(this.bufferedEvent instanceof PlayerNullEvent);
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
	 * takes some health from this actor. the proportion of pure damage that is subtracted
	 * from this actor's current health is dependent on this actor's strategy
	 */
	@Override
	public void takeDamage(int pureDamageAmount) {
		//the actual damage taken is dependent on the strategy of this Actor
		this.healthPercentage -= this.getCurrentBehaviour().determineActualDamage(pureDamageAmount);
		//if we killed this actor, we need to remove them from the game
		if(this.healthPercentage <= 0){
			this.isDead = true;
		}
	}


	/**
	 * marks this actor as dead so that they can cleanly be removed from the game
	 */
	public void killActor() {
		this.isDead = true;
	}




	@Override
	public RenderEntity generateDrawableCopy() {
		return this.currentBehaviour.generateDrawableCopy();
	}



	public String toXMLString(){
		return "Independent_Actor";
	}


	public boolean isDead() {
		return isDead;
	}



	public AiStrategy getCurrentBehaviour() {
		return currentBehaviour;
	}

	public void setCurrentBehaviour(AiStrategy currentBehaviour) {
		this.currentBehaviour = currentBehaviour;
	}






}
