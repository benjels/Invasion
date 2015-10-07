package gamelogic.events;

public class CarrierCloseEvent extends PlayerEvent implements CarrierOpenCloseEvent, ClientGeneratedEvent {

	public CarrierCloseEvent(int uid) {
		super(uid);
	}

}
