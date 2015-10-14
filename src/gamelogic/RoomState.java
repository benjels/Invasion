package gamelogic;

import java.util.HashMap;

import gamelogic.entities.Carryable;
import gamelogic.entities.Coin;
import gamelogic.entities.Damageable;
import gamelogic.entities.GameEntity;
import gamelogic.entities.Gun;
import gamelogic.entities.HealthKit;
import gamelogic.entities.MazeWall;
import gamelogic.entities.KeyCard;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.NightVisionGoggles;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Player;
import gamelogic.entities.Pylon;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderNullEntity;
import gamelogic.entities.SmallCarrier;
import gamelogic.entities.TeleporterGun;
import gamelogic.entities.Treasure;
import gamelogic.events.MeleeAttackEvent;
import gamelogic.events.MovementEvent;
import gamelogic.events.PlayerDropEvent;
import gamelogic.events.PlayerHealEvent;
import gamelogic.events.PlayerMoveDown;
import gamelogic.events.PlayerMoveLeft;
import gamelogic.events.PlayerMoveRight;
import gamelogic.events.PlayerMoveUp;
import gamelogic.events.PlayerPickupEvent;
import gamelogic.events.ShootGunEvent;
import gamelogic.events.SpatialEvent;
import gamelogic.events.WarpMoveEvent;
import gamelogic.events.useTeleGunEvent;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.HarmfulTile;
import gamelogic.tiles.RenderRoomTile;





/**
 *
 * @author brownmax1
 *represents a room in the game which is filled with tiles and entities
 *many of the events that involve interaction between entities and the spatial state are resolved in this class.
 *the tiles are the "base" of the room and the entities are all of the kinds of things like walls and actors that can
 *exist in the room
 */

public class RoomState {

	private final int roomWidth;
	private final int roomHeight;

	private final GameRoomTile[][] tiles;// 2d array of the tiles in this room. [x][y]. first x is on the far left. first y is on the top.
	private GameEntity[][] entities; // 2d array of the items in this room. ordered in same way as tiles.

	private GameEntity[][] entitiesCache;// 2d array of Traversable entities that are currently being covered up by a MovingEntity (e.g. when a player steps onto a keycard, the pl

	private final int roomId; //the unique id number for this room

	private boolean isDark = false; //if a player is in a "dark" room, they can only see a small area around them. Unless they have night vision.

	private final String stringDescriptorOfRoom; //the textual description of this room that is printed in the hud when ap layer is in this room


	public RoomState(GameRoomTile[][] tiles, GameEntity[][] entities, int width, int height, int roomId,  String roomName) {
		this.tiles = tiles;
		this.entities = entities;
		this.roomWidth = width;
		this.roomHeight = height;
		this.roomId = roomId;
		this.stringDescriptorOfRoom = roomName;
		//create the entities cache array
		this.entitiesCache = new GameEntity[width][height];
		//fill the cache with nulls
		for(int i = 0; i < height ; i++){
			for(int j = 0; j < width; j ++){
				this.entitiesCache[j][i] = new NullEntity(CardinalDirection.NORTH);
			}
		}
	}

	// /ATTEMPT EVENTS ///

