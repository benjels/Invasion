package gamelogic;
/**
 * represents a non-event.
 * prefer to do this with a designated "null event" object rather
 * than just using null because there is less ambiguity.
 * @author brownmax1
 *
 */
public class IDedPlayerNull implements IDedPlayerEvent {

	@Override
	public int getUid() {
		throw new RuntimeException("cannot get a player's id from a null event");
	}

}
