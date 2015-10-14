package gamelogic;
import gamelogic.events.PlayerEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.management.RuntimeErrorException;
/**
 * this class manages at a high level what the enemy entities in the game should be doing on every tick.
 * when it receives the tick/pulse, it scrapes the event buffer of each of the EnemyMaster instances (which it has stored in a collection).
 * These events are placed in a list and then returned back to the tick method in the server where they are applied to the game state


 * @author brownmax1
 *
 */
public class IndependentActorManager {
	private final HashMap<Integer, IndependentActor> npcs;
	private int pylonAttackerCount = 0;//the amount of pylonAttackers currently managed
	private int zombieCount = 0; //the amount of zombies that are currently managed
	private boolean attackTopPylonNext = true; //used to determine where to spawn the next wave of pylon attackers
	private final int TOP_PYLON_ROOM_ID = 0;
	private final int BOTTOM_PYLON_ROOM_ID = 5;
	private final WorldGameState trueWorldGameState;
	private int explosionCount = 200;



	public IndependentActorManager( WorldGameState initialState){
		this.npcs = new HashMap<Integer, IndependentActor>();
		this.trueWorldGameState = initialState;
		//create startup entities
		this.initialiseStartupNpcs();
	}
	//used by setup to pouplate enemies map
	private void initialiseStartupNpcs(){
	//create pylon attacker wave (at the moment that is the enemies at the start)
		createPylonAttackerWave();
	}
	/**
	 * used to create a wave of pylon attackers in the appropriate room
	 */
	private void createPylonAttackerWave(){

		//determine which of the two pylon rooms this wave is for
		PylonRoomState roomToAttack;
		if(this.attackTopPylonNext){

			if(this.trueWorldGameState.getRooms().get(TOP_PYLON_ROOM_ID) instanceof PylonRoomState){

			}

			roomToAttack = ((PylonRoomState) this.trueWorldGameState.getRooms().get(TOP_PYLON_ROOM_ID));
		}else{//in the case that we attacking bottom room
			roomToAttack = ((PylonRoomState) this.trueWorldGameState.getRooms().get(BOTTOM_PYLON_ROOM_ID));
		}
		//create a pylon attacker above, below, left, right of the pylon
		//top
		IndependentActor top = new IndependentActor(CardinalDirection.SOUTH, 10, roomToAttack);
		PylonAttackerStrategy topStrat = new PylonAttackerStrategy(top, CardinalDirection.SOUTH);
		//bottom
		IndependentActor bottom = new IndependentActor(CardinalDirection.NORTH, 11, roomToAttack);
		PylonAttackerStrategy bottomStrat = new PylonAttackerStrategy(bottom, CardinalDirection.NORTH);
		//left
		IndependentActor left = new IndependentActor(CardinalDirection.EAST, 12, roomToAttack);
		PylonAttackerStrategy leftStrat = new PylonAttackerStrategy(left, CardinalDirection.EAST);
		//right
		IndependentActor right = new IndependentActor(CardinalDirection.WEST, 13, roomToAttack);
		PylonAttackerStrategy rightStrat = new PylonAttackerStrategy(right, CardinalDirection.WEST);

		//we created all of our pylon attackers, so now attempt to place them all in the correct room
		//and then add the ones that were placed to our maps
		//add attackers to wave map
		HashMap<Integer, IndependentActor> waveMap = new HashMap<Integer, IndependentActor>();
		waveMap.put(top.getUniqueId(), top);
		waveMap.put(bottom.getUniqueId(), bottom);
		waveMap.put(left.getUniqueId(), left);
		waveMap.put(right.getUniqueId(), right);

		//attempt to spawn the attackers in the room, add the successfully spawned ones to our maps
		HashMap<Integer, IndependentActor> spawnedAttackers;
		spawnedAttackers = roomToAttack.spawnPylonAttackerWave(waveMap);

		//we might not have successfully spawned all of the attackers we made (e.g. if someone in the way of spawn area)
		//so add the attackers that were spawned successfully to the npcs map here and the MovableEntity map in the worldgamestate
		this.npcs.putAll(spawnedAttackers);
		for(IndependentActor each: spawnedAttackers.values()){
			this.trueWorldGameState.addMovableToMap(each);
		}

		//we added a certain amount of npcs to the game, so increment our count of them
		this.pylonAttackerCount += spawnedAttackers.size();

		//now our wave of pylon attackers has spawned and they should start generating events
		for(IndependentActor each: spawnedAttackers.values()){
			each.beginAi();
		}
	}
	/**
	 * used to create a wave of zombies in the appropriate room
	 */
	private void createZombieAttackerWave() {

		//determine which of the two pylon rooms this wave is for
		PylonRoomState roomToAttack;
		if(this.attackTopPylonNext){
			roomToAttack = ((PylonRoomState) this.trueWorldGameState.getRooms().get(TOP_PYLON_ROOM_ID));
		}else{//in the case that we attacking bottom room
			roomToAttack = ((PylonRoomState) this.trueWorldGameState.getRooms().get(BOTTOM_PYLON_ROOM_ID));
		}
		//create a pylon attacker above, below, left, right of the pylon
		//top
		IndependentActor top = new IndependentActor(CardinalDirection.SOUTH, 30, roomToAttack); //TODO: maintain an id allocator that goes up to like 500 and then resets back to 0
		ZombieStrategy topStrat = new ZombieStrategy(top);
		//bottom
		IndependentActor bottom = new IndependentActor(CardinalDirection.NORTH, 31, roomToAttack); //TODO: maintain an id allocator that goes up to like 500 and then resets back to 0
		ZombieStrategy bottomStrat = new ZombieStrategy(bottom);
		//left
		IndependentActor left = new IndependentActor(CardinalDirection.EAST, 32, roomToAttack); //TODO: maintain an id allocator that goes up to like 500 and then resets back to 0
		ZombieStrategy leftStrat = new ZombieStrategy(left);
		//right
		IndependentActor right = new IndependentActor(CardinalDirection.WEST, 33, roomToAttack); //TODO: maintain an id allocator that goes up to like 500 and then resets back to 0
		ZombieStrategy rightStrat = new ZombieStrategy(right);//alternatively could just reuse the same ids and only respawn a wave when they all dead or some shit

		//we created all of our zombies, so now attempt to place them all in the correct room
		//and then add the ones that were placed to our maps
		//add attackers to wave map
		HashMap<Integer, IndependentActor> waveMap = new HashMap<Integer, IndependentActor>();

		waveMap.put(top.getUniqueId(), top);

		//attempt to spawn the attackers in the room, add the successfully spawned ones to our maps
		HashMap<Integer, IndependentActor> spawnedAttackers;
		spawnedAttackers = roomToAttack.spawnZombieAttackerWave(waveMap);

		//we might not have successfully spawned all of the attackers we made (e.g. if someone in the way of spawn area)
		//so add the attackers that were spawned successfully to the npcs map here and the MovableEntity map in the worldgamestate
		this.npcs.putAll(spawnedAttackers);
		for(IndependentActor each: spawnedAttackers.values()){
			this.trueWorldGameState.addMovableToMap(each);
		}
		//we added a certain amount of npcs to the game, so increment our count of them
		this.zombieCount += spawnedAttackers.size();

		//now our wave of zombie attackers has spawned and they should start generating events
		for(IndependentActor each: spawnedAttackers.values()){
			each.beginAi();
		}
	}


