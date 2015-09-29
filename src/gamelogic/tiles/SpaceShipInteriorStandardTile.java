package gamelogic.tiles;

import gamelogic.Traversable;


/**
 * just the standard sterile/white looking floor tile.
 * no special properties at all
 * @author brownmax1
 *
 */
public class SpaceShipInteriorStandardTile extends GameRoomTile implements Traversable{

	@Override
	public
	RenderRoomTile generateDrawableCopy() { //TODO: set public for package divison
		return new RenderSpaceShipInteriorStandardTile(this.getBloody());
	}



}