	/**
	 * attempt to cause an event on the game world by a certain actor. e.g.
	 * a player's attempt to move north.
	 *
	 * @param actor
	 *            the actor who is attempting to cause the action
	 * @param eventWeNeedToUpdateStateWith
	 *            the event that this actor is attempting to cause
	 * @return true if the event was applied successfully, else false.
	 */
	protected boolean attemptGameMapEventByPlayer(MovableEntity actor,SpatialEvent eventWeNeedToUpdateStateWith) {
		if (eventWeNeedToUpdateStateWith instanceof MovementEvent) {
			return attemptMovementEvent(actor,eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof PlayerPickupEvent){
			assert(actor instanceof Player): "this really isnt allowed atm and shouldnt happen atm (attempted to treat an ai as a player in the game logic)";
			Player actingPlayer = (Player)actor;
			return attemptPickupEvent(actingPlayer, (PlayerPickupEvent)eventWeNeedToUpdateStateWith);
		}else if (eventWeNeedToUpdateStateWith instanceof PlayerDropEvent){
			assert(actor instanceof Player): "this really isnt allowed atm and shouldnt happen atm (attempted to treat an ai as a player in the game logic)";
			Player actingPlayer = (Player)actor;
			return attemptDropEvent(actingPlayer, (PlayerDropEvent)eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof ShootGunEvent){
			assert(actor instanceof Player): "this really isnt allowed atm and shouldnt happen atm (attempted to treat an ai as a player in the game logic)";
			Player actingPlayer = (Player)actor;
			if(!actingPlayer.hasGun()){
				return false;
			}
			return attemptShootGunEvent(actingPlayer, (ShootGunEvent)eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof MeleeAttackEvent){
			return attemptMeleeEvent(actor, (MeleeAttackEvent)eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof useTeleGunEvent){
			assert(actor instanceof Player): "this really isnt allowed atm and shouldnt happen atm (attempted to treat an ai as a player in the game logic)";
			Player actingPlayer = (Player)actor;
			if(!actingPlayer.hasTeleGun()){
				return false;
			}
			return attemptTeleGunEvent(actingPlayer, (useTeleGunEvent)eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof PlayerHealEvent){
			assert(actor instanceof Player): "this really isnt allowed atm and shouldnt happen atm (attempted to treat an ai as a player in the game logic)";
			Player actingPlayer = (Player)actor;
			//can only heal self if they have at least one health kit
			if(actingPlayer.getHealthKitsAmount() < 0){
				return false;
			}
			return attemptHealEvent(actingPlayer, (PlayerHealEvent)eventWeNeedToUpdateStateWith);
		}
		else {
			throw new RuntimeException("that's not a valid event atm IT SHOULD BE IN THIS METHOD THAT WE CHECK IF THE PLAYER ACTUALLY HAS THE GUN ETC or has tele gun or watev:" + eventWeNeedToUpdateStateWith);
		}

	}





	/**
	 * attempt to melee attack an entity in the game
	 * @param actor the attacker
	 * @param eventWeNeedToUpdateStateWith the attack event
	 * @return true if the event was attempted
	 */
	private boolean attemptMeleeEvent(MovableEntity actor,MeleeAttackEvent eventWeNeedToUpdateStateWith) {

		//determine the offset of the entity that the MovableEntity is trying to attack
		GameEntity entityAttacked = null;
		switch(actor.getFacingCardinalDirection()){
			case EAST:
				entityAttacked = this.entities[actor.getxInRoom() + 1][actor.getyInRoom()];
				break;
			case NORTH:
				entityAttacked = this.entities[actor.getxInRoom()][actor.getyInRoom() - 1];
				break;
			case SOUTH:
				entityAttacked = this.entities[actor.getxInRoom()][actor.getyInRoom() + 1];
				break;
			case WEST:
				entityAttacked = this.entities[actor.getxInRoom() - 1][actor.getyInRoom()];
				break;
			default:
				break;

		}

		//if it's damageable, we can attack it
		if(entityAttacked instanceof Damageable){
			((Damageable) entityAttacked).takeDamage(eventWeNeedToUpdateStateWith.getHitDamage());
			return true;
		}

		//if not, we do nothing


		return true;
	}



	//CHECKS IF THERE IS AN ENEMY IN THE LINE OF ENTITIES THAT THE PLAYER IS FACING, IF THERE IS, DAMAGE THEM
	private boolean attemptShootGunEvent(Player actingPlayer,ShootGunEvent eventWeNeedToUpdateStateWith) {

		//DETERMINE WHAT THE X AND Y OFFSETS ARE FOR THE "BULLET" AT EACH STEP

		int xOffsetEachTime = 0; //the x offset that the bullet travels at
		int yOffsetEachTime = 0; // the y offset that the bullet travels at
		if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.NORTH){
			xOffsetEachTime = 0;
			yOffsetEachTime = -1;
		}else if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.EAST){
			xOffsetEachTime = 1;
			yOffsetEachTime = 0;
		}else if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.SOUTH){
			xOffsetEachTime = 0;
			yOffsetEachTime = 1;
		}else{//in case they facing west
			xOffsetEachTime = -1;
			yOffsetEachTime = 0;
		}
		int xCurrentCheck = actingPlayer.getxInRoom() + xOffsetEachTime;
		int yCurrentCheck = actingPlayer.getyInRoom() + yOffsetEachTime;

		//CHECK FOR A DAMAGEABLE ENTITY UNTIL THE BULLET HITS SOMETHING THAT IS NOT TRAVERSABLE OR SOMETHING THAT IS DAMAGEABLE THAT IT CAN HURT

		GameEntity entityWithBulletOnIt = this.entities[xCurrentCheck][yCurrentCheck]; //init for algorithm

		while(entityWithBulletOnIt instanceof Traversable || entityWithBulletOnIt instanceof Damageable){
			//if the entity is damageable, hurt it and return
			if(entityWithBulletOnIt instanceof Damageable){
				System.out.println("WE HIT THE WITH A BULLET: " + entityWithBulletOnIt);
				assert false:("asdfasd");
				((Damageable) entityWithBulletOnIt).takeDamage(eventWeNeedToUpdateStateWith.getShotDamage());
				return true;
			}
			//if the entity is not damageable, we need to check the next entity in the line, so increment our current check offsets
			xCurrentCheck += xOffsetEachTime;
			yCurrentCheck += yOffsetEachTime;
			entityWithBulletOnIt = this.entities[xCurrentCheck][yCurrentCheck];
		}

		//THE BULLET ENCOUNTERED SOMETHING THAT IS NOT TRAVERSABLE AND IS NOT DAMAGEABLE SO IT HIT A WALL OR SOMETHING SO OUR GUN SHOT FAILED
		//:)///throw new RuntimeException("the bullet hit something that is not traversable or damageable");
		return true;

	}

	/**
	 * used to attempt to move a player around this room
	 *
	 * @param actorteStateWith
	 *            the kind of move they are attempting
	 * @return bool true if th
	 *            the player attempting to move
	 * @param eventWeNeedToUpdae move was applied successfully, else false
	 */
	private boolean attemptMovementEvent(MovableEntity actor,
			SpatialEvent playerMove) {
		//TODO: morph the requested move event depending on current perspective. e.g.
		// if player chose up, but the current perspective treats east as up,
		// change the event to IDedPlayerMoveLeft
		//ONLY PRINT DEBUG IF IT IS A PLAYER ACTING OTHERWISE WILL PRINT OUT EVENT FOR EACH AI

		//WE NEED TO MODIFY THE DIRECTION THAT A PLAYER MOVED IN DEPENDING ON THEIR CURRENT ORIENTATION.
		//it is more intuitive that pressing right moves you right than pressing right moves you east.
		if(actor instanceof Player){
			/*System.out.println("so the player is at the following x and y in this room: " + actor.getxInRoom() + " " + actor.getyInRoom() + " and we are attempting to: " + playerMove);
			System.out.println("the room id of the room we are in is: " + this.roomId);
			System.out.println("now printing out a crude representation of the board");
			this.debugDraw();*/
			//random debug shit ^^^
			//PUT THIS GROSS SHIT IN A METHOD AT THE BOTTOM
			Player playerActor = (Player)actor;
			if(playerActor.getDirectionThatIsUp() == CardinalDirection.NORTH){
				//this is the norm, so do nothing to modify direction taken
				if(playerMove instanceof PlayerMoveUp){
					playerActor.setFacingCardinalDirection(CardinalDirection.NORTH);
				}else if(playerMove instanceof PlayerMoveRight){
					playerActor.setFacingCardinalDirection(CardinalDirection.EAST);
				}else if(playerMove instanceof PlayerMoveDown){
					playerActor.setFacingCardinalDirection(CardinalDirection.SOUTH);
				}else if(playerMove instanceof PlayerMoveLeft){
					playerActor.setFacingCardinalDirection(CardinalDirection.WEST);
				}
			}else if(playerActor.getDirectionThatIsUp() == CardinalDirection.WEST){

				if(playerMove instanceof PlayerMoveUp){
					playerMove = new PlayerMoveRight(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.NORTH);
				}else if(playerMove instanceof PlayerMoveRight){
					playerMove = new PlayerMoveDown(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.EAST);
				}else if(playerMove instanceof PlayerMoveDown){
					playerMove = new PlayerMoveLeft(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.SOUTH);
				}else if(playerMove instanceof PlayerMoveLeft){
					playerMove = new PlayerMoveUp(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.WEST);
				}
			}else if(playerActor.getDirectionThatIsUp() == CardinalDirection.SOUTH){
				if(playerMove instanceof PlayerMoveUp){
					playerMove = new PlayerMoveDown(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.NORTH);
				}else if(playerMove instanceof PlayerMoveRight){
					playerMove = new PlayerMoveLeft(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.EAST);
				}else if(playerMove instanceof PlayerMoveDown){
					playerMove = new PlayerMoveUp(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.SOUTH);
				}else if(playerMove instanceof PlayerMoveLeft){
					playerMove = new PlayerMoveRight(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.WEST);
				}
			}else if (playerActor.getDirectionThatIsUp() == CardinalDirection.EAST){//in case that the orientation is West Is Up
				if(playerMove instanceof PlayerMoveUp){
					playerMove = new PlayerMoveLeft(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.NORTH);
				}else if(playerMove instanceof PlayerMoveRight){
					playerMove = new PlayerMoveUp(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.EAST);
				}else if(playerMove instanceof PlayerMoveDown){
					playerMove = new PlayerMoveRight(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.SOUTH);
				}else if(playerMove instanceof PlayerMoveLeft){
					playerMove = new PlayerMoveDown(roomHeight);
					playerActor.setFacingCardinalDirection(CardinalDirection.WEST);
				}
			}
		}



		//we moved the player, so set their direction faced //TODO: should prob put this in a helper method !!! esp cause this will depend on direction faced/current orientation etc.


		//CALCULATE MOVEMENT OFFSETS FOR THE ORDINARY MOVES (and set direction faced)

		if (playerMove instanceof PlayerMoveUp) {
			if(actor instanceof Player){
				Player player = (Player)actor;
				//player.setFacingCardinalDirection(CardinalDirection.NORTH);
			}

			return attemptRoomMove(actor, -1, 0);
		} else if (playerMove instanceof PlayerMoveLeft) {
			if(actor instanceof Player){
				Player player = (Player)actor;
				//player.setFacingCardinalDirection(CardinalDirection.WEST);
			}
			return attemptRoomMove(actor, 0, -1);
		} else if (playerMove instanceof PlayerMoveRight) {
			if(actor instanceof Player){
				Player player = (Player)actor;
			//	player.setFacingCardinalDirection(CardinalDirection.EAST);
			}
			return attemptRoomMove(actor, 0, 1);
		} else if (playerMove instanceof PlayerMoveDown) {
			if(actor instanceof Player){
				Player player = (Player)actor;
				//player.setFacingCardinalDirection(CardinalDirection.SOUTH);
			}
			return attemptRoomMove(actor, 1, 0); //TODO: maybe declare these as constrans (seems uncesseasttrssrdsffrry tho)
		}
		//CALCULATE MOVEMENT OFFSETS FOR THE WARP MOVES

		else if(playerMove instanceof WarpMoveEvent){

			assert(actor instanceof Player):"NO only players can warp atm";
			Player warpPlayer = (Player)actor;
			WarpMoveEvent warpEvent = (WarpMoveEvent)playerMove;

			//calculate warp offset based on current faced direction
			if(warpPlayer.getFacingCardinalDirection() == CardinalDirection.NORTH){
				return attemptRoomMove(actor, -1 * warpEvent.getWarpDistance(), 0);
			}else if (warpPlayer.getFacingCardinalDirection() == CardinalDirection.EAST){
				return attemptRoomMove(actor, 0, 1 * warpEvent.getWarpDistance());
			}else if (warpPlayer.getFacingCardinalDirection() == CardinalDirection.SOUTH){
				return attemptRoomMove(actor, 1 * warpEvent.getWarpDistance(), 0);
			}else{//in the case that the player is facing west
				return attemptRoomMove(actor, 0, -1 * warpEvent.getWarpDistance());
			}
			//just use same helper method as standard move but with greater offset
		}


		else {
			throw new RuntimeException(
					"this is not a valid move event at the moment: " + playerMove);
		}


	}

	/**
	 * used to take an entity from its current location to a new location on the board
	 * @param actingEntity the entity that is moving
	 * @param yOffset how far it is moving up / down
	 * @param xOffset how far it is moving left / right
	 * @return true if the entity moved, false if it didnt
	 */
	private boolean attemptRoomMove(MovableEntity actingEntity, int yOffset, int xOffset){

				//check that we are not moving out of bounds
				if(!((actingEntity.getxInRoom() + xOffset < this.entities.length && actingEntity.getxInRoom() + xOffset >= 0)&&(actingEntity.getyInRoom() + yOffset < this.entities[0].length && actingEntity.getyInRoom() + yOffset >= 0))){
					return false;
				}

				//check that the square that we are moving to is a traversable and that there is no other entity in that position
				if(this.tiles[actingEntity.getxInRoom() + xOffset][actingEntity.getyInRoom() + yOffset] instanceof Traversable &&
						this.entities[actingEntity.getxInRoom() + xOffset][actingEntity.getyInRoom() + yOffset] instanceof Traversable){


					//place the entity that we are moving into in the cache so that it can be replaced when we move off it
					this.entitiesCache[actingEntity.getxInRoom() + xOffset][actingEntity.getyInRoom() + yOffset] = this.entities[actingEntity.getxInRoom() + xOffset][actingEntity.getyInRoom() + yOffset];


					//we are moving out of a position so fill that position in the entities array with the cached entity at same location (put the thing we are standing on back)
					this.entities[actingEntity.getxInRoom()][actingEntity.getyInRoom()] = this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()];

					//update the player's entity position in the array
					this.entities[actingEntity.getxInRoom() + xOffset][actingEntity.getyInRoom() + yOffset] =  actingEntity;

					//update the player's internal x and y coordinates
					actingEntity.setyInRoom(actingEntity.getyInRoom() + yOffset);
					actingEntity.setxInRoom(actingEntity.getxInRoom() + xOffset);




					//WE ARE NOT DONE MOVING YET, WE NEED TO SEE IF WE HAVE TELEPORTED


					if(this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()] instanceof Teleporter ){//if they are standing on a tele
						int oldX = actingEntity.getxInRoom();
						int oldY = actingEntity.getyInRoom(); //used so that we know where the teleporter is that we need to put back into entities (if we teleport the player, their internal x and y will change so cant use that)
						Teleporter theTele = (Teleporter) this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()];
						//if we move player successfully, clean up afterwards (remove their old instance on the board)
						if(theTele.teleportEntity(actingEntity)){
							this.entities[oldX][oldY] = this.entitiesCache[oldX][oldY];
							return true;
						}
					}

					//CHECK IF WE SHOULD PORTAL

					if(this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()] instanceof Portal ){
						int oldX = actingEntity.getxInRoom();
						int oldY = actingEntity.getyInRoom(); //used so when we move the player, we can put the teleporter back here
						Portal thePortal = (Portal) this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()];
						//if we move player successfully, clean up afterwards (remove their old instance on the board)
						if(thePortal.attemptToTakePortal(actingEntity.getxInRoom(), actingEntity.getyInRoom(), this, actingEntity)){
							this.entities[oldX][oldY] = this.entitiesCache[oldX][oldY];
							return true;
						}else{
							//:)throw new RuntimeException("cannot take portal there prob something in the way of dest");//TODO: handle differently in final release
							return false;
						}
					}

					//CHECK IF PICKED UP COIN
					if(actingEntity instanceof Player){
						if(this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()] instanceof Coin){
							//give the player a coin
							((Player)actingEntity).addCoin();
							//remove the coin from the entitiesCache (it was taken)
							this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);
							System.out.println("picked up a coin!");
						}
					}


					//we moved the player so we return true
					return true;

				}else{//in the case that we cannot move to the desired tile
					return false;
					}
	}


	/**
	 * attempts to pickup an item from the board and place it in the player's current inventory
	 * @param actingPlayer the player who is attempting to move
	 * @param eventWeNeedToUpdateStateWith the movement event
	 * @return true if the item was successfully picked up and put in the inventory, else false
	 */
	private boolean attemptPickupEvent(Player actingPlayer,PlayerPickupEvent eventWeNeedToUpdateStateWith) {
		if(this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] instanceof Carryable){
			//attempt put in inventory (cant if at capacity)
			if(actingPlayer.getCurrentInventory().pickUpItem((Carryable) this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()])){
				//remove from entitiy cache because player picked it up
				this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}

	}


