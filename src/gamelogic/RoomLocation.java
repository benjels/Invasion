package gamelogic;
/**
 * basically just a passive holder of an x/y location of some object in a room
 *
 * @author brownmax1
 *
 */
public class RoomLocation {
private final int x;
private final int y;

public RoomLocation(int x, int y){//TODO: set public for package divison
	this.x = x;
	this.y = y;
}

public int getX() {
	return x;
}

public int getY() {
	return y;
}



}
