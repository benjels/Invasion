package gamelogic.entities;

import gamelogic.events.Action1PushedEvent;
import gamelogic.events.Action2PushedEvent;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.DownPushedEvent;
import gamelogic.events.LeftPushedEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerMoveDown;
import gamelogic.events.PlayerMoveLeft;
import gamelogic.events.PlayerMoveRight;
import gamelogic.events.PlayerMoveUp;
import gamelogic.events.RightPushedEvent;
import gamelogic.events.UpPushedEvent;

public class FighterCharacterStrategy implements PlayerCharacterStrategy{

	@Override
	public PlayerEvent convertClientEvent(ClientGeneratedEvent eventToConvert) {
		
		
		if(eventToConvert instanceof UpPushedEvent){
			return new PlayerMoveUp(eventToConvert.getUid());
		}else if(eventToConvert instanceof DownPushedEvent){
			return new PlayerMoveDown(eventToConvert.getUid());
		}else if(eventToConvert instanceof LeftPushedEvent){
			return new PlayerMoveLeft(eventToConvert.getUid());
		}else if(eventToConvert instanceof RightPushedEvent){
			return new PlayerMoveRight(eventToConvert.getUid());
		}else if(eventToConvert instanceof Action1PushedEvent){
			throw new RuntimeException("that kind of event is not supported yet tbh");
		}else if(eventToConvert instanceof Action2PushedEvent){
			throw new RuntimeException("that kind of event is not supported yet tbh");
		}
		
		//in the case that it is not one of the events that changes depending on what character we are playing, just return the event "as is"
		//e.g. the inventory selection events  that are not dependent on which character you are playing
		return (PlayerEvent) eventToConvert;
	}

}
