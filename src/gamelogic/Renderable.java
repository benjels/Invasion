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
	 * used by the renderer to get the offset of the object that needs to be drawn
	 * @return Point
	 */
	abstract Point getOffset();


}
