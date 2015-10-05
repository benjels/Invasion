package gamelogic.events;

public class RightPushedEvent implements ClientGeneratedEvent {

	protected final int uid;

	public RightPushedEvent(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}
}
