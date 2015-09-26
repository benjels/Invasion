package gamelogic;
/**
 * a tile that can be part of the 2d tile array that makes up rooms.
 * these tiles are not drawn but they can be converted into drawable versions for the renderer.
 * @author brownmax1
 *
 */
public abstract class GameRoomTile{

	/**
	 * creates a DrawableTile version of thie RoomTile.
	 * Used when we need to create the drawable version of the game state for each client.
	 * @return DrawableRoomTile the drawable version of this tile
	 */
	abstract RenderRoomTile generateDrawableCopy();

}