/**
 * attempts to drop an item from the inventory into the room
 * @param actingPlayer the player who is attempring to drop
 * @param eventWeNeedToUpdateStateWith the drop event
 * @return true if the item was dropped into the game world, else false
 */
	private boolean attemptDropEvent(Player actingPlayer,
			PlayerDropEvent eventWeNeedToUpdateStateWith) {

		//if this position in cached entitities is empty, we can drop
		if(this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] instanceof NullEntity){
			//set this position in cache to dropped item
			this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = actingPlayer.getCurrentInventory().dropItem();
			//if the player dropped the treasure, and this room is the treasure room, we should delete the treasure because the player has claimed the bounty
			if(this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] instanceof Treasure && this.roomId == 6){
				this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);
			}
			return true;
		}else{
			return false;
		}
	}

	/**
	 * attempts to place a portal on top of the tile that the player is facing
	 * @param actingPlayer the player attempting to place the portal
	 * @param eventWeNeedToUpdateStateWith the portal placing event
	 * @return true if the portal was placed, else false (e.g. if there is already an entity in that place)
	 */
	private boolean attemptTeleGunEvent(Player actingPlayer, useTeleGunEvent eventWeNeedToUpdateStateWith) {

		//attempt to place a portal gate in the room
		if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.NORTH){
			if(this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() - 1] instanceof NullEntity){
				//place portal in array
				this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() - 1] = eventWeNeedToUpdateStateWith.getMyPortal();
				//update portal internal gate fields to this location
				eventWeNeedToUpdateStateWith.getMyPortal().createANewGate(actingPlayer.getxInRoom(), actingPlayer.getyInRoom() - 1, this);
				//we created the new gate so return true
				return true;
			}else{
				return false;
			}
		}else if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.EAST){
			if(this.entities[actingPlayer.getxInRoom() + 1][actingPlayer.getyInRoom()] instanceof NullEntity){

				//place portal in array
				this.entities[actingPlayer.getxInRoom() + 1][actingPlayer.getyInRoom()] = eventWeNeedToUpdateStateWith.getMyPortal();

				//update portal internal gate fields to this location
				eventWeNeedToUpdateStateWith.getMyPortal().createANewGate(actingPlayer.getxInRoom() + 1, actingPlayer.getyInRoom(), this);

				//we created the new gate so return true
				return true;
			}else{//in the case that there is something in the way of where we want to place aportal

				return false;
			}
		}else if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.SOUTH){

			if(this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() + 1] instanceof NullEntity){

				//place portal in array
				this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() + 1] = eventWeNeedToUpdateStateWith.getMyPortal();

				//update portal internal gate fields to this location

				eventWeNeedToUpdateStateWith.getMyPortal().createANewGate(actingPlayer.getxInRoom(), actingPlayer.getyInRoom() + 1, this);

				//we created the new gate so return true
				return true;
			}else{//in the case that there is something in the way of where we want to place aportal

				return false;
			}
		}else{//in case player facing west
			if(this.entities[actingPlayer.getxInRoom() - 1][actingPlayer.getyInRoom()] instanceof NullEntity){
				//place portal in array
				this.entities[actingPlayer.getxInRoom() - 1][actingPlayer.getyInRoom()] = eventWeNeedToUpdateStateWith.getMyPortal();
				//update portal internal gate fields to this location
				eventWeNeedToUpdateStateWith.getMyPortal().createANewGate(actingPlayer.getxInRoom() - 1, actingPlayer.getyInRoom(), this);
				//we created the new gate so return true
				return true;
			}else{//in the case that there is something in the way of where we want to place aportal
				return false;
			}
		}
	}
	/**
	 * attempts to use a health kit to give the user more health
	 * @param actingPlayer the player attempting to use the health kit
	 * @param eventWeNeedToUpdateStateWith the heal event
	 * @return true if the player was healed, false if not
	 */
	private boolean attemptHealEvent(Player actingPlayer,PlayerHealEvent eventWeNeedToUpdateStateWith) {

		//HEAL THE PLAYER
		return actingPlayer.useHealthKit();
	}



	/**
	 * removes the GameEntity at a specified position in this room by replacing it
	 * with a NullEntity.
	 * @param x the x position that we are removing an entity from
	 * @param y the y position that we are removing an entity from
	 */
	public void removeRedundantGameEntity(int x, int y){
		System.out.println("REMOVING:" + this.entities[x][y]);
		if(this.entities[x][y] instanceof Portal || this.entities[x][y] instanceof IndependentActor){
			this.entities[x][y] = new NullEntity(CardinalDirection.NORTH);
			return;
		}else if(this.entitiesCache[x][y] instanceof Portal|| this.entities[x][y] instanceof IndependentActor){ //in the case that we are removing as the player goes through the portal and the player is standing on the gate so it is in the cache
			this.entitiesCache[x][y] = new NullEntity(CardinalDirection.NORTH);
			return;
		}

	}






	/**
	 * attempts to place a supplied MovableEntity (e.g. an NPC or Player) at the specified position
	 * in this room. We can only place the MovableEntity in a position in the entities array that is currently
	 * occupied by a NullEntity
	 * @param entToMove the MovableEntity that we are placing
	 * @param destinationx the x position that we are adding an entity to
	 * @param destinationy the y position that we are adding an entity to
	 * @return boolean true if the entity was placed successfully in the specified position, else false
	 */
	public boolean attemptToPlaceEntityInRoom(MovableEntity entToMove, int destinationx, int destinationy) {
		//if the teleporter receiver tile has entities on it, we cannot teleport
		if(this.entities[destinationx][destinationy] instanceof Traversable){
			//place the traversable entity at the destination in the cache
			this.entitiesCache[destinationx][destinationy] = this.entities[destinationx][destinationy];
			//update the 2d array
			this.entities[destinationx][destinationy] = entToMove;
			//update the player's internal x/y
			entToMove.setCurrentRoom(this);
			entToMove.setxInRoom(destinationx);
			entToMove.setyInRoom(destinationy);
			return true;
		}
		return false;
	}

	/// ADD TILES TO THE ROOM ///

