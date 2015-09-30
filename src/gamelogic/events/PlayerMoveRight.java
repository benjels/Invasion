package gamelogic.events;



/**
 * this is the version of the event that will be submitted to the serverside true game state maintenance class.
 * has the requested event AND the id of the player who made that event
 * @author brownmax1
 *
 */
public class PlayerMoveRight extends PlayerEvent implements  SpatialEvent, MovementEvent{


	public PlayerMoveRight(int uid){
		super(uid);
	}

}
