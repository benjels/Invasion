package gamelogic;

import java.io.Serializable;

/**
 * wrapper class for an x and y location that is used to send a location over the network
 *
 * @author brownmax1
 *
 */
public class RoomLocation implements Serializable{
private final int x;
private final int y;

public RoomLocation(int x, int y){
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
