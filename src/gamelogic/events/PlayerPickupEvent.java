package gamelogic.events;
/**
 * an attempt to pick up the entity that that player is standing on
 * @author Max Brown
 *
 */
public class PlayerPickupEvent extends PlayerEvent {

	public PlayerPickupEvent(int uid) {
		super(uid);
	}

}
