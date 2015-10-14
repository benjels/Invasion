package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.renderentities.RenderEntity;

/**
 * an item in the game world that can occupy an entity slot
 * on the board. A Game entity is not what is drawn by the renderer,
 * but game entities can be converted into RenderEntities
 * @author brownmax1
 *
 */

public abstract class GameEntity  extends DirectionFacer{

	public GameEntity(CardinalDirection directionFacing) {
		super(directionFacing);
	}
	

	/**
	 * creates a DrawableTile version of thie GameEntity
	 * Used when we need to create the drawable version of the game state for each client.
	 * @return DrawableRoomTile the drawable version of this tile
	 */
	public abstract RenderEntity generateDrawableCopy(); //TODO: set public for package divison

	public abstract String toXMLString();


}
