package gamelogic;

import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerNullEvent;

import java.util.ArrayList;

import control.DummyMaster;
import control.DummySlave;

/**
 * this is the class that maintains the "true" state of the game. It receives desired events from the players via their Masters.
 * If a desired event is valid (e.g. they are not trying to move into a wall or pick up a tree etc), then we will enqueue this event.
 *
 * each time the clock ticks, the server will dequeue the event at the head of the queue and distribute it to all of the players who will use
 * it to update their local version of the game state.
 * @author brownmax1
 * CRUCIALLY, THIS CLASS DEALS WITH REGISTERING/ADDING/REMOVING ENTITITIES FROM THE GAME STATE. THIS IS THE STA
 *
 */
public class Server{

	//TODO: this class will need maps: 1) to identify Player from playeruid (e.g. to determine if that Player can move in the true game state).
	private final WorldGameState serverTrueWorldGameState;// the server's version of the physical game state
	private ArrayList<DummyMaster> masters = new ArrayList<>();//the masters that dequeued events will be sent to
	private final IndependentActorManager enemyManager; //the enemy manager that the server communicates with to get the AIs events



	public Server( WorldGameState initGameWorld, IndependentActorManager enemyManager){ //this will really just receive an XML file which is the game map which it will use to construct the rooms and the initGameWorld etc
		//TODO: NOTE THAT the server won't actually be started with the Masters connected and shit. willl really only receive a game state that has been writeen in from xml.
		//really the players will have to be able to connect after the server has been instantiated which means wae will need like acceptNewPlayer() methods aned shit tbh
		//HOWEVER FOR NOW: we will just act as though the server does not need to be connected to via ports etc
		//will probably call a method called createServerGameState from xml or something which parses the xml to create the rooms and graph and links them together and shit.
		//note that we only need to store the spawn room though because it will have references to other rooms and its all connected yea
		this.serverTrueWorldGameState =  initGameWorld;
		this.enemyManager = enemyManager;
	}




	/**
	 *every time the clock ticks, we should grab each master's current event and attempt to apply it to the game state
	 *once all of the changes have been made, we need to send this updated version of the game state to all of the players so
	 *that they can draw it
	 */
		public void clockTick() {

			/*long testingTickTimeStart =  System.currentTimeMillis();*/


			ArrayList<PlayerEvent> eventsToAttemptToApplyToGameState = new ArrayList<>();//this queue will be filled up by the events fetched from the Masters and the zombies. It's conceivable that in the future, applying an event will enqueue more events here.

			
			
		//gather all of the events from the masters 
			for(DummyMaster eachMaster: this.masters){
				if(eachMaster.hasEvent()){
					//get the ClientGeneratedEvent from the master's buffer
					ClientGeneratedEvent tempClientEvent = eachMaster.fetchEvent();
					assert (this.serverTrueWorldGameState.getMovableEntites().get(eachMaster.getUid()) instanceof Player): "the MovableEntity taht shares an uid with a mster needs to be a Player";
					//convert the ClientGeneratedEvent stored in the master's buffer into a MovableEntityEvent that is used in game logic
					PlayerEvent tempPlayerEvent = ((Player) this.serverTrueWorldGameState.getMovableEntites().get(eachMaster.getUid())).getCharacter().createCharacterEvent(tempClientEvent);
					
					//add the MovableEntityEvent to the list of events to be applied
					eventsToAttemptToApplyToGameState.add(tempPlayerEvent);
					
				}
			}


			//gather all of the events from the AI ZOMBIES 
			eventsToAttemptToApplyToGameState.addAll(this.enemyManager.retrieveEnemyStatusOnTick()); 
			
			
	    //attempt to apply all of the queued  events to the game state
		while(!eventsToAttemptToApplyToGameState.isEmpty()){
			PlayerEvent headEvent = eventsToAttemptToApplyToGameState.get(0);
			assert(headEvent != null && !(headEvent instanceof PlayerNullEvent)):"nah that's not me. no event should be true null";
		/*	System.out.println("about to apply an event by entity with id: " + headEvent.getUid());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			eventsToAttemptToApplyToGameState.remove(0);
			if(!attemptToApplyEvent(headEvent)){
				//throw new RuntimeException("failed to apply move"); //TODO obviously not a real exception
				System.out.println("the following event's apply methods returned false somewhere along its journey (this isnt reallly a bad thing) uid + event: " + headEvent.getUid() + headEvent.toString());
			}
		}





		//broadcast new game state to each master (each master may need a different version cause they only need their room or watev)
		for(DummyMaster eachMaster: this.masters){//TODO: dont actually need to send anything to players whose rooms havent changed tbh
			eachMaster.sendClientFrameMasterToSlave(this.serverTrueWorldGameState.generateFrameForClient(eachMaster.getUid()));
		}


	/*	long testingTickTimeEnd =  System.currentTimeMillis();


		System.out.println("so it took a total of " + (testingTickTimeEnd - testingTickTimeStart) + "to apply the events from that tick to the game state and then send \n it out to the players. as far as i know this should be fine as long as it is definitely always comfortably less than the tick rate (but we actually need to also include the render time and network send time). \n despite this, because this interval does seem to vary, it means that we are going to be sending frames to draw irregularly. This should be ok because of double buffering. \n if it does look stuttery, you should use jah's technique ");

*/


		}





/**
 * attempts to apply an event from a player to the current version of the game state
 * @param desiredEvent the event that we want to perform
 * @return bool true if the event successfully applied to the game state, else false.
 */
	private boolean attemptToApplyEvent(PlayerEvent desiredEvent) {
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



//USED AS PART OF HACKY SHIT TO CONNECT EVERYTHING UP
/*public void registerPlayerWithGameState(MovableEntity myPlayer) { marked for deletion
	if(!(this.serverTrueWorldGameState.addMovableEntityToRoomState(myPlayer, 0, 10, 10))){
		throw new RuntimeException("failed to spawn the player in the roommm");
	}
	//actually add that player to the entity map
	this.serverTrueWorldGameState.addMovableToMap(myPlayer);

}*/
//NOTE THAT THE BELOW GETTER NEEDED FOR HACKY SETUP, SHOULDNT BE USED AFTER INTEGRATION MAYBE
	public WorldGameState getWorldGameState(){
		return this.serverTrueWorldGameState;
	}



}