/**
 * used to create a one way teleporter in this room to another room.
 * @param myX the x that the teleporter entrance is at in this room
 * @param myY the y that the teleporter entrance is at in this room
 * @param targetX the x that this teleporter leads to in the target room
 * @param targetY the y that this teleporter leads to in the target room
 * @param targetRoom the room that this teleporter leads to
 */
	public void spawnStandardTeleporter(CardinalDirection directionFaced, int myX, int myY, int targetX, int targetY, RoomState targetRoom) {
		this.entities[myX][myY] = new StandardTeleporter(myX, myY, directionFaced, targetX, targetY, targetRoom);

	}
	/**
	 * used to create a one way LOCKED teleporter in this room to another room. The teleporter
	 * will only transport you if you have the key card somewhere in your inventory
	 * @param myX the x that the teleporter entrance is at in this room
	 * @param myY the y that the teleporter entrance is at in this room
	 * @param targetX the x that this teleporter leads to in the target room
	 * @param targetY the y that this teleporter leads to in the target room
	 * @param targetRoom the room that this teleporter leads to
	 */
	public void spawnLockedTeleporter(CardinalDirection directionFaced, int myX, int myY, int targetX, int targetY, RoomState targetRoom) {
			this.entities[myX][myY] = new LockedTeleporter(directionFaced, targetX, targetY, targetRoom);

		}





		/**
		 * sets the room as day or night.
		 * @param isDark whether the room should be set to dark or light
		 */
		public void setDark(boolean isDark) {
			this.isDark = isDark;
		}

		public boolean isDark() {
			return isDark;
		}


		/**
		 * checks whether a movable entity is facing into an entity in the next position in the entities array that cannot be moved into or attacked.
		 * useful helper method for the deliberately stupid pylon attacker npc
		 * @param actor checking whether this actor is "stuck"
		 * @return true if entity they are moving into is not traversable or damageable, else false
		 */
		public boolean pylonAttackerStuck(MovableEntity actor) {

			//determine the offset of the entity that the MovableEntity is trying to move/attack into

			GameEntity entityMovingInto = null;

			switch(actor.getFacingCardinalDirection()){
				case EAST:
					entityMovingInto = this.entities[actor.getxInRoom() + 1][actor.getyInRoom()];
					break;
				case NORTH:
					entityMovingInto = this.entities[actor.getxInRoom()][actor.getyInRoom() - 1];
					break;
				case SOUTH:
					entityMovingInto = this.entities[actor.getxInRoom()][actor.getyInRoom() + 1];
					break;
				case WEST:
					entityMovingInto = this.entities[actor.getxInRoom() - 1][actor.getyInRoom()];
					break;
				default:
					break;

			}
			//if it's  traversable or damageable, we are  not stuck
			if(!(entityMovingInto instanceof Traversable || entityMovingInto instanceof Damageable)){
				System.out.println("stuck on : at :" + entityMovingInto + " ");
				return true;
			}

			return false;
		}






	///UTILITY///

