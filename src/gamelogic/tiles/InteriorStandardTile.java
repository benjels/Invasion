package gamelogic.tiles;

import gamelogic.Traversable;


/**
 * just the standard sterile/white looking floor tile.
 * no special properties at all
 * @author brownmax1
 *
 */

public class InteriorStandardTile extends GameRoomTile implements Traversable{

	@Override
	public
	RenderRoomTile generateDrawableCopy() { //TODO: set public for package divison
		return new RenderInteriorStandardTile();
	}
	
	@Override 
	public String toString(){
		return "Interior_Tile";
	}



}
