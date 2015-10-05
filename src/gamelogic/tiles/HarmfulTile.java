package gamelogic.tiles;

import javax.xml.bind.annotation.XmlRootElement;

import gamelogic.Traversable;


/**
 * tile that hurts the movable entity that moves over it (maybe lava or ice or electric or something)
 * @author brownmax1
 *
 */

public class HarmfulTile extends GameRoomTile implements Traversable{

	@Override
	public
	RenderRoomTile generateDrawableCopy() { //TODO: set public for package divison
		return new RenderHarmfulTile(this.getBloody());
	}



}