	/**
	 * creates an explosion entity at the position indicated to represent an explosion in the game state
	 * @param room the room to put the explosion
	 * @param x the x location in the room to place the explosion
	 * @param y the y location in the room to place the explosion
	 */
	private void createExplosionForZombie(RoomState room,int x, int y) {

		IndependentActor explosion = new IndependentActor(CardinalDirection.NORTH, this.explosionCount, room); //TODO: maintain an id allocator that goes up to like 500 and then resets back to 0

		//we made an explosion so increment the counter
		this.explosionCount ++;
		AiStrategy explosionStrat = new ExplosionStrategy(explosion);

		//add explosion to wave map
		HashMap<Integer, IndependentActor> waveMap = new HashMap<Integer, IndependentActor>();
		waveMap.put(explosion.getUniqueId(), explosion);

		//cause damage in a radius around the explosion
		room.resolveExplosion(x, y);

		//attempt to place the explosion in the game world
		HashMap<Integer, IndependentActor> spawnedExplosion = new HashMap<Integer, IndependentActor>();
		if(room.attemptToPlaceEntityInRoom(explosion, x, y)){
			spawnedExplosion.put(explosion.getUniqueId(), explosion);
		}
		//we might not have successfully spawned the explosion we made (e.g. if someone in the way of spawn area)
		//so add the explosions that were spawned successfully to the npcs map here and the MovableEntity map in the worldgamestate
		this.npcs.putAll(spawnedExplosion);
		for(IndependentActor each: spawnedExplosion.values()){
			this.trueWorldGameState.addMovableToMap(each);
		}
		//we either added one explosion or 0 explosions to the game, so increment the size of explosions
		this.explosionCount += spawnedExplosion.size();


		//CURRENTLY EXPLOSIONS DO NOT DEAL DAMAGE OVER TIME so the ai has no real behaviour
		for(IndependentActor each: spawnedExplosion.values()){
			each.beginAi();
		}
	}



