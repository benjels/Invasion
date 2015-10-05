package gamelogic.events;

public class UpPushedEvent implements ClientGeneratedEvent {

	protected final int uid;

	public UpPushedEvent(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}
}
