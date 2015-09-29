package gamelogic.events;

import gamelogic.MovementEvent;
import gamelogic.tiles.TraversableTile;

/**
 * this is the version of the event that will be submitted to the serverside true game state maintenance class.
 * has the requested event AND the id of the player who made that event
 * @author brownmax1
 *
 */
public class IDedPlayerMoveLeft implements IDedPlayerEvent, MovementEvent, TraversableTile{


	private final int Uid;//the unique id of the player who sent this event to the server

	public IDedPlayerMoveLeft(int Uid){
		this.Uid = Uid;
	}

	public int getUid(){
		return this.Uid;
	}

}
