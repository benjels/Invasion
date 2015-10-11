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


	
	private String textualDescriptionForGuiInspection;//used for when you inspect an item in the gui

	public RenderEntity(CardinalDirection directionFacing, String textualDesc) {
		super(directionFacing);
		this.textualDescriptionForGuiInspection = textualDesc;
	}
	
	/**
	 * used for the message that is displayed in the inventory
	 * @return the string to be displayed on the hud when a player selectes this item
	 */
	public String getTextDesc(){
		return this.textualDescriptionForGuiInspection;
	}
	


}
