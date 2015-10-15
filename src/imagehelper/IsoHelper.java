package imagehelper;

import java.awt.Point;
/**
 * Converts position in array to isometric coordinates
 * @author Joely Huang 300305742
 *
 */
public class IsoHelper {

	//formula to convert a 2D array point to Isometric
	//isoX = col * (width/2) - row * (width/2);
	//isoY = col * (height/2) - row * (height/2);
	//source: http://www.java-gaming.org/index.php?topic=24922.0
	public static Point twoDToIso(int x, int y, int width, int height){
		int isoX = (int) (0.5*y*width + 0.5*x*width);
		int isoY = (int) (0.5*y*height - 0.5*x*height);
		return new Point(isoX,isoY);
	}
}
