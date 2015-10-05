package gamelogic.events;

public class Action1PushedEvent implements ClientGeneratedEvent{

	protected final int uid;

	public Action1PushedEvent(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}

}
