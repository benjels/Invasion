package imagehelper;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * Game Canvas Image Helper Class
 * Stores all the Images needed for the game canvas
 * @author Joely
 *
 */

public class GCImageH {
		private HashMap<String, Image> GCImages;
		//dimensions
		public static final int width = 64;
		public static final int height = 32;
		//Wall Images
		public static final Image WallNS = Imagehelper.loadImage("wallNS.png");
		public static final Image WallEW = Imagehelper.loadImage("wallEW.png");
		public static final Image Grass = Imagehelper.loadImage("grass64.png");
		//Player images
		public static final Image pWest = Imagehelper.loadImage("PlayerImages/MaleA.png");
		public static final Image pEast = Imagehelper.loadImage("PlayerImages/MaleD.png");
		public static final Image pNorth = Imagehelper.loadImage("PlayerImages/MaleW.png");
		public static final Image pSouth = Imagehelper.loadImage("PlayerImages/MaleS.png");
		//tile images
		public static final Image Dirt = Imagehelper.loadImage("dirt64.png");
		public static final Image Stone = Imagehelper.loadImage("stone64.png");
		public static final Image testStone = Imagehelper.loadImage("Tiles/Concrete3.png");
		//entity images
		public static final Image Zombie = Imagehelper.loadImage("wall64.png");
		public static final Image key = Imagehelper.loadImage("key.png");
		public static final Image coin = Imagehelper.loadImage("coin.png");
		public static final Image pylon = Imagehelper.loadImage("pylon.png");
		public static final Image impassCoNS = Imagehelper.loadImage("ImpassableColumn/corregatedIronNS.png");
		public static final Image smallC = Imagehelper.loadImage("Items/smallCarrier.png");
		
		public GCImageH(){
			GCImages = new HashMap<String,Image>();
			loadImages();
		}
		
		/**
		 * Loading all the images that are needed in the game
		 */
		public void loadImages(){
			/*//Wall Images
			Image WallNS = Imagehelper.loadImage("wallNS.png");
			Image WallEW = Imagehelper.loadImage("wallEW.png");
			Image Grass = Imagehelper.loadImage("grass64.png");
			//Player images
			Image pWest = Imagehelper.loadImage("PlayerImages/MaleA.png");
			Image pEast = Imagehelper.loadImage("PlayerImages/MaleD.png");
			Image pNorth = Imagehelper.loadImage("PlayerImages/MaleW.png");
			Image pSouth = Imagehelper.loadImage("PlayerImages/MaleS.png");
			//tile images
			Image Dirt = Imagehelper.loadImage("dirt64.png");
			Image Stone = Imagehelper.loadImage("stone64.png");
			Image testStone = Imagehelper.loadImage("Tiles/Concrete3.png");
			//entity images
			Image Zombie = Imagehelper.loadImage("wall64.png");
			Image key = Imagehelper.loadImage("key.png");
			Image coin = Imagehelper.loadImage("coin.png");
			Image pylon = Imagehelper.loadImage("pylon.png");
			Image impassCoNS = Imagehelper.loadImage("ImpassableColumn/corregatedIronNS.png");
			Image smallC = Imagehelper.loadImage("Items/smallCarrier.png");*/
			
			GCImages.put("WallNS", WallNS);
			GCImages.put("WallEW", WallEW);
			GCImages.put("Grass", Grass);
			GCImages.put("pWest", pWest);
			GCImages.put("pEast", pEast);
			GCImages.put("pNorth", pNorth);
			GCImages.put("pSouth", pSouth);
			GCImages.put("Dirt", Dirt);
			GCImages.put("Stone", Stone);
			GCImages.put("testStone", testStone);
			GCImages.put("Zombie", Zombie);
			GCImages.put("key", key);
			GCImages.put("coin", coin);
			GCImages.put("pylon", pylon);
			GCImages.put("impassCoNS", impassCoNS);
			GCImages.put("smallC", smallC);
		}
		
		public HashMap<String, Image> getGCImages() {
			return GCImages;
		}
}
