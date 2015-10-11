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
		ImageIcon priestIcon = new ImageIcon("images/PlayerImages/priestIconNew.png");
		ImageIcon playerCanvasButtons = new ImageIcon("images/playerCanvasButtons.png");






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
