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
		//Fighter images
		public static final Image pWest = Imagehelper.loadImage("PlayerImages/MaleA.png");
		public static final Image pEast = Imagehelper.loadImage("PlayerImages/MaleD.png");
		public static final Image pNorth = Imagehelper.loadImage("PlayerImages/MaleW.png");
		public static final Image pSouth = Imagehelper.loadImage("PlayerImages/MaleS.png");
		//Sorcerer images
		public static final Image SWest = Imagehelper.loadImage("PlayerImages/priestA.png");
		public static final Image SEast = Imagehelper.loadImage("PlayerImages/priestD.png");
		public static final Image SNorth = Imagehelper.loadImage("PlayerImages/priestW.png");
		public static final Image SSouth = Imagehelper.loadImage("PlayerImages/priestS.png");
		//tile images
		public static final Image Dirt = Imagehelper.loadImage("dirt64.png");
		public static final Image Stone = Imagehelper.loadImage("stone64.png");
		public static final Image testStone = Imagehelper.loadImage("Tiles/Concrete2.png");
		public static final Image Harmful = Imagehelper.loadImage("HarmfulTile/harmfultile.png");
		//entity images
		public static final Image key = Imagehelper.loadImage("key.png");
		public static final Image coin = Imagehelper.loadImage("money.png");
		public static final Image pylon = Imagehelper.loadImage("pylon.png");
		public static final Image smallC = Imagehelper.loadImage("Items/smallC.png");
		public static final Image medC = Imagehelper.loadImage("Items/medC.png");
		public static final Image teleGun = Imagehelper.loadImage("teleporterGun.png");
		public static final Image gun = Imagehelper.loadImage("gun.png");
		public static final Image tele = Imagehelper.loadImage("StandardTeleporter.png");
		public static final Image lockedTele = Imagehelper.loadImage("LockedTeleporter.png");
		public static final Image nightV = Imagehelper.loadImage("NVG.png");
		public static final Image healthkit = Imagehelper.loadImage("healthkit.png");
		public static final Image treasure = Imagehelper.loadImage("treasureBoard.png");
		public static final Image portal = Imagehelper.loadImage("portalTile.png");
		public static final Image explosion = Imagehelper.loadImage("explosion.png");

		//robot images
		public static final Image robotN = Imagehelper.loadImage("Enemy/robotN.png");
		public static final Image robotS = Imagehelper.loadImage("Enemy/robotS.png");
		public static final Image robotE = Imagehelper.loadImage("Enemy/robotE.png");
		public static final Image robotW = Imagehelper.loadImage("Enemy/robotW.png");

		//enemies
		public static final Image burningN = Imagehelper.loadImage("Enemy/Flame3.png");
		public static final Image burningS = Imagehelper.loadImage("Enemy/Flame1.png");
		public static final Image burningE = Imagehelper.loadImage("Enemy/Flame4.png");
		public static final Image burningW = Imagehelper.loadImage("Enemy/Flame2.png");

		//impassable column
		public static final Image impassCoNS = Imagehelper.loadImage("ImpassableColumn/IronNS.png");
		public static final Image impassCoEW = Imagehelper.loadImage("ImpassableColumn/IronEW.png");

		public GCImageH(){
			GCImages = new HashMap<String,Image>();
			loadImages();
		}

		/**
		 * Loading all the images that are needed in the game
		 */
		public void loadImages(){
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
			GCImages.put("robotN", robotN);
			GCImages.put("robotS", robotS);
			GCImages.put("robotE", robotE);
			GCImages.put("robotW", robotW);
			GCImages.put("key", key);
			GCImages.put("coin", coin);
			GCImages.put("pylon", pylon);
			GCImages.put("impassConNS", impassCoNS);
			GCImages.put("impassConEW", impassCoEW);
			GCImages.put("medC", medC);
			GCImages.put("lockedTele", lockedTele);
			GCImages.put("tele", tele);
			GCImages.put("portal", portal);
			GCImages.put("teleGun", teleGun);
			GCImages.put("gun", gun);
			GCImages.put("nightV", nightV);
			GCImages.put("tele", tele);
			GCImages.put("harmfultile", Harmful);
			GCImages.put("healthkit", healthkit);
			GCImages.put("smallC", smallC);
			GCImages.put("tile",testStone);
			GCImages.put("burningN",burningN);
			GCImages.put("burningS",burningS);
			GCImages.put("burningE",burningE);
			GCImages.put("burningW",burningW);
			GCImages.put("treasure", treasure);
			GCImages.put("SWest", SWest);
			GCImages.put("SEast", SEast);
			GCImages.put("SNorth", SNorth);
			GCImages.put("SSouth", SSouth);
			GCImages.put("explosion", explosion);
		}

		/**
		 * returns the hashmap of images
		 * @return HashMap<String,Image>
		 */
		public HashMap<String, Image> getGCImages() {
			return GCImages;
		}
}
