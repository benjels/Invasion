package gamelogic;

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

	private HashMap<Integer, Player> uidToPlayerMap = new HashMap<>();//used to associate a unique id from a requested move sent over the network with a Player.
	private  RoomState spawnRoom; //the room that players will spawn in when they join the server.
	private int timeOfDay = 10; //time of day in military/24 hour time
	/**
	 * applies the event to the game world ONLY EVENTS ARE MOVES ATM
	 * decided whether the event needs to be passed onto the board to check for validity or if the event is applied in some other way (e.g. a suicide event would not require checking by the board).
	 * @param eventWeNeedToUpdateLocalStateWith
	 * @return bool true if the event was applied to the game world, else false
	 */
	 boolean applyEvent(IDedPlayerEvent eventWeNeedToUpdateStateWith) {

		//FIND WHICH PLAYER WE ARE APPLYING THE EVENT FOR
		Player actingPlayer = this.uidToPlayerMap.get(eventWeNeedToUpdateStateWith.getUid());

		//THE ABSTRACTION IS THAT THIS IS ENTIRE GAME STATE AND ROOMSTATE INSTANCES ARE SPATIAL STATE
		//WE MAY NEED OTHER KINDS OF EVENTS IN THE FUTURE THAT DO NOT CARE ABOUT THE ROOMS

		//SEND THAT REQUESTED DIRECTION ALONG WITH PLAYER TO THE APPROPRIATE ROOM OBJECT THEY OCCUPY TO ATTEMPT TO APPLY MOVE
		//!!!i.e. if the event is an instanceof spatialevent or watev

		return actingPlayer.getCurrentRoom().attemptGameMapEventByPlayer(actingPlayer, eventWeNeedToUpdateStateWith);

	}







	 void addPlayerToGameState(Player myPlayer) {

		//attempt to place the character in the spawn room
		RoomLocation spawnLocation = this.spawnRoom.spawnPlayerInRoom(myPlayer);
		if(spawnLocation == null){
			throw new RuntimeException("must be able to place the player somewhere");//TODO: set this to a default value first so player gets put somewhere. OR MAYBE HAVE IT LOOP ROOM EVERY 1 SECOND TO LOOK FOR A SPACE
		}

		//set the player's current room to the spawn room
		myPlayer.setCurrentRoom(this.spawnRoom);

		//set the player's x and y to the x and y that they were placed at.
		myPlayer.setxInRoom(spawnLocation.getX());
		myPlayer.setyInRoom(spawnLocation.getY());

		//add the player to uid --> player map
		this.uidToPlayerMap.put(myPlayer.getUid(), myPlayer);

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

			 //create the drawable room state
			DrawableRoomState playerDrawableRoomState = new DrawableRoomState(tiles, entities, timeOfDay, currentUp, playerLocation);


			//get the info that is needed for the hud from the Player
			RoomState playerRoom = this.uidToPlayerMap.get(uid).getCurrentRoom();

			int playerCoins = this.uidToPlayerMap.get(uid).getCoins();

			int playerHp = this.uidToPlayerMap.get(uid).getHealthPercentage();

			CharacterStrategy playerCharacter = this.uidToPlayerMap.get(uid).getCharacter();

			String playerRealName = this.uidToPlayerMap.get(uid).getIrlName();

			//create the DrawablePlayerInfo object for this player
			DrawablePlayerInfo playerInfo = new DrawablePlayerInfo(playerRoom, playerCoins, playerHp, playerCharacter, playerRealName, 10);//TODO: add in score field

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










}
