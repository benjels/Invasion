package gamelogic;

import gamelogic.entities.IndependentActor;
import gamelogic.events.PlayerEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class manages at a high level what the enemy entities in the game should be doing on every tick.
 * when it receives the tick/pulse, it scrapes the event buffer of each of the EnemyMaster instances (which it has stored in a collection).
 * These events are placed in a list and then returned back to the tick method in the server where they are applied to the game state
 *
 * Note that when an enemy "dies" in the game, that object is not really destroyed, its state just changes. e.g. if a player kills a zombie in room1, that zombie might have its "status"
 * field set to "dead" and then be moved to "respawn" in some other room.
 * @author brownmax1
 *
 */
public class IndependentActorManager {

	private final HashMap<Integer, IndependentActor> enemies;

	public IndependentActorManager(HashMap<Integer, IndependentActor> enemies){
		this.enemies = enemies;
	}

	/**
	 * used to start the thread which runs inside each individual enemy's AI thread object
	 * when this is called, the enemies will start generating their events so that this object can be scraped to collect all of their desired events.
	 * Note that we are not going to applying those events until the "main" ClockThread starts sending out pulses to actually scrape the enemies/players.
	 */
	public void startEnemies(){
		for(IndependentActor eachEnemy: this.enemies.values()){
			eachEnemy.beginAi();
		}
	}

	/**
	 * is called on clock tick to return a list of the events from all of the enemies
	 * @return List the list of events from the enemies that need to be applied
	 */
	ArrayList<PlayerEvent> retrieveEnemyEventsOnTick(){
		//create the list that we will fill with events
		ArrayList<PlayerEvent> enemyEvents = new ArrayList<PlayerEvent>();

		//scrape the enemy events
		for(IndependentActor eachEnemy: this.enemies.values()){
			enemyEvents.add(eachEnemy.scrapeEnemyEvent());
		}

		//return the events to be added to the queue of events that will be applied to game state on this tick
		return enemyEvents;
	}



}
