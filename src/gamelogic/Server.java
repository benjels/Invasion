package gamelogic;

import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.PlayerNullEvent;
import gamelogic.events.SaveGameEvent;
import graphics.GameCanvas;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import storage.XMLParser;
import storage.XMLWriter;
import ui.GameGui;
import control.Controller;
import control.DummyMaster;
import control.DummySlave;
import control.NewGameEvent;

/**
 * this is the class that maintains the "true" state of the game. It receives desired events from the players via their Masters and events from the
 * IndependentActors via the IndependentActorManager
 * If a desired event is valid (e.g. they are not trying to move into a wall or pick up a tree etc), then we will enqueue this event.
 *
 * each time the clock ticks, the server will dequeue the event at the head of the queue and apply it to the game state, then create a drawable version of that and
 * send it out to all of the players
 * @author brownmax1
 *
 *
 */
public class Server{


	private final WorldGameState serverTrueWorldGameState;// the server's version of the physical game state
	private ArrayList<DummyMaster> masters = new ArrayList<>();//the masters that dequeued events will be sent to
	private final IndependentActorManager enemyManager; //the enemy manager that the server communicates with to get the AIs events



	public Server( WorldGameState initGameWorld, IndependentActorManager enemyManager){
		this.serverTrueWorldGameState =  initGameWorld;
		this.enemyManager = enemyManager;
	}

	public Server (DummySlave slave, String name, CharacterStrategy character){
		XMLParser parser = new XMLParser();
		serverTrueWorldGameState = parser.parse(new File ("Standard-Entities.xml"));

		GameWorldTimeClockThread realClock = new GameWorldTimeClockThread(serverTrueWorldGameState);

		this.enemyManager = new IndependentActorManager(serverTrueWorldGameState);


		Player player = new Player(name, 0, character, CardinalDirection.NORTH, serverTrueWorldGameState.getRooms().get(0));

		this.getWorldGameState().addMovableToMap(player);
		this.getWorldGameState().getRooms().get(0).attemptToPlaceEntityInRoom(player, 20, 20);

		//connect the slave to the server which creates/spawns the player too
		slave.connectToServer(this);

		ClockThread clock = new ClockThread(35, this);
		realClock.start();
		clock.start();


	}




	/**
	 * on every clock tick we need to get all od the events from the masters and the independent actors and apply
	 * them to the game state. We then need to generate a drawable version of the room that each player is in and send that to the player
	 * over the network so that they can see what has happended.
	 */
		protected void clockTick() {



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
			eventsToAttemptToApplyToGameState.remove(0);
			if(!attemptToApplyEvent(headEvent)){
				//do nothing...
			}
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
	private boolean attemptToApplyEvent(PlayerEvent desiredEvent) {
		if(desiredEvent instanceof SaveGameEvent){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			chooser.showOpenDialog(new JFrame("Save File"));
			File file = chooser.getSelectedFile();
			XMLWriter writer = new XMLWriter();
			writer.saveState(file, serverTrueWorldGameState);
		}
		return this.serverTrueWorldGameState.applyEvent(desiredEvent);
	}





///REGISTERING/JOINING SHIT///
	//NOTE!!!: MUCH OF THIS IS DUMMY CODE THAT SHOULD HAVE BEEN REPLACED BUT THE ACTUAL NETWORKING COMPONENT OF THE PROJECT THAT WAS NEVER DONE

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




//NOTE THAT THE BELOW GETTER NEEDED FOR HACKY SETUP, SHOULDNT BE USED AFTER INTEGRATION MAYBE
	public WorldGameState getWorldGameState(){
		return this.serverTrueWorldGameState;
	}



}
