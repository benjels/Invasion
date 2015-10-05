package gamelogic.events;

public class LeftPushedEvent implements ClientGeneratedEvent {

	protected final int uid;

	public LeftPushedEvent(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}
}
