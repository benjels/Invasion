package gamelogic;

import gamelogic.entities.Carryable;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.entities.Pylon;
import gamelogic.entities.RenderEntity;
import gamelogic.events.CarrierOpenCloseEvent;
import gamelogic.events.InventorySelectionEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.RotateMapClockwise;
import gamelogic.events.SpatialEvent;
import gamelogic.tiles.RenderRoomTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * represents all state in the game world
 * has a collection of rooms, a collection of players, (maybe other stuff like collection of enemy ids or something idk)
 * @author brownmax1
 *
 */
public class WorldGameState {


//WILL NEED ALL OF THE PLAYERS AND LILKE A MAP TO GET PLAYER FROM PLAYER ID (to find out which player we should be applying an action to) AND WILL NEED AN OBJECT FOR ALL THE ROOMS AND MAYBE NEEDS A COLLECTION OF AIs BUT DO THAT LATER
//note that Players can probably exist solely on the serverside of the program. All a slave has is the unique id that corresponds to their Player.
	//we can prob connect a player to the server by sending a JoinRequestEvent. We could use something unique (IP) about each machine to generate the UID or else
	//the server could generate one or something.

	private HashMap<Integer, MovableEntity> uidToMovableEntity = new HashMap<>();//used to associate a unique id from a requested move sent over the network with a Player.
	private HashMap<Integer, RoomState> roomsCollection; //used as centralised collection of rooms atm
	private String timeOfDay = "unstarted..."; //set by the clock
	private int playerScore = 0; // the score that this player has (just amount of game minutes that they and pylons survived)
	private final Pylon topPylon;
	private final Pylon bottomPylon;


	public WorldGameState(HashMap<Integer, RoomState> rooms, Pylon top, Pylon bottom){
		this.roomsCollection = rooms;
		this.topPylon = top;
		this.bottomPylon = bottom;
	}


	/**
	 * applies the event to the game world DECIDES WHERE/HOW IN THE GAME WORLD THIS EVENT NEEDS TO BE DELEGATED
	 * decided whether the event needs to be passed onto the board to check for validity or if the event is applied in some other way (e.g. a suicide event would not require checking by the board).
	 * @param eventWeNeedToUpdateLocalStateWith
	 * @return bool true if the event was applied to the game world, else false
	 */
	 protected boolean applyEvent(PlayerEvent eventWeNeedToUpdateStateWith) {
		//FIND WHICH ACTOR WE ARE APPLYING THE EVENT FOR
		/*System.out.println("so we are applying an event");
		System.out.println("the event is:" + eventWeNeedToUpdateStateWith);*/
		 MovableEntity actor = this.uidToMovableEntity.	get(eventWeNeedToUpdateStateWith.getUid());


		//WE MAY NEED OTHER KINDS OF EVENTS IN THE FUTURE THAT DO NOT CARE ABOUT THE ROOMS


		//if the attempted event is a spacial event, it needs to be checked by the entities' current room
		if(eventWeNeedToUpdateStateWith instanceof SpatialEvent){
			return actor.getCurrentRoom().attemptGameMapEventByPlayer(actor, (SpatialEvent) eventWeNeedToUpdateStateWith);
		}

		//if the attempted event is an inventory selection event, we need to send it to the inventory
		else if(eventWeNeedToUpdateStateWith instanceof InventorySelectionEvent){ // if it's an inventory event, check it in player's inventory
			assert(actor instanceof Player):"note that eventually the game wont crash when e.g. a zombie attempts to pickup. that event might just be meaningless with their item strategy";
			Player playerActor = (Player)actor;
			return playerActor.getCurrentInventory().attemptInventorySelectionEventByPlayer((InventorySelectionEvent) eventWeNeedToUpdateStateWith);
		}

		//likewise if it is a carrier open/drop event
		else if(eventWeNeedToUpdateStateWith instanceof CarrierOpenCloseEvent){
			assert(actor instanceof Player):"note that eventually the game wont crash when e.g. a zombie attempts to pickup. that event might just be meaningless with their item strategy";
			Player playerActor = (Player)actor;
			return playerActor.getCurrentInventory().attemptSwitchCurrentInventoryEventByPlayer((CarrierOpenCloseEvent)eventWeNeedToUpdateStateWith);

		}else if(eventWeNeedToUpdateStateWith instanceof RotateMapClockwise){//in the case that it is an orientation rotation event
			assert(actor instanceof Player):"you need to be a player to rotate the view tbh";
			Player playerActor = (Player)actor;
			return playerActor.attemptClockwiseRotationEvent(eventWeNeedToUpdateStateWith);
		}

		else{
			throw new RuntimeException("this kind of event is not supported atm");
		}

	}


	 //TODO vvv maybe just prefer to use getRooms and then add shit in directly through there
	 //all this middleman shit u feel
	 //ACTUALLY STILL NEED TO BE ABLE TO ADD MOVABLES TO MAP BECAUSE OF HOW EVENTS ARE PASSED THROUGH
	 //HERE AND SHIT. PROB SHOULD BE SPAWNING EVERYTHING BY JSUT GETTING THE ROOMS AND USING SPAWN
	 //METHODS IN THERE THO

/*
	 *//**
	  * adds an entity to the correct place in the game world and updates their internal x and y with where they are place MARKED FOR DELETION
	  * NOTE THAT THIS DOES NOT TAKE CARE OF PLACING ENTITIES IN ANY MANAGING COLLECTIONS.
	  * used to add entities by the higher level
	  * @param entToAdd the entity that we are adding to the game state
	  * @param roomToAddIn the room that we are adding our entity into
	  * @param x the x position the entity will take in that room
	  * @param y the y position the entity will take in that room
	  *//*
	public boolean addMovableEntityToRoomState(MovableEntity entToAdd, int roomToAddInId, int x, int y) {

		//attempt to place the entity in that room
		boolean managedToPlace = this.roomsCollection.get(roomToAddInId).attemptToPlaceEntityInRoom(entToAdd, x, y);

		//update internal positions if we re-placed the player
		if(managedToPlace){
			entToAdd.setCurrentRoom(this.roomsCollection.get(roomToAddInId));
			entToAdd.setxInRoom(x);
			entToAdd.setyInRoom(y);
		}

		return managedToPlace;

	}*/

