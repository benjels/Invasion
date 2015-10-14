package gamelogic.events;
/**
 * represents any event that a movable entity in the game might want to perform.
 * like moving or attacking etc
 * @author brownmax1
 *
 */

public class PlayerEvent {

	protected final int uid;

	public PlayerEvent(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}

}