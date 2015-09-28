package gamelogic;
/**
 * just the standard sterile/white looking floor tile.
 * no special properties at all
 * @author brownmax1
 *
 */
public class SpaceShipInteriorStandardTile extends GameRoomTile implements TraversableTile{

	@Override
	RenderRoomTile generateDrawableCopy() {
		return new RenderSpaceShipInteriorStandardTile(this.getBloody());
	}



}
