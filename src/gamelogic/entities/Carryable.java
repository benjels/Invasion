package gamelogic.entities;

import gamelogic.CardinalDirection;

/**
 * an entity that can be picked up and stored in a carrier (included carriers themselves)
 * @author Max Brown
 *
 */
public abstract class Carryable extends GameEntity{

	private Player currentHolder = null;//the player who currently possesses this item
	private final int size; //the size of this entity

	public Carryable(CardinalDirection directionFacing, int size) {
		super(directionFacing);
		this.size = size;
	}








	/**
	 * used as part of the composite pattern to check whether picking up this item changes the player's state in some way. e.g. picking up the night vision goggles or
	 * a carrier with them in it etc will activate night vision
	 * @param pickUpPlayer the player who picked them up
	 */
	abstract void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer);

	/**
	 * used as part of the composite pattern to check whether dropping this item changes the player's state in some way. e.g. dropping the night vision goggles or
	 * a carrier with them in it etc will deactivate night vision
	 * @param pickUpPlayer the player who dropped them
	 */
	abstract void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer);




	public void setCurrentHolder(Player newHolder){
		this.currentHolder = newHolder;
	}

	public Player getCurrentHolder(){
		return this.currentHolder;
	}



	public int getSize() {
		return size;
	}


}
