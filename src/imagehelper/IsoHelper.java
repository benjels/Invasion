package imagehelper;

import java.awt.Point;

public class IsoHelper {
	
	//formula to convert a 2D array point to Isometric
	//isoX = col * (width/2) - row * (width/2);
	//isoY = col * (height/2) - row * (height/2);
	//source: http://www.java-gaming.org/index.php?topic=24922.0
	public static Point twoDToIso(int col, int row, int width, int height){
		int isoX = col*(width/2) - row*(width/2);
		int isoY = col*(height/2) + row*(height/2);
		return new Point(isoX,isoY);
	}
}
