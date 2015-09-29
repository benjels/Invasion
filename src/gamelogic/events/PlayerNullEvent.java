package gamelogic.events;


/**
 * represents a non-event.
 * prefer to do this with a designated "null event" object rather
 * than just using null because there is less ambiguity.
 * 
 * NOTE THAT THE ID 0 IS RESERVED FOR THE NULL EVENT
 * @author brownmax1
 *
 */
public class PlayerNullEvent extends PlayerEvent {

	public PlayerNullEvent(int uid) {
		super(uid);
	}



}
