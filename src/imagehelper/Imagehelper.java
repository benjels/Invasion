package imagehelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class deals with images for the entire game. It sets/stores the images
 * path so then the other classes can make a new images and gets the required
 * image
 */
public class Imagehelper {

	// private HashMap<String, Image> gameCanvasImages;
	private HashMap<String, ImageIcon> playerCanvasImages;
	public static final Image WallNS = loadImage2("wallNS.png");
	public static final Image WallEW = loadImage2("wallEW.png");
	public static final Image Grass = loadImage2("grass64.png");
	public static final Image testPlayer = loadImage2("PlayerImages/MaleA.png");
	public static final Image Dirt = loadImage2("dirt64.png");
	public static final Image Stone = loadImage2("stone64.png");
	public static final Image testStone = loadImage2("Tiles/Concrete.png");
	public static final Image Zombie = loadImage2("wall64.png");
	public static final Image key = loadImage2("key.png");
	public static final Image coin = loadImage2("coin.png");
	public Imagehelper(){
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

	/**
	 * player canvas image helper method to retrieve images.
	 * @param filename
	 * @return
	 */
	public Image loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/" + filename));
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	public HashMap<String, ImageIcon> getPlayerCanvasImages() {
		return playerCanvasImages;
	}
	public void setPlayerCanvasImages(HashMap<String, ImageIcon> playerCanvasImages) {
		this.playerCanvasImages = playerCanvasImages;
	}
	/**
	 *
	 * Method to return images for GameCanvas
	 *
	 * @author Joely
	 * @param filename
	 * @return
	 *
	 */
	public static Image loadImage2(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/" + filename));
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

}