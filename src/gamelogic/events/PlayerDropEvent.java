package gamelogic.events;
/**
 * event that represents the player attempting to drop their currently selected item
 * @author Max Brown
 *
 */
public class PlayerDropEvent extends PlayerEvent implements SpatialEvent, ClientGeneratedEvent{

	public PlayerDropEvent(int uid) {
		super(uid);
	}

}
