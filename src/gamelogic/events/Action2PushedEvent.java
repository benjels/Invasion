package gamelogic.events;

public class Action2PushedEvent implements ClientGeneratedEvent {

	protected final int uid;

	public Action2PushedEvent(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}
	
}
