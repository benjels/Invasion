package gamelogic;

import java.util.ArrayList;
import java.util.LinkedList;

import control.DummyMaster;
import control.DummySlave;

/**
 * this is the class that maintains the "true" state of the game. It receives desired events from the players via their Masters.
 * If a desired event is valid (e.g. they are not trying to move into a wall or pick up a tree etc), then we will enqueue this event.
 *
 * each time the clock ticks, the server will dequeue the event at the head of the queue and distribute it to all of the players who will use
 * it to update their local version of the game state.
 * @author brownmax1
 *
 */
public class Server{

	//TODO: this class will need maps: 1) to identify Player from playeruid (e.g. to determine if that Player can move in the true game state).
	private final WorldGameState serverTrueWorldGameState;// the server's version of the physical game state
	private ArrayList<DummyMaster> masters = new ArrayList<>();//the masters that dequeued events will be sent to



	public Server( WorldGameState initGameWorld){ //this will really just receive an XML file which is the game map which it will use to construct the rooms and the initGameWorld etc
		//TODO: NOTE THAT the server won't actually be started with the Masters connected and shit. willl really only receive a game state that has been writeen in from xml.
		//really the players will have to be able to connect after the server has been instantiated which means wae will need like acceptNewPlayer() methods aned shit tbh
		//HOWEVER FOR NOW: we will just act as though the server does not need to be connected to via ports etc
		//will probably call a method called createServerGameState from xml or something which parses the xml to create the rooms and graph and links them together and shit.
		//note that we only need to store the spawn room though because it will have references to other rooms and its all connected yea
		this.serverTrueWorldGameState =  initGameWorld;
	}




	/**
	 *every time the clock ticks, we should grab each master's current event and attempt to apply it to the game state
	 *once all of the changes have been made, we need to send this updated version of the game state to all of the players so
	 *that they can draw it
	 */
		public void clockTick() {
			LinkedList<IDedPlayerEvent> eventsToAttemptToApplyToGameState = new LinkedList<>();//this queue will be filled up by the events fetched from the Masters and the zombies. It's conceivable that in the future, applying an event will enqueue more events here.

		//gather all of the events from the masters
			for(DummyMaster eachMaster: this.masters){
				if(eachMaster.hasEvent()){
					eventsToAttemptToApplyToGameState.offer(eachMaster.fetchEvent());
				}
			}

			//gather all of the events from the AI ZOMBIES... DO THIS LATER
				//note that these AI objects will function like autonomous versions of the Masters which are not connected to any slaves and  generate their own events. They will be told to get an event ready by the clock thread immediately after each tick so that they have something to do on the next tick.

	    //attempt to apply all of the queued  events to the game state
		while(!eventsToAttemptToApplyToGameState.isEmpty()){
			IDedPlayerEvent headEvent = eventsToAttemptToApplyToGameState.poll();
			attemptToApplyEvent(headEvent);
		}

		//broadcast new game state to each master (each master may need a different version cause they only need their room or watev)
		for(DummyMaster eachMaster: this.masters){//TODO: dont actually need to send anything to players whose rooms havent changed tbh
			eachMaster.sendClientFrameMasterToSlave(this.serverTrueWorldGameState.generateFrameForClient(eachMaster.getUid()));
		}


		}





/**
 * attempts to apply an event from a player to the current version of the game state
 * @param desiredEvent the event that we want to perform
 * @return bool true if the event successfully applied to the game state, else false.
 */
	private boolean attemptToApplyEvent(IDedPlayerEvent desiredEvent) {
		//TODO: WILL NEED TO CHECK HERE WHETHER THE GAME CAN ACTUALLY BE UPDATED IN THE DESIRED WAY AND AFFECT THAT CHANGE IF IT CAN.
		return this.serverTrueWorldGameState.applyEvent(desiredEvent);
	}





///REGISTERING/JOINING SHIT///

/**
 * when a client/slave wants to join the server, they attempt to connect to a socket that is listening for new players (i think?)
 * this will create a new master  for that client and connect that master to the client so that they can communicate
 * @param dummySlave the client that wants to connect
 * @return DummyMaster the Master that the client will speak with for rest of the program's execution
 */
	public DummyMaster createMasterForSlave(DummySlave dummySlave) {
		//create the master for the slave that tried to join
		DummyMaster newMaster = new DummyMaster(dummySlave.getPlayerUid());
		//add the master to the collection of masters
		this.masters.add(newMaster);
		//send back this master so that the slave can talk to tit
		return newMaster;
	}

/**
 * used by hacky setup shits to add a player to the true game state so that the player's master can send events for this player
 * @param myPlayer the player we are adding
 */
public void addPlayer(Player myPlayer) {
	this.serverTrueWorldGameState.addPlayerToGameState(myPlayer);
}



}
