package gamelogic.events;

public class DownPushedEvent implements ClientGeneratedEvent {

	protected final int uid;

	public DownPushedEvent(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}
}
