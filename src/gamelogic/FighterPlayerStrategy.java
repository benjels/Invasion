package gamelogic;

import gamelogic.events.CarrierOpenCloseEvent;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.DownPushedEvent;
import gamelogic.events.InventorySelectionEvent;
import gamelogic.events.LeftPushedEvent;
import gamelogic.events.PlayerDropEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerMoveDown;
import gamelogic.events.PlayerMoveLeft;
import gamelogic.events.PlayerMoveRight;
import gamelogic.events.PlayerMoveUp;
import gamelogic.events.PlayerPickupEvent;
import gamelogic.events.RightPushedEvent;
import gamelogic.events.RotateMapClockwise;
import gamelogic.events.*;

/**
 * the character that players can choose that has a lot of health
 * @author brownmax1
 *
 */
public class FighterPlayerStrategy implements CharacterStrategy {

	@Override
	public PlayerEvent createCharacterEvent(ClientGeneratedEvent tempClientEvent) {
		if(tempClientEvent instanceof DownPushedEvent){
			return new PlayerMoveDown(tempClientEvent.getUid());
		}else if(tempClientEvent instanceof UpPushedEvent){
			return new PlayerMoveUp(tempClientEvent.getUid());
		}else if(tempClientEvent instanceof LeftPushedEvent){
			return new PlayerMoveLeft(tempClientEvent.getUid());
		}else if(tempClientEvent instanceof RightPushedEvent){
			return new PlayerMoveRight(tempClientEvent.getUid());
		}

		//in the case that it is some other kind of event that is consistent between different characters, return it as is. (e.g. PlayerSelectInvSlot1 is a ClientGeneratedEvent and a PlayerEvent
		//sanity checkkk
		assert(tempClientEvent instanceof PlayerDropEvent || tempClientEvent instanceof PlayerPickupEvent || tempClientEvent instanceof InventorySelectionEvent || tempClientEvent instanceof CarrierOpenCloseEvent || tempClientEvent instanceof RotateMapClockwise):"this kind of event not supported atm in the strategy converter";
		return (PlayerEvent) tempClientEvent;
	}
//TODO: implement getMeleeHit() method etc
	//need to think about how damagin system will work etc actually


	//JOSH ADDED THIS
	@Override
	public String toString(){
		return "Tank_Strategy";
	}
}
