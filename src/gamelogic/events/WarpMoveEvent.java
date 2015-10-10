package gamelogic.events;
//WARPS THE ENTITY SEVERAL TILES AT A TIME IN THE DIRECTION THAT THEY ARE CURRENTLY FACING
public class WarpMoveEvent extends PlayerEvent implements SpatialEvent, MovementEvent {

	private final int warpDistance;//how many tiles to warp

	public WarpMoveEvent(int uid, int warpDistance) {
		super(uid);
		this.warpDistance = warpDistance;
	}

	public int getWarpDistance() {
		return warpDistance;
	}

}
