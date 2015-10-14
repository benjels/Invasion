package gamelogic;

import gamelogic.entities.Carryable;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.Player;
import gamelogic.entities.Pylon;
import gamelogic.entities.RenderEntity;
import gamelogic.events.CarrierOpenCloseEvent;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.InventorySelectionEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.events.RotateMapClockwise;
import gamelogic.events.SaveGameEvent;
import gamelogic.events.SpatialEvent;
import gamelogic.tiles.RenderRoomTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import storage.XMLWriter;

/**
 * maintains all game state.
 * essentially this is a wrapper for the rooms and movable entities
 * also has some logic for maintaining game state that is not specific to a certain room or actor e.g. the game world time
 * @author brownmax1
 *
 */
public class WorldGameState {




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

			boolean gameOver = false;

			//check game over conditions
			for(MovableEntity each: this.uidToMovableEntity.values()){
				if(each instanceof Player && ((Player) each).isDead()){
					gameOver = true;
				}
			}
			//game also ends if pylons are dead
			if(this.topPylon.isPylonDead() || this.bottomPylon.isPylonDead()){
				gameOver = true;
			}

			 //create the drawable room state
			DrawableRoomState playerDrawableRoomState = new DrawableRoomState(tiles, entities, timeOfDay, currentUp, playerLocation, roomId, isDark, gameOver);


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
