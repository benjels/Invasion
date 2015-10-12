package gamelogic.entities;

import gamelogic.CardinalDirection;

//DRAWABLE VERSION OF ALL CARRYABLES
public abstract class RenderCarryable extends RenderEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7074159435458503312L;
	private final String inventoryImageFileName; //used to determine which image to draw in the ui when a player is holding this item

	
	
	
	public RenderCarryable(CardinalDirection directionFacing, String textualDesc, String invFile, String gameImageName) {
		super(directionFacing, textualDesc, gameImageName);
		this.inventoryImageFileName = invFile;
	}

	/**
	 * gets the stored file name of the image that gets drawn when a player has this item in their inventory
	 * @return
	 */
	public String getInventoryImageFileName(){
		return this.inventoryImageFileName;
	}


}
