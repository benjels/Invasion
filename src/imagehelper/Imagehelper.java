package imagehelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class loads the images for the files
 * @author Joely Huang 300305742
 *
 */
public class Imagehelper {
	/**
	 *
	 * Method to return images for GameCanvas
	 *
	 * @param String - the filename of the image
	 * @return Image
	 *
	 */
	public static Image loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/" + filename));
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

}