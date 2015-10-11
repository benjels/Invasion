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
	}

	
	
	
	//FOR EVENT GENERATION///

	@Override
	public void run() {
		while (1 == 1) {
			// we are looping ucontinuosly to generate a relevant event for the enemy that this strategy is attached to
			try {
//	eat shit			this.actorIGenerateEventsFor.getCurrentRoom() use this shit to attempt to continually attempt to mvoe up and then attack. need a helper method in PylonRoomState "checkWhetherAttackerSTuck" which makes this actor die if the thing in from of them is not traversable or damageable
//	...			other than that this piece of shit just moves in the direction that it is facing when it is spawned and then attacks until it dies
//		...		this piece of shit will generate some kind of meleeEvent with a certain damage and that event will be resolved in the standard way by RoomState
				/*Thread.sleep(1000);//TODO: thats a p lazy enemy tbh
				 this.giveEventToParent(new PlayerMoveUp(this.actorIGenerateEventsFor.getUniqueId()) this.determineMove(this.actorIGenerateEventsFor)); //TODO: HARDCODED THIS SO THAT HE CAN JUST MOVE UP AND DOWN
				 Thread.sleep(1000);//TODO: thats a p lazy enemy tbh
				 this.giveEventToParent(new PlayerMoveRight(this.actorIGenerateEventsFor.getUniqueId()) this.determineMove(this.actorIGenerateEventsFor));
				 Thread.sleep(1000);//TODO: thats a p lazy enemy tbh
				 this.giveEventToParent(new PlayerMoveLeft(this.actorIGenerateEventsFor.getUniqueId()) this.determineMove(this.actorIGenerateEventsFor));
				
				 this.giveEventToParent(new PlayerMoveDown(this.actorIGenerateEventsFor.getUniqueId()) this.determineMove(this.actorIGenerateEventsFor)); //TODO: HARDCODED THIS SO THAT HE CAN JUST MOVE UP AND DOWN
*/			
				 Thread.sleep(1000);//TODO: remove this when uncomment the rest fam
			} catch (InterruptedException e) {
			//dead code tbh
			}
		}
	}


	
	@Override
	public PlayerEvent determineMove(IndependentActor enemyToMove) {
		//pathfinding ai goes here fam
		throw new RuntimeException("not imp yet");
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
