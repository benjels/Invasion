package gamelogic.entities;

import java.io.Serializable;

import gamelogic.CardinalDirection;
import gamelogic.Renderable;

/**
 * the renderable version of an entity that is passed to the renderer
 *
 * @author brownmax1
 *
 */
public abstract class RenderEntity extends DirectionFacer implements Renderable, Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = -2113789511268787509L;
	private final String gameImageName; //probably used to determine which wrapper object to look up in the map



	public RenderEntity(CardinalDirection directionFacing, String gameImageName) {
		super(directionFacing);
		this.gameImageName = gameImageName;
	}

	/**
	 * gets the string that will be used in the renderer map to associate a render entity with the information required to draw it
	 * @return String the string for this RenderEntity
	 */
	public String getGameImageName() {
		return gameImageName;
	}

	abstract public String getName();


}
