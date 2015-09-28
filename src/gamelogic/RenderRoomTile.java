package gamelogic;

import java.awt.Image;

/**
 * a tile that is not used in the game state. Used for rendering.
 * @author brownmax1
 *
 */
public abstract class RenderRoomTile implements Renderable{

private final boolean isBloody;//whether this tile is blood spattered or not

RenderRoomTile(boolean isBloody){
	this.isBloody = isBloody;

}



public boolean isBloody(){
	return this.isBloody;
}

}
