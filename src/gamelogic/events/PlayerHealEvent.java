package gamelogic.events;
//an attempt to use the currently selected item in the inventory with the game world
//resolved in much the same way as melee attack (e.g. check that there is free space in front of the player)
public class PlayerHealEvent extends PlayerEvent implements ClientGeneratedEvent, SpatialEvent {
//TODO: !!! acutally this is just to use the health so should be refactored to UseHealthEvent or some shit
	public PlayerHealEvent(int uid) {
		super(uid);
	}



}
