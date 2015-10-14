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
	private int LastRoomIn; //when a zombie enters a new room, they set this field. and every time they move they check if they have entered a new room and need to retarget



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

			/*	//if our target was a movable entity that moved to another room, we need to pick a new target OR IF WE ENTERED A NEW ROOM
				if((this.currentTarget instanceof MovableEntity  && ((MovableEntity) this.currentTarget).getCurrentRoom().getId() != this.actorIGenerateEventsFor.getCurrentRoom().getId()) || this.actorIGenerateEventsFor.getCurrentRoom().getId() != this.LastRoomIn){
					//we will be finding a new target
					this.currentTarget = null;
					//we might have changed room so set that
					this.LastRoomIn = this.actorIGenerateEventsFor.getCurrentRoom().getId();
					System.out.println("                                   SETTING ROOM IM IN TO: " + this.LastRoomIn);
				}

				if(this.actorIGenerateEventsFor.getCurrentRoom().getId() == 5){
					throw new RuntimeException("-----------------------------------------reached room 2 but didnt set it to room im in");
				}*/

				//if we do not have a target at the moment, we will need to choose one
				if(this.currentTarget == null){
					this.currentTarget = findTargetInRoom();
				}
				assert(this.currentTarget != null):"we should def have a target by now";

				//if our target is a movable entity, and we are close to them, we should explode :)

				int xDiff = Math.abs(this.currentTarget.getxInRoom() - this.actorIGenerateEventsFor.getxInRoom());
				int yDiff = Math.abs(this.currentTarget.getyInRoom() - this.actorIGenerateEventsFor.getyInRoom());
				if(this.currentTarget instanceof MovableEntity && (xDiff <= 1 && yDiff <= 1)){
					//throw new RuntimeException("yeah just exploded fam");
					System.out.println("yeah just exploded");
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
					//System.out.println("KEEPING THE SAME TARGET");
				}else{//if our target is not a player, get a new target on next run through
					//assert(this.currentTarget instanceof StandardTeleporter): "should be a tele but its: " + this.currentTarget;

					System.out.println("should be a tele but its: " + this.currentTarget);
					System.out.println("and i am at position: " + this.actorIGenerateEventsFor.getxInRoom() + "." + actorIGenerateEventsFor.getyInRoom() +" in room:" +this.actorIGenerateEventsFor.getCurrentRoom().getId());
					if(this.actorIGenerateEventsFor.getxInRoom() == 21 && this.actorIGenerateEventsFor.getyInRoom() == 20){
						System.out.println("so im at 21, 20 and the thing beneath me which seems to be the target is: " + this.actorIGenerateEventsFor.getCurrentRoom().getEntities()[21][21]);
						System.out.println("i think that i should be going to 1, 21 where there is: " + this.actorIGenerateEventsFor.getCurrentRoom().getEntities()[1][21]);
					}
					this.currentTarget = null;
				}


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
		if(this.actorIGenerateEventsFor.getCurrentRoom().getId() == 2  && entitiesToSearch[21][21] instanceof Teleporter){
			throw new RuntimeException("but theres nothing thereee");
		}else{
			System.out.println("we shouldnt go to 21 21 because at that position there is: " + entitiesToSearch[21][21]);
		}

		//set the target to null
		Locatable targetTemp = null;
		for(int i = 0; i < entitiesToSearch.length ; i ++){
			for(int j = 0; j < entitiesToSearch[0].length; j ++){//todo use pathfinding to find closest target. just use djikstra'a method and check amount of steps to each target
				//if we havent found a meaty target yet, a teleporter will do
				if(targetTemp == null && entitiesToSearch[i][j] instanceof StandardTeleporter){
					if(this.actorIGenerateEventsFor.getCurrentRoom().getId() == 2){
						System.out.println("----------------------------------------we are choosing a target in room and we decided on " + j +"." + i);
					}
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
			throw new RuntimeException("no target founddd we should prob suicide");
			//this.actorIGenerateEventsFor.killActor();
		}


		if(this.actorIGenerateEventsFor.getCurrentRoom().getId() == 2){
			System.out.println("i am in the room with id 2 and setting my target to: " + targetTemp.getxInRoom() + "." + targetTemp.getyInRoom() +" because at that position there is a:" + targetTemp);
		}


		return targetTemp;

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
