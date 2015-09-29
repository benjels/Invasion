package gamelogic.events;

import gamelogic.MovementEvent;


/**
 * this is the version of the event that will be submitted to the serverside true game state maintenance class.
 * has the requested event AND the id of the player who made that event
 * @author brownmax1
 *
 */
public class PlayerMoveUp extends PlayerEvent implements  MovementEvent{


	public PlayerMoveUp(int uid){
		super(uid);
	}

}
