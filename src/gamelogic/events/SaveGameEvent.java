package gamelogic.events;

public class SaveGameEvent extends PlayerEvent implements ClientGeneratedEvent{

	public SaveGameEvent(int uid) {
		super(uid);
	}

	@Override
	public int getUid() {//NOT SURE IF ACTUALLY NECESSARY INFO FOR SAVIN
		return 0;
	}

}
