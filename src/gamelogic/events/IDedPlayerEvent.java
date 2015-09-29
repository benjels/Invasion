package gamelogic.events;
/**
 * an event that is created from the encoded bits on the server side of the program.
 *  A desired event that has been requested by a specific player.
 * @author brownmax1
 *
 */
public interface IDedPlayerEvent {

	/**
	 * used to identify which player tried to cause this event
	 * @return int the unique id of the player who requested this event
	 */
	public abstract int getUid();

}