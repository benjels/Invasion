package gamelogic;

import java.util.HashMap;

/**
 * a kind of event that a player can attempt to perform
 * e.g. MoveNth, MoveSth, PickUp, MeleeAttack etc
 * These will be generated/passed around on the client and server sides BUT,
 * they will not be sent over the network where data size is important. Instead we will
 * be ssdnending a "compressed" bit version of the instruction. A map that does this translation
 * will be defined somewhere (here if this is an abstract class)
 * NOTE: the length of the bit string that is used will be dependent on the amount ofevents that we want a player to be able
 * to perform i guess. e.g. if we only need 8 types of events (likely), we can probably get away with a 3 bit string.
 * (i.e. 2 ^ 3 possible events).
 * @author brownmax1
 *
 */
public interface  PlayerEvent {

	//ENCODE//

	//this map is used as a convenient, more human way of determining what kind of event is represented by an int/stringofbits
	//will be used by the Master to decode what the Slave has sent to it.
	//the int will map to an enum that represents that event. (just a string for now cause testinghacky) TODO:
		public final HashMap<Integer, String> intToEventMap = new HashMap<Integer, String>(){{//NOTE: hack (anon class) to allow direct init of map
			put(0, "PlayerMoveNorth");
			put(1, "PlayerMoveSouth");
			put(2, "PlayerMoveEast");
			put(3, "PlayerMoveWest");
		}};


		///DECODE//

		//JUST THE OPPOSITE OF THE MAP ABOVE translate event -> int
		public final HashMap<String, Integer> eventToIntMap = new HashMap<String, Integer>(){{//NOTE: hack (anon class) to allow direct init of map
			put("PlayerMoveNorth", 0);
			put("PlayerMoveSouth", 1);
			put("PlayerMoveEast", 2);
			put("PlayerMoveWest", 3);
		}};


	/**
	 * used to turn this event into a number that represents this event.
	 * There will be a map that goes int -> PlayerEvent Object which will prob be defined
	 * in this abstract class. This number will further be "compressed" into bits when
	 * we send it over the network
	 * @return int the number that represents this type of event
	 */
	//abstract int getEventIntCode();
		//WILL JUST USE ANOTHER MAP AS IT IS MORE CONSISTENT




}
