package imagehelper;

import java.util.HashMap;

import javax.swing.ImageIcon;
/**
 * Class to support the drawing of static images to the player Canvas.
 * This is kept on the client side and prevents the port to send images across the network.
 * @author Quentin Copley
 *
 */
public class PlayerCanvasImagehelper {

	private HashMap<String, ImageIcon> playerCanvasImages;

	public PlayerCanvasImagehelper() {
		this.initializeImageHashMap();
	}
	/**
	 * Hash Map created for efficiency. Hard Coded constructor to add every Game image to a hashmap.
	 * Player Canvas uses this hash map to read in images and draw them.
	 *
	 */
	public void initializeImageHashMap(){
		this.setPlayerCanvasImages(new HashMap<String, ImageIcon>());
		ImageIcon warriorIcon = new ImageIcon("images/PlayerImages/warriorIcon.png");
		ImageIcon priestIcon = new ImageIcon("images/PlayerImages/priestIcon.jpg");
		ImageIcon playerCanvasButtons = new ImageIcon("images/playerCanvasButtons.png");
		ImageIcon gun = new ImageIcon("images/InventoryImages/gun.png");
		ImageIcon keycard = new ImageIcon("images/InventoryImages/keycard.png");
		ImageIcon key = new ImageIcon("images/InventoryImages/key.png");
		ImageIcon lockedTeleport = new ImageIcon("images/InventoryImages/lockedTeleport.png");
		ImageIcon map = new ImageIcon("images/InventoryImages/map.png");
		ImageIcon mbag = new ImageIcon("images/InventoryImages/mediumCarrier.png");
		ImageIcon nvg = new ImageIcon("images/InventoryImages/nightVisionGoggles.jpg");
		ImageIcon nullentity = new ImageIcon("images/InventoryImages/nullentity.png");
		ImageIcon sbag = new ImageIcon("images/InventoryImages/smallCarrier.png");
		ImageIcon sbaginv = new ImageIcon("images/InventoryImages/smallCarrierinv.png");
		ImageIcon teleportGun = new ImageIcon("images/InventoryImages/teleportGun.png");
		ImageIcon coin = new ImageIcon("images/InventoryImages/coin.png");
		ImageIcon coininv = new ImageIcon("images/InventoryImages/coininv.png");
		ImageIcon guninv = new ImageIcon("images/InventoryImages/guninv.png");
		ImageIcon keycardinv = new ImageIcon("images/InventoryImages/keycardinv.png");
		ImageIcon nvginv = new ImageIcon("images/InventoryImages/nvginv.png");
		ImageIcon nullentityinv = new ImageIcon("images/InventoryImages/nullentityinv.png");
		ImageIcon mbaginv = new ImageIcon("images/InventoryImages/mediumCarrierinv.png");
		ImageIcon teleportGuninv = new ImageIcon("images/InventoryImages/teleportGuninv.png");
		ImageIcon healthKitinv = new ImageIcon("images/InventoryImages/healthKitinv.png");
		ImageIcon healthKit = new ImageIcon("images/InventoryImages/healthKit.png");
		ImageIcon treasure = new ImageIcon("images/InventoryImages/treasure.png");

		getPlayerCanvasImages().put("healthkit", healthKit);
		getPlayerCanvasImages().put("healthkitinv", healthKitinv);
		getPlayerCanvasImages().put("gun",gun);
		getPlayerCanvasImages().put("keycard",keycard);
		getPlayerCanvasImages().put("key",key);
		getPlayerCanvasImages().put("coin",coin);
		getPlayerCanvasImages().put("lockedTeleport",lockedTeleport);
		getPlayerCanvasImages().put("map",map);
		getPlayerCanvasImages().put("mbag", mbag);
		getPlayerCanvasImages().put("nvg",nvg);
		getPlayerCanvasImages().put("nullentity", nullentity);
		getPlayerCanvasImages().put("smallCarrier", sbag);
		getPlayerCanvasImages().put("smallCarrierinv", sbaginv);
		getPlayerCanvasImages().put("teleportGun",teleportGun);
		getPlayerCanvasImages().put("treasure",treasure);
		getPlayerCanvasImages().put("treasureinv", coininv);
		getPlayerCanvasImages().put("guninv", guninv);
		getPlayerCanvasImages().put("keycardinv", keycardinv);
		getPlayerCanvasImages().put("nvginv", nvginv);
		getPlayerCanvasImages().put("nullentityinv", nullentityinv);
		getPlayerCanvasImages().put("mbaginv", mbaginv);
		getPlayerCanvasImages().put("teleportGuninv",teleportGuninv);
		getPlayerCanvasImages().put("warriorIcon", warriorIcon);
		getPlayerCanvasImages().put("priestIcon", priestIcon);
		getPlayerCanvasImages().put("playerCanvasButtons", playerCanvasButtons);
	}

	/**
	 *
	 *
	 *
	 *
	 *
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
