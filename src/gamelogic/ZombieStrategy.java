package gamelogic;

import gamelogic.events.MeleeAttackEvent;
import gamelogic.events.PlayerEvent;

//STRATEGY FOR A MORE ADVANCED NPC THAT FOLLOWS A PLAYER AROUND AND TRYS TO HURT THEM
public class ZombieStrategy extends Thread implements AiStrategy{
	
	
	
	private final IndependentActor actorIGenerateEventsFor;
	private final int EXPLOSION_ATTACK_DAMAGE = 100;

	
	
	public ZombieStrategy(IndependentActor actorIAmStrategyFor){
		this.actorIGenerateEventsFor = actorIAmStrategyFor;
	}
	
	
	//CONSTANTLY GENERATE EVENTS THAT GET SCRAPED TO BE APPLIED ON EVERY TICK
	@Override
	public void run() {
		while (true) {
			// we are looping ucontinuosly to generate a relevant event for the enemy that this strategy is attached to
			try {

				Thread.sleep(2000); //only tries to do something
				if(this.actorIGenerateEventsFor.getCurrentRoom().pylonAttackerStuck(this.actorIGenerateEventsFor)){
					this.actorIGenerateEventsFor.killActor();
				}
			} catch (InterruptedException e) {
			System.out.println("thread interrupted");
			}
		}
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

}
