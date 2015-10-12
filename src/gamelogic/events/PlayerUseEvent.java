package gamelogic.events;
//an attempt to use the currently selected item in the inventory with the game world
//resolved in much the same way as melee attack (e.g. check that there is free space in front of the player)
public class PlayerUseEvent extends PlayerEvent implements ClientGeneratedEvent, SpatialEvent {

	public PlayerUseEvent(int uid) {
		super(uid);
	}



}