	//USED TO ADD A a ent TO THE INT ID -> PLAYER MAP.
		//WE DONT DO THIS IN THE ADD ENTITY TO ROOM METHOD BECAUSE THAT MIGHT BE A PLAYER OR AN ENEMY
			public void addMovableToMap(MovableEntity eachActor) {
				this.uidToMovableEntity.put(eachActor.getUniqueId(), eachActor);

			}






		/**
		 * creates the drawable version of a room for a particular player in the game
		 * this is what will be sent back across network to be drawn on each tick
		 * @param uid the unique id of the player whose drawable room we are creating
		 * @return the drawable version of the room state
		 */
		protected ClientFrame generateFrameForClient(int uid) {

			 assert(this.uidToMovableEntity.get(uid) instanceof Player):"shouldnt be generating a frame for a non player entitiy";

			 //get the Player that we are creating a frame for
			 Player playerFrameFor = (Player)this.uidToMovableEntity.get(uid);


			 //TODO: note that we are currently not deep copying the arrays so if miguel alters them
			 //n the Master class, it will break the game. Perhaps implement a deep copy for all tiles
			 //and GameEntities in the future. shouldnt be too hard. just make a clone method in the roomstate and have clone methods in every kind of entitiy and tile  ez

			 //create a deep copy of the tiles on the board
			 RenderRoomTile[][] tiles = playerFrameFor.getCurrentRoom().generateDrawableTiles();

			//get the light/darkness of how the room should be drawn
			 boolean isDark = playerFrameFor.getCurrentRoom().isDark();

			 //create a copy of the entities on the board
			 RenderEntity[][] entities;
			if(!isDark){
				entities = playerFrameFor.getCurrentRoom().generateDrawableEntities();
			}else{
				entities = playerFrameFor.getCurrentRoom().generateDrawableEntitiesDarkRoom(playerFrameFor.getxInRoom(), playerFrameFor.getyInRoom(), playerFrameFor.hasNightVisionEnabled());
			}

			 //get the time of day that will be included in the DrawableGameState
			 String timeOfDay = this.timeOfDay;

			 //get the orientation of this room
			 CardinalDirection currentUp = playerFrameFor.getDirectionThatIsUp();

			 //get the location of the player in this room
			 RoomLocation playerLocation = playerFrameFor.getLocation();

			 //get the unique room id from the room
			 int roomId = playerFrameFor.getCurrentRoom().getId();



			 //create the drawable room state
			DrawableRoomState playerDrawableRoomState = new DrawableRoomState(tiles, entities, timeOfDay, currentUp, playerLocation, roomId, isDark);


			//get the info that is needed for the hud from the Player
			int playerRoomId = playerFrameFor.getCurrentRoom().getId();

			int playerCoins = playerFrameFor.getCoins();

			int playerHp = playerFrameFor.getHealthPercentage();

			CharacterStrategy playerCharacter =playerFrameFor.getCharacter();

			String playerRealName = playerFrameFor.getIrlName();

			String currentRoomName = this.roomsCollection.get(playerRoomId).toString();//TODO: should not use toString should use like "getRoomDesc"

			ArrayList<RenderEntity> inventory = playerFrameFor.getCurrentInventory().generateDrawableInventory();

			int invSlot = playerFrameFor.getCurrentInventory().getSelectedIndex();

			//create the DrawablePlayerInfo object for this player
			DrawablePlayerInfo playerInfo = new DrawablePlayerInfo(playerRoomId, playerCoins, playerHp, playerCharacter, playerRealName, this.playerScore, inventory, this.topPylon.getHealthPercentage(), this.bottomPylon.getHealthPercentage(), currentRoomName, this.timeOfDay, invSlot);//TODO: unhardcode score field, pylon hp

			//wrap the DrawableGameState and DrawablePlayerInfo objects in a ClientFrame object to be sent to client

			return new ClientFrame(playerDrawableRoomState, playerInfo);

		 }







		public HashMap<Integer, RoomState> getRooms() {
			return roomsCollection;
		}

		public HashMap<Integer, MovableEntity> getMovableEntites(){
			return uidToMovableEntity;
		}

//USED FOR THE DAY / NIGHT CYCLES
		protected void setDark(boolean isDark){
			for(RoomState eachRoom: this.roomsCollection.values()){
				eachRoom.setDark(isDark);
			}
		}


		public void setTimeOfDay(String newTime){
			this.timeOfDay = newTime;
		}



//USED BY THE GAME CLOCK TO GIVE THE PLAYER MORE SCORE EVERY MINUTE
		protected void incrementPlayerScore() {
			this.playerScore ++;
			System.out.println("player now has: " + this.playerScore + " points");
		}


		//JOSH ADDED THIS
		public int getScore(){
			return playerScore;
		}

		public void setScore(int score){
			this.playerScore = score;
		}









}
