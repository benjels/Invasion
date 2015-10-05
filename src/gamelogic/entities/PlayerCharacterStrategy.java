package gamelogic.entities;

import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;

/**
 *a class that implements this interface defines a large amount of the behaviour that the character that a player is playing the game as has in this game..
 *e.g. it determines what events should be generated for the player in the game world when they press a certain button and how much health they should lose when they are damaged etc
 * @author Max Brown
 *
 */
public interface PlayerCharacterStrategy {

	
	/**
	 * converts an event from the client (e.g. action button 1 pressed) into the event that is appropriate for this
	 * character in the game
	 * @param eventToConvert the event from the client
	 * @return PlayerEvent the event to be applied in the game world
	 */
	abstract PlayerEvent convertClientEvent(ClientGeneratedEvent eventToConvert);
	
	
}