/**
 * performs a deep translation/copy of the tiles in this room into a new array and then returns it.
 * Note that the objects held in the tile 2d array in this object are RoomTiles and we are returning DrawableRoomTiles
 * this distinction is useful because :
 * 1) we don't want to pass a mutable array crucial to gamestate around the program
 * 2) it lets us distinguish between game state elements and markers that indicate to the renderer what to draw
 * @return the 2d array of drawable tiles
 */
	protected RenderRoomTile[][] generateDrawableTiles() {
		//create the array to house the "copy"
		RenderRoomTile copiedTiles[][] = new RenderRoomTile[this.roomWidth][this.roomHeight];
		//copy everything across
		for(int i = 0; i < this.tiles.length ; i ++){
			for(int j = 0; j < this.tiles[i].length; j ++){
				copiedTiles[i][j] = this.tiles[i][j].generateDrawableCopy();
			}
		}



		//return this copied array, ready to be given to the renderer to draw some tiles
		return copiedTiles;
	}


	/**
	 * performs a deep translation/copy of the entities in this room into a new array and then returns it.
	 * Note that the objects held in the entitities 2d array in this object are GameEntities and we are returning DrawableGameEntities
	  * this distinction is useful because :
	  * 1) we don't want to pass a mutable array crucial to gamestate around the program
	  * 2) it lets us distinguish between game state elements and markers that indicate to the renderer what to draw
	 * @return the 2d array of drawable entities
	 */
	protected RenderEntity[][] generateDrawableEntities() {

		//create the array to house the "copy"
		RenderEntity copiedEntities[][] = new RenderEntity[this.roomWidth][this.roomHeight];
		//copy everything across
		for(int i = 0; i < this.entities.length ; i ++){
			for(int j = 0; j < this.entities[i].length; j ++){
				copiedEntities[i][j] = this.entities[i][j].generateDrawableCopy();
			}
		}




		//return this copied 2d array, ready to be given to the renderer to draw some entities
		return copiedEntities;
	}



	/**
	 * NOTE that this version only copies entities that are within a 3 tile radius of the player to simulate darkness
	 * performs a deep translation/copy of the entities in this room into a new array and then returns it.
	 * Note that the objects held in the entitities 2d array in this object are GameEntities and we are returning DrawableGameEntities
	  * this distinction is useful because :
	  * 1) we don't want to pass a mutable array crucial to gamestate around the program
	  * 2) it lets us distinguish between game state elements and markers that indicate to the renderer what to draw
	 * @param int playerY the y position of the player we are generating a drawable room for
	 * @param int playerX the x position of the player we are generating a drawable room for
	 * @param bool nightVision true when the player we are drawing the room for has nightvision equipped, else false
	 * @return the 2d array of drawable tiles
	 */
	protected RenderEntity[][] generateDrawableEntitiesDarkRoom(int playerX, int playerY, boolean nightVision) {

		int seeDistance = 2;

		if(nightVision){
			seeDistance = 6;
		}


		//create the array to house the "copy"
		RenderEntity copiedEntities[][] = new RenderEntity[this.roomWidth][this.roomHeight];
		//copy everything across
		for(int i = 0; i < this.entities.length ; i ++){
			for(int j = 0; j < this.entities[i].length; j ++){
				if(Math.abs(playerX - i) > seeDistance || Math.abs(playerY - j) > seeDistance){
					copiedEntities[i][j] = new RenderNullEntity(CardinalDirection.NORTH);
				}else{
					copiedEntities[i][j] = this.entities[i][j].generateDrawableCopy();
				}
			}
		}




		//return this copied 2d array, ready to be given to the renderer to draw some entities
		return copiedEntities;
	}







	/**
	 * draws simple room.used for debug
	 * useful for seeing the "true" state of the game when adding new entitities when you cannot rely on the rendering
	 *//*
		public void debugDraw() {
			/// DEBUG DRAWING THAT DRAWS THE ROOMSTATE BEFORE EVERY ACTION ATTEMPTED BY A PLAYER ///


			for(int i = 0; i < this.roomHeight ; i ++){
				for(int j = 0; j < this.roomWidth ; j ++){


					 if(this.entities[j][i] instanceof NullEntity){
						System.out.print("n  ");
					}else if(this.entities[j][i] instanceof MazeWall){
						System.out.print("i  ");
					}else if(this.entities[j][i] instanceof OuterWall){
						System.out.print("x  ");
					}else if (this.entities[j][i] instanceof Player){
						System.out.print("p  ");
					}else if(this.entities[j][i] instanceof KeyCard){
						System.out.print("k  ");
					}else if(this.entities[j][i] instanceof IndependentActor){
						System.out.print("Z  ");
					}else if(this.entities[j][i] instanceof NightVisionGoggles){
						System.out.print("NV ");
					}else if(this.entities[j][i] instanceof Coin){
						System.out.print("$  ");
					}else if(this.entities[j][i] instanceof StandardTeleporter){
						System.out.print("D  ");
					}else if(this.entities[j][i] instanceof MediumCarrier){
						System.out.print("MC ");
					}else if(this.entities[j][i] instanceof SmallCarrier){
						System.out.print("SC ");
					}else if(this.entities[j][i] instanceof Pylon){
						System.out.print("^  ");
					}else if(this.entities[j][i] instanceof LockedTeleporter){
						System.out.print("Q  ");
					}else if(this.entities[j][i] instanceof Gun){
						System.out.print("G  ");
					}else if(this.entities[j][i] instanceof TeleporterGun){
						System.out.print("T  ");
					}else if(this.entities[j][i] instanceof Portal){
						System.out.print("0  ");
					}else if(this.entities[j][i] instanceof HealthKit){
						System.out.print("<3 ");
					}else if(this.entities[j][i] instanceof Treasure){
						System.out.print("@  ");
					}
					else{

						throw new RuntimeException("some kind of unrecogniesd entity was attempted to drawraw. you prob added an entity to the game and forgot to add it here");
					}
					 //PRINT A HARMFUL TILE OVERTOP OF ANY ENTITIES
					if(this.tiles[j][i] instanceof HarmfulTile){
						System.out.print("oww");
					}


				}

			System.out.println("\n");
			}





		}*/


	public int getId() {
		return this.roomId;
	}

	public int getRoomWidth() {
		return roomWidth;
	}

	public int getRoomHeight() {
		return roomHeight;
	}

	public GameRoomTile[][] getTiles() {
		return tiles;
	}


	public GameEntity[][] getEntities() {
		return entities;
	}

/**
 * creates a graph from this room to be used for pathfinding
 * @return
 */
	public RoomMovementGraph generateMovementGraph() {
		return new RoomMovementGraph(this.entities);
	}





	//JOSH MADE THESE
	public String getDescription(){
		return this.stringDescriptorOfRoom;
	}


	public RoomState(GameRoomTile[][] tiles, int width, int height, int roomId, boolean isDark, String roomName){
		this.tiles = tiles;
		this.entities = new GameEntity[width][height];
		this.roomWidth = width;
		this.roomHeight = height;
		this.roomId = roomId;
		this.setDark(isDark);
		this.stringDescriptorOfRoom = roomName;
		//create the entities cache array
		this.entitiesCache = new GameEntity[width][height];
		//fill the cache with nulls
		for(int i = 0; i < height ; i++){
			for(int j = 0; j < width; j ++){
				this.entitiesCache[j][i] = new NullEntity(CardinalDirection.NORTH);
			}
		}
	}

	public void setEntities(GameEntity[][] entities){
		this.entities = entities;
	}





}
