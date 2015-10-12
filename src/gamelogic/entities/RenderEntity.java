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
	private final String gameImageName; //probably used to determine which wrapper object to look up in the map
	
	

	public RenderEntity(CardinalDirection directionFacing, String textualDesc, String gameImageName) {
		super(directionFacing);
		this.textualDescriptionForGuiInspection = textualDesc;
		this.gameImageName = gameImageName;
	}
	
	/**
	 * used for the message that is displayed in the inventory
	 * @return the string to be displayed on the hud when a player selectes this item
	 */
	public String getTextDesc(){
		return this.textualDescriptionForGuiInspection;
	}
	

	/**
	 * gets the string that will be used in the renderer map to associate a render entity with the information required to draw it
	 * @return String the string for this RenderEntity
	 */
	public String getGameImageName() {
		return gameImageName;
	}

}
