package gamelogic.events;

public class CarrierOpenEvent extends PlayerEvent implements CarrierOpenCloseEvent, ClientGeneratedEvent {

	public CarrierOpenEvent(int uid) {
		super(uid);
	}

}
