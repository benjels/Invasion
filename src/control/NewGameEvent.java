package control;

import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;

public class NewGameEvent extends PlayerEvent implements ClientGeneratedEvent {

	public NewGameEvent(int uid) {
		super(uid);
	}

}
