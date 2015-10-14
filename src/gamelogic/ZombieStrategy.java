package gamelogic;

import java.util.HashMap;

import gamelogic.entities.GameEntity;
import gamelogic.entities.Targatable;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.events.MeleeAttackEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.renderentities.RenderEntity;
import gamelogic.renderentities.RenderPylonAttacker;

/**
 * the strategy that governs the zombie behaviour of an independent actor. this strategy uses a graph to pathfind to its current target (usually a player)
 * and then explodes.
 * @author brownmax1
 *
 */
public class ZombieStrategy extends Thread implements AiStrategy{



	private final IndependentActor actorIGenerateEventsFor;
	private final int EXPLOSION_ATTACK_DAMAGE = 100;
	private Targatable currentTarget;
	private int LastRoomIn; //when a zombie enters a new room, they set this field. and every time they move they check if they have entered a new room and need to retarget



	public ZombieStrategy(IndependentActor actorIAmStrategyFor){
		this.actorIGenerateEventsFor = actorIAmStrategyFor;

		//set me as the strategy for my actor
		this.actorIGenerateEventsFor.setStrategy(this);
	}


	/**
	 * constantly generate events that will be scraped on every tick
	 */
	@Override
	public void run() {
		while (true) {

			try {


				//if we do not have a target at the moment, we will need to choose one
				if(this.currentTarget == null){
					this.currentTarget = findTargetInRoom();
				}
				assert(this.currentTarget != null):"we should def have a target by now";

				//if our target is a movable entity, and we are close to them, we should explode :)

				int xDiff = Math.abs(this.currentTarget.getxInRoom() - this.actorIGenerateEventsFor.getxInRoom());
				int yDiff = Math.abs(this.currentTarget.getyInRoom() - this.actorIGenerateEventsFor.getyInRoom());
				if(this.currentTarget instanceof MovableEntity && (xDiff <= 1 && yDiff <= 1)){
					this.actorIGenerateEventsFor.killActor();
				}else{//if we didn't explode, we should use pathfinding to choose where to move to to reach our target

					//create a graph from the current state of our current room
					RoomMovementGraph graph = this.actorIGenerateEventsFor.getCurrentRoom().generateMovementGraph();

					//move in the direction that the shortest path to target dictates
					this.giveEventToParent((PlayerEvent) graph.getShortestPathMove(this.actorIGenerateEventsFor.getxInRoom(), this.actorIGenerateEventsFor.getyInRoom(), this.currentTarget.getxInRoom(), this.currentTarget.getyInRoom(),  this.actorIGenerateEventsFor.getUniqueId()));
				}



				//so at this point we have either exploded if we reached our target player/actor or we have made a move towards our target.

				//we should not choose a new target if our target is a player who is still in the same room
				if(this.currentTarget instanceof Player && ((Player) this.currentTarget).getCurrentRoom().getId() == this.actorIGenerateEventsFor.getCurrentRoom().getId()){
					//do nothing
				}else{//if our target is not a player, get a new target on next run through
					this.currentTarget = null;
				}


				//if the actor who this thread belongs to is dead, we should stop running this thread
				if(this.actorIGenerateEventsFor.isDead()){
					break;
				}

				Thread.sleep(300); //the interval at which this actor attempts an event

			} catch (InterruptedException e) {
			System.out.println("thread interrupted" + e);
			}
		}
	}




	/**
	 * used to determine what the target of this zombie currently is. preferes players over teleporters.
	 * @return the thing to target
	 */
	private Targatable findTargetInRoom() {
		assert(this.currentTarget == null):"why are we looking for a target if we already have one (MAYBE IF OUR TARGET REACHED A DIFFERENT ROOMMM)?";
		//we will search the room we are in and decide on the thing that we want to get to the most
		GameEntity[][] entitiesToSearch = this.actorIGenerateEventsFor.getCurrentRoom().getEntities();
		if(this.actorIGenerateEventsFor.getCurrentRoom().getId() == 2  && entitiesToSearch[21][21] instanceof Teleporter){
			throw new RuntimeException("but theres nothing thereee");
		}

		//set the target to null
		Targatable targetTemp = null;
		for(int i = 0; i < entitiesToSearch.length ; i ++){
			for(int j = 0; j < entitiesToSearch[0].length; j ++){//todo use pathfinding to find closest target. just use djikstra'a method and check amount of steps to each target
				//if we havent found a meaty target yet, a teleporter will do
				if(targetTemp == null && entitiesToSearch[i][j] instanceof StandardTeleporter){
					targetTemp = (StandardTeleporter) entitiesToSearch[i][j];
				}
				//if we find a Player, go attack them
				if(entitiesToSearch[i][j] instanceof Player){
					targetTemp = (Player) entitiesToSearch[i][j];
					break;
				}
			}
		}

		//if the target is still null, it means we did not find anything to move towards, so suicide
		if(targetTemp == null){
			this.actorIGenerateEventsFor.killActor();
		}



		return targetTemp;

	}


	@Override
	public void giveEventToParent(PlayerEvent event) {
		this.actorIGenerateEventsFor.setBufferedEvent(event);

	}



	@Override
	public int determineActualDamage(int pureDamage) {
		//the standard zombie is relatively weak
		double dmgDone = (double)pureDamage;  //(have to convert to double here because attacks should always do SOME damage. if we just have ints, we might divide the damage and then round down to 0)
		dmgDone = Math.ceil(dmgDone / 2);
		return (int)dmgDone;
	}


	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderZombieAttacker(this.actorIGenerateEventsFor.getFacingCardinalDirection());
	}



	@Override
	public PlayerEvent determineMove(IndependentActor enemyToMove) {
		//all events are generated in run thread for this class
		return null;
	}


}
