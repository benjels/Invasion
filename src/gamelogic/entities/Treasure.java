package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;

//A PIECE OF TREASURE THAT, IF TAKEN TO THE SECRET SHOP ROOM AND DROPPED, GIVES THE PLAYER MONEY
public class Treasure extends Carryable implements Traversable {
	private static final int CARRY_SIZE = 5;// cannot be carried in smaller bags

	public Treasure(CardinalDirection directionFacing) {
		super(directionFacing, CARRY_SIZE);

	}

	@Override
	protected void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer) {
		assert(pickUpPlayer != null);
		//some player just picked this up
		this.setCurrentHolder(pickUpPlayer);
		System.out.println("picked up the Treasure!");
	}

	@Override
	protected void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
		assert(droppingPlayer != null);
		//some player just dropped this
		this.setCurrentHolder(null);
		//make sure the dropping player has no gun TODO: this causes bug/feature where if you were holding two night visions and you dropped one, you lose nightvision. its ok cause theres only one in the game anyway.
		System.out.println("dropped the Treasure!");
		//if the player is in the secret room, they get the money
		if(droppingPlayer.getCurrentRoom().getId() == 6){
			for(int i = 0; i < 50; i++){
				droppingPlayer.addCoin();
			}
		}
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderTreasure(this.getFacingCardinalDirection());
	}

	@Override
	public String toXMLString() {
		return "Treasure";
	}

}