	/**
	 * is called on clock tick to return a list of the events from all of the npcs that this object is managing.
	 * @return List the list of events from the enemies that need to be applied
	 */
	protected ArrayList<PlayerEvent> retrieveEnemyStatusOnTick(){

		//create the list that we will fill with events
		ArrayList<PlayerEvent> enemyEvents = new ArrayList<PlayerEvent>(0);//initialise the list to 0 size that it will only be filled with added events

		//we should use an iterator to traverse the set of actors because we may be removing from the map
		Iterator<IndependentActor> iter = this.npcs.values().iterator();
		while (iter.hasNext()){
			IndependentActor each = iter.next();

			//clean up dead actors
			if (each.isDead()){

				//remove this actor from the room array it is in
				each.getCurrentRoom().removeRedundantGameEntity(each.getxInRoom(), each.getyInRoom());

				//remove from the map of MovableEntities in the worldgamestate
				  this.trueWorldGameState.getMovableEntites().remove(each);

				  //remove from the map in here thatwe are iterating over
				  iter.remove();

				  //decrement appropriate counts
				  if(each.getCurrentBehaviour() instanceof PylonAttackerStrategy ){
					  this.pylonAttackerCount --;
				  }else if(each.getCurrentBehaviour() instanceof ZombieStrategy){
					  this.zombieCount --;

					  //if a zombie died, then we need to create an explosion there
					  createExplosionForZombie(each.getCurrentRoom(), each.getxInRoom(), each.getyInRoom());
				  } else{
					  assert false: "dont have a count for this enemy type yet";
				  }
			}

			//get the events of living actors
			if(each.hasEvent()){
				enemyEvents.add(each.scrapeEnemyEvent());
			}
		}

		//put new actors in if we deem it appropriate
		//if the players killed all the pylon attackers, spawn more
		//same with the zombies
		if(this.pylonAttackerCount == 0){

			//we alternate which pylon is attacked by pylon attackers
			if(this.attackTopPylonNext){
				attackTopPylonNext = false;
			}else{
				attackTopPylonNext = true;
			}
			createPylonAttackerWave();
		}

		//put in some zombies for testing
		if(this.zombieCount == 0){
			createZombieAttackerWave();
		}

		//return the events to be added to the queue of events that will be applied to game state on this tick
		return enemyEvents;
	}
	private void testInvariant(){
		assert(this.npcs.size() == this.pylonAttackerCount):"should not be managing more than count of added/removed ais";
	}
}