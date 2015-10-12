package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.PylonRoomState;
import gamelogic.events.*;


/**
 *this is the strategy for the very basic npc that spawns in the same row or col as a pylon and slowly moves  in a straight line towards the pylon until it is reached
 *if it somehow loses its course (e.g. the player uses a portal to misdirect it), it will die when it reaches a non traversable entity that is not a pylon
 *in this way, the sorcerer can defend a pylon without using any actual weapons
 * @author brownmax1
 *
 */
public class PylonAttackerStrategy extends Thread implements AiStrategy {

	private final IndependentActor actorIGenerateEventsFor;
	private final CardinalDirection directionPylonIn;
	private final PlayerEvent moveIDo; //each pylon attacker only ever moves in one direction
	private final int MELEE_ATTACK_DAMAGE = 50; //this will be like 40 or some shit




	/**
	 *
	 * @param directionPylonIsIn  the direction the pylon is in when the pylon attacker is spawned
	 * @param zombie needs this zombie so that it can place events that are generated into its buffer
	 */
	public PylonAttackerStrategy(IndependentActor actorIAmStrategyFor, CardinalDirection directionPylonIsIn) {
		this.actorIGenerateEventsFor = actorIAmStrategyFor;
		this.directionPylonIn = directionPylonIsIn;
		//set me as the strategy for my actor
		this.actorIGenerateEventsFor.setStrategy(this);
		//set the kind of movement i need to do which is dependent on the direction i am facing
		switch (directionPylonIsIn){
		case NORTH :
			this.moveIDo = new PlayerMoveUp(actorIAmStrategyFor.getUniqueId());
			break;
		case EAST:
			this.moveIDo = new PlayerMoveRight(actorIAmStrategyFor.getUniqueId());
			break;
		case SOUTH:
			this.moveIDo = new PlayerMoveDown(actorIAmStrategyFor.getUniqueId());
			break;
		case WEST:
			this.moveIDo = new PlayerMoveLeft(actorIAmStrategyFor.getUniqueId());
			break;
		default:
			throw new RuntimeException("the direction of the actor this strategy is attached to must be set to a cardinal direction");
		}
	}




	//FOR EVENT GENERATION///

	@Override
	public void run() {
		while (true) {
			// we are looping ucontinuosly to generate a relevant event for the enemy that this strategy is attached to
			try {
				 //if at the end of a run of this method, the pylon attacker failed to move and failed to attack (because it is trying to move into and attack some entity that is not Traversable or Damageable, then it dies.
				this.giveEventToParent(this.moveIDo);
				Thread.sleep(2000); //only tries to do something
				this.giveEventToParent(new MeleeAttackEvent(this.actorIGenerateEventsFor.getUniqueId(), MELEE_ATTACK_DAMAGE));
				Thread.sleep(2000); //only tries to do something
				if(this.actorIGenerateEventsFor.getCurrentRoom().pylonAttackerStuck(this.actorIGenerateEventsFor)){
					this.actorIGenerateEventsFor.killActor();
				}
			} catch (InterruptedException e) {
			System.out.println("thread interrupted");
			}
		}
	}







	@Override //might be superfluos. maybe jsut have helpers in more adivanced enemies and take this out of the abstract class so its not inherited
	public PlayerEvent determineMove(IndependentActor enemyToMove) {
		return null;
	}


	@Override
	public void giveEventToParent(PlayerEvent event) {
		this.actorIGenerateEventsFor.setBufferedEvent(event);

	}




	///FOR THINGS DONE TO ME///


	@Override
	public int determineActualDamage(int pureDamage) {
		//the pylon attacker is not very resistant to damage. but tougher than the normal zombie
		double dmgDone = (double)pureDamage;  //(have to convert to double here because attacks should always do SOME damage. if we just have ints, we might divide the damage and then round down to 0)
		dmgDone = Math.ceil(dmgDone / 5);
		return (int)dmgDone;
	}


}
