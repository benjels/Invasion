package gamelogic.entities;

import gamelogic.CardinalDirection;

/**
 * used to identify entities that can be picked up and stored in a player's containers 
 * (this included other containers)
 * @author Max Brown
 *
 */
public abstract class Carryable extends GameEntity{
	
	private Player currentHolder = null;//the player who currently possesses this item

	public Carryable(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	
	
	///PICK UP / DROP HELPER METHODS
	
	
	//E.G. RECURSIVELY CALLS THIS METHOD ON ALL COMPONENTS. IF A COMPONENT IS A LEAF, IF THAT LEAF IS NV GOGGLES, CHANGES THE PLAYER'S NV GOGGLES FIELD TO TRUE
	abstract void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer);
	
	//E.G. RECURSIVELY CALLS THIS METHOD ON ALL COMPONENTS. IF A COMPONENT IS A LEAF, IF THAT LEAF IS NV GOGGLES, CHANGES THE PLAYER'S NV GOGGLES FIELD TO FALSE
	abstract void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer);
	
	
	public void setCurrentHolder(Player newHolder){
		this.currentHolder = newHolder;
	}
	
	public Player getCurrentHolder(){
		return this.currentHolder;
	}
	

}
