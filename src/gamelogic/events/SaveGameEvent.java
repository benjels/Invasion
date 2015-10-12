package gamelogic.events;

public class SaveGameEvent implements ClientGeneratedEvent{

	@Override
	public int getUid() {//NOT SURE IF ACTUALLY NECESSARY INFO FOR SAVIN
		return 0;
	}

}
