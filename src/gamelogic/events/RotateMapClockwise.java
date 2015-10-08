package gamelogic.events;
//THE EVENT THAT IS GENERATED WHEN THE PLAYER CHOOSES TO ROTATE THE MAP
public class RotateMapClockwise extends PlayerEvent implements ClientGeneratedEvent{

	public RotateMapClockwise(int uid) {
		super(uid);
	}

	@Override
	public int getUid() {
		return 0;
	}





}
