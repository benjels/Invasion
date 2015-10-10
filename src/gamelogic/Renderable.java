package gamelogic;

import java.awt.Image;
import java.awt.Point;

/**
 * used by anything that can have its img retreived fro drawing purpose.
 * @author brownmax1
 *
 */
public interface Renderable {

	/**
	 * used by the renderer to retreive the image used for drawing this game element
	 * @return an Image that should be drawn on the canvas
	 */
	abstract Image getImg();
	abstract Point getOffset();


}
