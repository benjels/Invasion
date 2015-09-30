package imagehelper;

import java.awt.Point;

public class IsoHelper {
	
	//formula to convert a 2D array point to Isometric
	//isoX = col * (width/2) - row * (width/2);
	//isoY = col * (height/2) - row * (height/2);
	//source: http://www.java-gaming.org/index.php?topic=24922.0
	public static Point twoDToIso(int col, int row, int width, int height){
		//int isoY = col*(height/2) - row*(height/2);
		//int isoX = col*(width/2) + row*(width/2);
		int isoX = (int) (0.5*row*width + 0.5*col*width);
		int isoY = (int) (0.5*row*height - 0.5*col*height);
		return new Point(isoX,isoY);
	}
}
