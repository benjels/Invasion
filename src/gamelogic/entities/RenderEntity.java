package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Renderable;

/**
 * the renderable version of an entity that is passed to the renderer
 *
 * @author brownmax1
 *
 */
public abstract class RenderEntity extends DirectionFacer implements Renderable {

	public RenderEntity(CardinalDirection directionFacing) {
		super(directionFacing);
	}


}
