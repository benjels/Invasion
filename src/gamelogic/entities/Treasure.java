package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;
import gamelogic.renderentities.RenderEntity;
import gamelogic.renderentities.RenderTreasure;

/**
 * a pieceo f treasure that if picked up and then dropped in the secret room, gives 50 coins
 * @author brownmax1
 *
 */
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
	}

	@Override
	protected void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer) {
		assert(droppingPlayer != null);
		//some player just dropped this
		this.setCurrentHolder(null);
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
