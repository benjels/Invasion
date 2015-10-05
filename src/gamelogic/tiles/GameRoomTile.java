package gamelogic.tiles;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * a tile that can be part of the 2d tile array that makes up rooms.
 * these tiles are not drawn but they can be converted into drawable versions for the renderer.
 * @author brownmax1
 *
 */
@XmlRootElement(name = "tile")
@XmlSeeAlso({InteriorStandardTile.class})
public abstract class GameRoomTile{


	private boolean isBloody = false;




	/**
	 * creates a DrawableTile version of thie RoomTile.
	 * Used when we need to create the drawable version of the game state for each client.
	 * @return DrawableRoomTile the drawable version of this tile
	 */
	public abstract RenderRoomTile generateDrawableCopy(); //TODO: set public for package divison


	public void setBloody(boolean bloodyness){
		this.isBloody = bloodyness;
	}

	@XmlElement
	public boolean getBloody(){
		return this.isBloody;
	}



}
