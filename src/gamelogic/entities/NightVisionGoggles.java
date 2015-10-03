package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.Traversable;
/**
 * night vision goggles. When they are in a player's inventory, that player can see much
 * further inside dark rooms
 * @author Max Brown
 *
 */

public class NightVisionGoggles extends Carryable implements Traversable {

	public NightVisionGoggles(CardinalDirection directionFacing) {
		super(directionFacing);
	}

	@Override
	public RenderEntity generateDrawableCopy() {
		return new RenderNightVisionGoggles(this.getFacingCardinalDirection());
	}

}
