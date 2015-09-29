package gamelogic.events;

import gamelogic.MovementEvent;


/**
 * this is the version of the event that will be submitted to the serverside true game state maintenance class.
 * has the requested event AND the id of the player who made that event
 * @author brownmax1
 *
 */
public class IDedPlayerMoveDown implements IDedPlayerEvent, MovementEvent{


	private final int Uid;//the unique id of the player who sent this event to the server

	public IDedPlayerMoveDown(int Uid){
		this.Uid = Uid;
	}

	public int getUid(){
		return this.Uid;
	}

}
