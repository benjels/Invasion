package gamelogic;

import java.util.HashMap;

import gamelogic.entities.GameEntity;
import gamelogic.entities.Locatable;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderPylonAttacker;
import gamelogic.events.MeleeAttackEvent;
import gamelogic.events.PlayerEvent;

//STRATEGY FOR A MORE ADVANCED NPC THAT FOLLOWS A PLAYER AROUND AND TRYS TO HURT THEM
public class ZombieStrategy extends Thread implements AiStrategy{
	
	
	
	private final IndependentActor actorIGenerateEventsFor;
	private final int EXPLOSION_ATTACK_DAMAGE = 100;
	private Locatable currentTarget; //i.e. the entity that this zombie is trying to hurt

	
	
	public ZombieStrategy(IndependentActor actorIAmStrategyFor){
		this.actorIGenerateEventsFor = actorIAmStrategyFor;
		
		//set me as the strategy for my actor
		this.actorIGenerateEventsFor.setStrategy(this);
	}
	
	
	//CONSTANTLY GENERATE EVENTS THAT GET SCRAPED TO BE APPLIED ON EVERY TICK
	@Override
	public void run() {
		while (true) {
			// we are looping continuosly to generate a relevant event for the enemy that this strategy is attached to
			try {
				
				//if our target was a movable entity that moved to another room, we need to pick a new target
				if(this.currentTarget instanceof MovableEntity  && ((MovableEntity) this.currentTarget).getCurrentRoom().getId() != this.actorIGenerateEventsFor.getCurrentRoom().getId()){
					this.currentTarget = null;
				}
				
				//if we do not have a target at the moment, we will need to choose one
				if(this.currentTarget == null){
					this.currentTarget = findTargetInRoom();
				}
				assert(this.currentTarget != null):"we should def have a target by now";
				
				//if our target is a movable entity, and we are close to them, we should explode :)
				
				int xDiff = Math.abs(this.currentTarget.getxInRoom() - this.actorIGenerateEventsFor.getxInRoom());
				int yDiff = Math.abs(this.currentTarget.getyInRoom() - this.actorIGenerateEventsFor.getyInRoom());
				if(this.currentTarget instanceof MovableEntity && (xDiff <= 1 || yDiff <= 1)){
					throw new RuntimeException("yeah just exploded fam");
				}else{//if we didn't explode, we should use pathfinding to choose where to move to to reach our target
				
					//create a graph from the current state of our current room
					RoomMovementGraph graph = this.actorIGenerateEventsFor.getCurrentRoom().generateMovementGraph();
					
					//move in the direction that the shortest path to target dictates
					this.giveEventToParent((PlayerEvent) graph.getShortestPathMove(this.actorIGenerateEventsFor.getxInRoom(), this.actorIGenerateEventsFor.getyInRoom(), this.currentTarget.getxInRoom(), this.currentTarget.getyInRoom(),  this.actorIGenerateEventsFor.getUniqueId()));
				}
				
				
				
				
				
				
				//so at this point we have either exploded if we reached our target player/actor or we have made a move towards our target.
				//TODO: WE NEED TO SET TARGET TO NULL IF WE WENT THROUGH A TELEPORTER!!!
				
				
				
				
				//if the actor who this thread belongs to is dead, we should stop running this thread
				if(this.actorIGenerateEventsFor.isDead()){//TODO this is actually kinda confusing. is it possible to to damange and kill an enemy while it is executing its run loop and then when it tries to look itself up or what room its in or watev it will generate a null pointer because it has been taken out of the game? 
															//SHOULD CHECK BEFORE YOU ACCESS ANYTHING OUTSIDE OF THIS CLASS WITH if !this.actorIAmFor.isDead() or s/t
					break;									//could also resolve this by just getting this actor to generate an event immediately after each tick? the board state isnt actually gonna change between ticks anyway lul
				}
				
				Thread.sleep(500); //only tries to do something every 500 ms
				
			} catch (InterruptedException e) {
			System.out.println("thread interrupted");
			}
		}
	}
	

	
	
	//USED TO DETERMINE WHICH TARGET THIS ACTOR SHOULD BE TRYING TO REACH
	//WHEN IT MOVES. picks a player in this room. if there are no players in this room, picks a teleporter.
	//if there are no teleporters or players, it should explode
	private Locatable findTargetInRoom() {
		assert(this.currentTarget == null):"why are we looking for a target if we already have one (MAYBE IF OUR TARGET REACHED A DIFFERENT ROOMMM)?";
		//we will search the room we are in and decide on the thing that we want to get to the most
		GameEntity[][] entitiesToSearch = this.actorIGenerateEventsFor.getCurrentRoom().getEntities();
		
		//set the target to null
		Locatable target = null;
		for(int i = 0; i < entitiesToSearch.length ; i ++){
			for(int j = 0; j < entitiesToSearch[0].length; j ++){//todo use pathfinding to find closest target. just use djikstra'a method and check amount of steps to each target
				//if we havent found a meaty target yet, a teleporter will do
				if(target == null && entitiesToSearch[j][i] instanceof StandardTeleporter){
					target = (Locatable) entitiesToSearch[j][i];
				}
				//if we find a Player, go attack them
				if(entitiesToSearch[j][i] instanceof Player){
					target = (Locatable) entitiesToSearch[j][i];
					break;
				}
			}
		}
		
		//if the target is still null, it means we did not find anything to move towards, so suicide
		if(target == null){
			throw new RuntimeException("no target founddd we should prob suicide");
			//this.actorIGenerateEventsFor.killActor();
		}
		
		return target;
		
	}


	@Override
	public void giveEventToParent(PlayerEvent event) {
		this.actorIGenerateEventsFor.setBufferedEvent(event);
		
	}

	@Override //DELETE FROM SUPER CLASS/interface  IF THIS IS UNUSED because i man ot using it in the other independennt actor eitehr
	public PlayerEvent determineMove(IndependentActor enemyToMove) {
		// TODO Auto-generated method stub
		return null;
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

	

}
