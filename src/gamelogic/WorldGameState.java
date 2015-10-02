package gamelogic;

import gamelogic.entities.Carryable;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.IndependentActor;
import gamelogic.events.InventorySelectionEvent;
import gamelogic.events.PlayerEvent;
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
	private  RoomState spawnRoom; //the room that players will spawn in when they join the server.
	private int timeOfDay = 10; //time of day in military/24 hour time



	public WorldGameState(HashMap<Integer, RoomState> rooms){
		this.roomsCollection = rooms;
	}


	/**
	 * applies the event to the game world ONLY EVENTS ARE MOVES ATM
	 * decided whether the event needs to be passed onto the board to check for validity or if the event is applied in some other way (e.g. a suicide event would not require checking by the board).
	 * @param eventWeNeedToUpdateLocalStateWith
	 * @return bool true if the event was applied to the game world, else false
	 */
	 boolean applyEvent(PlayerEvent eventWeNeedToUpdateStateWith) {

		//FIND WHICH PLAYER WE ARE APPLYING THE EVENT FOR
		Player actingPlayer = this.uidToPlayerMap.get(eventWeNeedToUpdateStateWith.getUid());

		//THE ABSTRACTION IS THAT THIS IS ENTIRE GAME STATE AND ROOMSTATE INSTANCES ARE SPATIAL STATE
		//WE MAY NEED OTHER KINDS OF EVENTS IN THE FUTURE THAT DO NOT CARE ABOUT THE ROOMS

		//SEND THAT REQUESTED DIRECTION ALONG WITH PLAYER TO THE APPROPRIATE ROOM OBJECT THEY OCCUPY TO ATTEMPT TO APPLY MOVE
		//!!!i.e. if the event is an instanceof spatialevent or watev

		//if the attempted event is a spacial event, it needs to be checked by the entities' current room
		if(eventWeNeedToUpdateStateWith instanceof SpatialEvent){
			return actingPlayer.getCurrentRoom().attemptGameMapEventByPlayer(actingPlayer, (SpatialEvent) eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof InventorySelectionEvent){ // if it's an inventory event, check it in player's inventory
			return actingPlayer.getInventory().attemptInventorySelectionEventByPlayer((InventorySelectionEvent) eventWeNeedToUpdateStateWith);
		}else{
			throw new RuntimeException("this not supported atm");
		}

	}





	 /**
	  * adds an entity to the correct place in the game world and updates their internal x and y with where they are place
	  * NOTE THAT THIS DOES NOT TAKE CARE OF PLACING ENTITIES IN ANY MANAGING COLLECTIONS.
	  * used to add entities by the higher level
	  * @param entToAdd the entity that we are adding to the game state
	  * @param roomToAddIn the room that we are adding our entity into
	  * @param x the x position the entity will take in that room
	  * @param y the y position the entity will take in that room
	  */
	public void addMovableEntityToRoomState(MovableEntity entToAdd, int roomToAddInId, int x, int y) {
		//place the entity in that room

		if(!this.roomsCollection.get(roomToAddInId).attemptToPlaceEntityInRoom(entToAdd, x, y)){
			throw new RuntimeException("THIS SHOULD BE HANDLED AT THE HIGHEST LEVEL E.G. BY THE EnemyManager WHICH KEEPS TRYING TO INSERT ENEMIES AT SLIGHTLY DIFF LOCATIONS IF THIS FAILS");

		}

		//set the entitity's current room to the room that we spawned them in
		entToAdd.setCurrentRoom(this.roomsCollection.get(roomToAddInId));

		//set the player's x and y to the x and y that they were placed at.
		entToAdd.setxInRoom(x);
		entToAdd.setyInRoom(y);



	}









		/**
		 * creates the drawable version of a room for a particular player in the game
		 * this is what will be sent back across network to be drawn on each tick
		 * @param uid the unique id of the player whose drawable room we are creating
		 * @return the drawable version of the room state
		 */
		 ClientFrame generateFrameForClient(int uid) {
			 //TODO: note that we are currently not deep copying the arrays so if miguel alters them
			 //n the Master class, it will break the game. Perhaps implement a deep copy for all tiles
			 //and GameEntities in the future. shouldnt be too hard. just make a clone method in the roomstate and have clone methods in every kind of entitiy and tile  ez

			 //create a deep copy of the tiles on the board
			 RenderRoomTile[][] tiles = this.uidToPlayerMap.get(uid).getCurrentRoom().generateDrawableTiles();

			 //create a copy of the entities on the board
			 RenderEntity[][] entities = this.uidToPlayerMap.get(uid).getCurrentRoom().generateDrawableEntities();

			 //get the time of day that will be included in the DrawableGameState
			 int timeOfDay = this.timeOfDay;

			 //get the orientation of this room
			 CardinalDirection currentUp = this.uidToPlayerMap.get(uid).getDirectionThatIsUp();

			 //get the location of the player in this room
			 RoomLocation playerLocation = this.uidToPlayerMap.get(uid).getLocation();

			 //get the unique room id from the room
			 int roomId = this.uidToPlayerMap.get(uid).getCurrentRoom().getId();

			 //create the drawable room state
			DrawableRoomState playerDrawableRoomState = new DrawableRoomState(tiles, entities, timeOfDay, currentUp, playerLocation, roomId);


			//get the info that is needed for the hud from the Player
			int playerRoomId = this.uidToPlayerMap.get(uid).getCurrentRoom().getId();

			int playerCoins = this.uidToPlayerMap.get(uid).getCoins();

			int playerHp = this.uidToPlayerMap.get(uid).getHealthPercentage();

			CharacterStrategy playerCharacter = this.uidToPlayerMap.get(uid).getCharacter();

			String playerRealName = this.uidToPlayerMap.get(uid).getIrlName();

			ArrayList<RenderEntity> inventory = this.uidToPlayerMap.get(uid).getInventory().generateDrawableInventory();


			//TODO: add score



			//create the DrawablePlayerInfo object for this player
			DrawablePlayerInfo playerInfo = new DrawablePlayerInfo(playerRoomId, playerCoins, playerHp, playerCharacter, playerRealName, 10, inventory);//TODO: add in score field

			//wrap the DrawableGameState and DrawablePlayerInfo objects in a ClientFrame object to be sent to client

			return new ClientFrame(playerDrawableRoomState, playerInfo);

		 }



		 /**
		  * hascky asf shit to get the non-networked version allg fam
		  * @param spawnRoom
		  */
	public void setSpawnRoom(RoomState spawnRoom) {
		this.spawnRoom = spawnRoom;
	}

//USED TO ADD A PLAYER TO THE INT ID -> PLAYER MAP.
	//WE DONT DO THIS IN THE ADD ENTITY TO ROOM METHOD BECAUSE THAT MIGHT BE A PLAYER OR AN ENEMY
		public void addPlayerToMap(Player myPlayer) {
			this.uidToPlayerMap.put(myPlayer.getUid(), myPlayer);

		}

















}
