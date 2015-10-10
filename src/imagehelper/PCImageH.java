package imagehelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class PCImageH {
	private HashMap<String, ImageIcon> playerCanvasImages;
	public static final Image playerCanvasButtons = Imagehelper.loadImage("playerCanvasButtons.png");
	
	public PCImageH(){
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

		getPlayerCanvasImages().put("inventory", inventory);
		getPlayerCanvasImages().put("gameIcon", gameIcon);
		getPlayerCanvasImages().put("warriorIcon", warriorIcon);
		getPlayerCanvasImages().put("priestIcon", priestIcon);
	}
	
	public HashMap<String, ImageIcon> getPlayerCanvasImages() {
		return playerCanvasImages;
	}
	public void setPlayerCanvasImages(HashMap<String, ImageIcon> playerCanvasImages) {
		this.playerCanvasImages = playerCanvasImages;
	}
}
