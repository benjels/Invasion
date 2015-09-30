package gamelogic.events;

/**
 * represents the event where a player has clicked/selected a particular slot on their inventory.
 * (most likely because they want to drop/use that item)
 * @author user
 *
 */
public class PlayerSelectInvSlot4 extends PlayerEvent implements InventorySelectionEvent {

	private final int SLOT_NUMBER = 4;
	
	
	public PlayerSelectInvSlot4(int uid) {
		super(uid);

	}

	@Override
	public int getSlot() {
		return this.SLOT_NUMBER;
	}

	
	
	
	
	
}
