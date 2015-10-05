package gamelogic.tiles;

import javax.xml.bind.annotation.XmlRootElement;

import gamelogic.Traversable;


/**
 * just the standard sterile/white looking floor tile.
 * no special properties at all
 * @author brownmax1
 *
 */
@XmlRootElement(name = "SpaceShip")
public class InteriorStandardTile extends GameRoomTile implements Traversable{

	@Override
	public
	RenderRoomTile generateDrawableCopy() { //TODO: set public for package divison
		return new RenderInteriorStandardTile(this.getBloody());
	}



}
