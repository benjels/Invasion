package imagehelper;

import java.util.HashMap;

import javax.swing.ImageIcon;
/**
 * Class to support the drawing of static images to the player Canvas.
 * @author maxcopley
 *
 */
public class PlayerCanvasImagehelper {

	private HashMap<String, ImageIcon> playerCanvasImages;

	public PlayerCanvasImagehelper() {
		this.initializeImageHashMap();
	}
	/**
	 * Hash Map created for efficiency. Hard Coded constructor to add every Game image to a hashmap.
	 *
	 */
	public void initializeImageHashMap(){
		this.setPlayerCanvasImages(new HashMap<String, ImageIcon>());
		ImageIcon inventory = new ImageIcon("images/inventory.png");
		ImageIcon gameIcon = new ImageIcon("images/GameIcon.png");
		ImageIcon warriorIcon = new ImageIcon("images/PlayerImages/warriorIcon.png");
		ImageIcon priestIcon = new ImageIcon("images/PlayerImages/priestIcon.jpg");
		ImageIcon playerCanvasButtons = new ImageIcon("images/playerCanvasButtons.png");
		ImageIcon gun = new ImageIcon("images/InventoryImages/gun.png");
		ImageIcon keycard = new ImageIcon("images/InventoryImages/keycard.png");
		ImageIcon key = new ImageIcon("images/InventoryImages/key.png");

		ImageIcon lockedTeleport = new ImageIcon("images/InventoryImages/lockedTeleport.png");
		ImageIcon map = new ImageIcon("images/InventoryImages/map.png");
		ImageIcon mediumCarrier = new ImageIcon("images/InventoryImages/mediumCarrier.png");
		ImageIcon nightVisionGoggles = new ImageIcon("images/InventoryImages/nightVisionGoggles.png");
		ImageIcon nullentity = new ImageIcon("images/InventoryImages/nullentity.png");
		ImageIcon smallCarrier = new ImageIcon("images/InventoryImages/smallCarrier.png");

		getPlayerCanvasImages().put("gun",gun);
		getPlayerCanvasImages().put("keycard",keycard);
		getPlayerCanvasImages().put("lockedTeleport",lockedTeleport);
		getPlayerCanvasImages().put("map",map);
		getPlayerCanvasImages().put("mediumCarrier", mediumCarrier);
		getPlayerCanvasImages().put("nightVisionGoggles",nightVisionGoggles);
		getPlayerCanvasImages().put("nullentity", nullentity);
		getPlayerCanvasImages().put("smallCarrier", smallCarrier);

		getPlayerCanvasImages().put("inventory", inventory);
		getPlayerCanvasImages().put("gameIcon", gameIcon);
		getPlayerCanvasImages().put("warriorIcon", warriorIcon);
		getPlayerCanvasImages().put("priestIcon", priestIcon);
		getPlayerCanvasImages().put("playerCanvasButtons", playerCanvasButtons);
	}

	/**
	 * player canvas image helper method to retrieve images.
	 * @param filename
	 * @return
	 */

	public HashMap<String, ImageIcon> getPlayerCanvasImages() {
		return playerCanvasImages;
	}
	public void setPlayerCanvasImages(HashMap<String, ImageIcon> playerCanvasImages) {
		this.playerCanvasImages = playerCanvasImages;
	}
}
