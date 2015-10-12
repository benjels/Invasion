package gamelogic;

import java.util.HashMap;

import gamelogic.entities.Carryable;
import gamelogic.entities.Coin;
import gamelogic.entities.Damageable;
import gamelogic.entities.GameEntity;
import gamelogic.entities.Gun;
import gamelogic.entities.MazeWall;
import gamelogic.entities.IndependentActor;
import gamelogic.entities.KeyCard;
import gamelogic.entities.LockedTeleporter;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.NightVisionGoggles;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Player;
import gamelogic.entities.Portal;
import gamelogic.entities.Pylon;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderNullEntity;
import gamelogic.entities.SmallCarrier;
import gamelogic.entities.Teleporter;
import gamelogic.entities.TeleporterGun;
import gamelogic.events.MeleeAttackEvent;
import gamelogic.events.MovementEvent;
import gamelogic.events.PlayerDropEvent;
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
 *
 *
 *         a room in the game state with entities/tiles in it. NOTE: this is
 *         different to the DrawableRoomState which is the object that is sent
 *         back to the clients to be drawn on each tick.
 *
 *         Note that these rooms will be linked together by references to other
 *         rooms in the Door object. THere is no central collection of these
 *         rooms.
 */

public class RoomState {

	private final int roomWidth;
	private final int roomHeight;

	private final GameRoomTile[][] tiles;// 2d array of the tiles in this room. [x][y]. first x is on the far left. first y is on the top.
	private final GameEntity[][] entities; // 2d array of the items in this room. ordered in same way as tiles.

	private GameEntity[][] entitiesCache;// 2d array of Traversable entities that are currently being covered up by a MovingEntity (e.g. when a player steps onto a keycard, the pl
	//will then be occupying that location in the entities array, so put the key card here. It is from this array that items are "picked up" by players.

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
	 * a player's attempt to move north. Or a zombie's attack down or watev
	 *
	 * @param actor
	 *            the actor who is attempting to cause the action
	 * @param eventWeNeedToUpdateStateWith
	 *            the event that this actor is attempting to cause
	 * @return true if the event was applied successfully, else false.
	 */
	protected boolean attemptGameMapEventByPlayer(MovableEntity actor,SpatialEvent eventWeNeedToUpdateStateWith) {
		// determine which kind of map event this is
		if (eventWeNeedToUpdateStateWith instanceof MovementEvent) {
			return attemptMovementEvent(actor,eventWeNeedToUpdateStateWith);
		} else if(eventWeNeedToUpdateStateWith instanceof PlayerPickupEvent){
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
				throw new RuntimeException("cant shoot the gun if you didnt pick it up");
			}
			return attemptShootGunEvent(actingPlayer, (ShootGunEvent)eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof MeleeAttackEvent){
			return attemptMeleeEvent(actor, (MeleeAttackEvent)eventWeNeedToUpdateStateWith);
		}else if(eventWeNeedToUpdateStateWith instanceof useTeleGunEvent){
			assert(actor instanceof Player): "this really isnt allowed atm and shouldnt happen atm (attempted to treat an ai as a player in the game logic)";
			Player actingPlayer = (Player)actor;
			if(!actingPlayer.hasTeleGun()){
				throw new RuntimeException("cant shoot the telegun if you didnt pick it up");
			}
			return attemptTeleGunEvent(actingPlayer, (useTeleGunEvent)eventWeNeedToUpdateStateWith);
		}
		else {
			throw new RuntimeException("that's not a valid event atm IT SHOULD BE IN THIS METHOD THAT WE CHECK IF THE PLAYER ACTUALLY HAS THE GUN ETC or has tele gun or watev:" + eventWeNeedToUpdateStateWith);
		}

	}

	
	//NOTE THAT THESE METHODS SHOULD PROB RETURN FALSE IF THERE IS SOME KINF OF FAILURE. NOT JUST WHEN THE PLAYER MISSES WITH GUN ETC
	//actually not sure kinda depends on how we wanna use them
	
	
	
	//ATTEMPTS TO HIT SOMEONE (this can be combined with the check stuck method)
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
		
		//return false;
		return true; //is attempting to attack but not attacking true? idk 
	}

	//ATTEMPTS TO PLACE A PORTAL ON TOP OF THE TILE THAT IS IN FRONT OF THE PLAYER
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
				throw new RuntimeException("cannot place tele theres something in the way");
			}
		}else if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.EAST){
			if(this.entities[actingPlayer.getxInRoom() + 1][actingPlayer.getyInRoom()] instanceof NullEntity){
				//place portal in array
				this.entities[actingPlayer.getxInRoom() + 1][actingPlayer.getyInRoom()] = eventWeNeedToUpdateStateWith.getMyPortal();
				//update portal internal gate fields to this location
				eventWeNeedToUpdateStateWith.getMyPortal().createANewGate(actingPlayer.getxInRoom() + 1, actingPlayer.getyInRoom(), this);
				//we created the new gate so return true
				return true;
			}else{
				throw new RuntimeException("cannot place tele theres something in the way");
			}
		}else if(actingPlayer.getFacingCardinalDirection() == CardinalDirection.SOUTH){
			if(this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() + 1] instanceof NullEntity){
				//place portal in array
				this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() + 1] = eventWeNeedToUpdateStateWith.getMyPortal();
				//update portal internal gate fields to this location
				eventWeNeedToUpdateStateWith.getMyPortal().createANewGate(actingPlayer.getxInRoom(), actingPlayer.getyInRoom() + 1, this);
				//we created the new gate so return true
				return true;
			}else{
				throw new RuntimeException("cannot place tele theres something in the way");
			}
		}else{//in case player facing west
			if(this.entities[actingPlayer.getxInRoom() - 1][actingPlayer.getyInRoom()] instanceof NullEntity){
				//place portal in array
				this.entities[actingPlayer.getxInRoom() - 1][actingPlayer.getyInRoom()] = eventWeNeedToUpdateStateWith.getMyPortal();
				//update portal internal gate fields to this location
				eventWeNeedToUpdateStateWith.getMyPortal().createANewGate(actingPlayer.getxInRoom() - 1, actingPlayer.getyInRoom(), this);
				//we created the new gate so return true
				return true;
			}else{
				throw new RuntimeException("cannot place tele theres something in the way");
			}
		}
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
				((Damageable) entityWithBulletOnIt).takeDamage(eventWeNeedToUpdateStateWith.getShotDamage());
				return true;
			}
			//if the entity is not damageable, we need to check the next entity in the line, so increment our current check offsets
			xCurrentCheck += xOffsetEachTime;
			yCurrentCheck += yOffsetEachTime;
			entityWithBulletOnIt = this.entities[xCurrentCheck][yCurrentCheck];
		}
		
		//THE BULLET ENCOUNTERED SOMETHING THAT IS NOT TRAVERSABLE AND IS NOT DAMAGEABLE SO IT HIT A WALL OR SOMETHING SO OUR GUN SHOT FAILED
		throw new RuntimeException("the bullet hit something that is not traversable or damageable");
		//return false; or maybe return true afterall, missing is still shooting
		
	}

	/**
	 * used to attempt to move a player around this room
	 *
	 * @param actor
	 *            the player attempting to move
	 * @param eventWeNeedToUpdateStateWith
	 *            the kind of move they are attempting
	 * @return bool true if the move was applied successfully, else false
	 */
	private boolean attemptMovementEvent(MovableEntity actor,
			SpatialEvent playerMove) {
		//TODO: morph the requested move event depending on current perspective. e.g.
		// if player chose up, but the current perspective treats east as up,
		// change the event to IDedPlayerMoveLeft
		//ONLY PRINT DEBUG IF IT IS A PLAYER ACTING OTHERWISE WILL PRINT OUT EVENT FOR EACH AI
		if(actor instanceof Player){
			System.out.println("so the player is at the following x and y in this room: " + actor.getxInRoom() + " " + actor.getyInRoom() + " and we are attempting to: " + playerMove);
			System.out.println("the room id of the room we are in is: " + this.roomId);
			System.out.println("now printing out a crude representation of the board");
			this.debugDraw();
			//random debug shit

		}


		//we moved the player, so set their direction faced //TODO: should prob put this in a helper method !!! esp cause this will depend on direction faced/current orientation etc.
		

		//CALCULATE MOVEMENT OFFSETS FOR THE ORDINARY MOVES (and set direction faced)

		if (playerMove instanceof PlayerMoveUp) {
			if(actor instanceof Player){
				Player player = (Player)actor;
				player.setFacingCardinalDirection(CardinalDirection.NORTH);
			}
			
			return attemptOneSquareMove(actor, -1, 0);
		} else if (playerMove instanceof PlayerMoveLeft) {
			if(actor instanceof Player){
				Player player = (Player)actor;
				player.setFacingCardinalDirection(CardinalDirection.WEST);
			}
			return attemptOneSquareMove(actor, 0, -1);
		} else if (playerMove instanceof PlayerMoveRight) {
			if(actor instanceof Player){
				Player player = (Player)actor;
				player.setFacingCardinalDirection(CardinalDirection.EAST);
			}
			return attemptOneSquareMove(actor, 0, 1);
		} else if (playerMove instanceof PlayerMoveDown) {
			if(actor instanceof Player){
				Player player = (Player)actor;
				player.setFacingCardinalDirection(CardinalDirection.SOUTH);
			}
			return attemptOneSquareMove(actor, 1, 0); //TODO: maybe declare these as constrans (seems uncesseasttrssrdsffrry tho)
		} 
		//CALCULATE MOVEMENT OFFSETS FOR THE WARP MOVES
		
		else if(playerMove instanceof WarpMoveEvent){
			
			assert(actor instanceof Player):"NO only players can warp atm";
			Player warpPlayer = (Player)actor;
			WarpMoveEvent warpEvent = (WarpMoveEvent)playerMove;
		
			//calculate warp offset based on current faced direction
			if(warpPlayer.getFacingCardinalDirection() == CardinalDirection.NORTH){
				return attemptOneSquareMove(actor, -1 * warpEvent.getWarpDistance(), 0);
			}else if (warpPlayer.getFacingCardinalDirection() == CardinalDirection.EAST){
				return attemptOneSquareMove(actor, 0, 1 * warpEvent.getWarpDistance());
			}else if (warpPlayer.getFacingCardinalDirection() == CardinalDirection.SOUTH){
				return attemptOneSquareMove(actor, 1 * warpEvent.getWarpDistance(), 0);
			}else{//in the case that the player is facing west
				return attemptOneSquareMove(actor, 0, -1 * warpEvent.getWarpDistance());
			}
			//just use same helper method as standard move but with greater offset
		}
		
		
		else {
			throw new RuntimeException(
					"this is not a valid move event at the moment: " + playerMove);
		}


	}

	//USING THIS TO CONSOLIDATE ALL OF THE FOUR MOVE DIRECTIONS METHODS (CAN ALSO BE USED TO EASILY SUPPORT DIAGONAL MOVES) .e.g. up/right is just -1, 1 offsets.
	private boolean attemptOneSquareMove(MovableEntity actingEntity, int yOffset, int xOffset){

				//CHECK FOR OUT OF BOUNDS MOVE (SANITY CHECK)
				if((actingEntity.getxInRoom() + xOffset >= this.roomWidth ||actingEntity.getxInRoom() + xOffset <= 0) || (actingEntity.getyInRoom() + yOffset >= this.roomHeight||actingEntity.getyInRoom() + yOffset <= 0)){
					throw new RuntimeException("definitely cannot move out of bounds of the tile arrays!!!");
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


					///DEBUG SHIT
					if(actingEntity instanceof Player){
						System.out.println("HAVING ATTEMPTED THE MOVE...");

						System.out.println("so the player is at the following x and y in this room: " + actingEntity.getxInRoom() + " " + actingEntity.getyInRoom() + " and we went down");
						this.debugDraw();
					}
					/////////////


					//WE ARE NOT DONE MOVING YET, WE NEED TO SEE IF WE HAVE TELEPORTED
					//TODO: SHOULD HAVE A GENERAL USE spatialHitDetecion() helper method that has shit like this in it

					if(this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()] instanceof Teleporter ){//if they are standing on a tele
						int oldX = actingEntity.getxInRoom();
						int oldY = actingEntity.getyInRoom(); //used so that we know where the teleporter is that we need to put back into entities (if we teleport the player, their internal x and y will change so cant use that)
						Teleporter theTele = (Teleporter) this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()];
						//if we move player successfully, clean up afterwards (remove their old instance on the board)
						if(theTele.teleportEntity(actingEntity)){
							this.entities[oldX][oldY] = this.entitiesCache[oldX][oldY];
							return true;
						}else{
							throw new RuntimeException("cannot tele there prob something in the way of dest");//TODO: handle differently in final release
						}
					}
					
					//check if we should portal
					if(this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()] instanceof Portal ){
						int oldX = actingEntity.getxInRoom();
						int oldY = actingEntity.getyInRoom(); //used so when we move the player, we can put the teleporter back here
						Portal thePortal = (Portal) this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()];
						//if we move player successfully, clean up afterwards (remove their old instance on the board)
						if(thePortal.attemptToTakePortal(actingEntity.getxInRoom(), actingEntity.getyInRoom(), this, actingEntity)){
							this.entities[oldX][oldY] = this.entitiesCache[oldX][oldY];
							return true;
						}else{
							throw new RuntimeException("cannot take portal there prob something in the way of dest");//TODO: handle differently in final release
						}
					}

					////we moved the player so check if they pickED UP A COIN
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
					//throw new RuntimeException("attempted to move to an invalid positon in the room");
					return false;
					}
	}


	//USING THIS TO ATTEMPT TO PICK UP AN ITEM ON THE BOARD
	private boolean attemptPickupEvent(Player actingPlayer,PlayerPickupEvent eventWeNeedToUpdateStateWith) {
		if(this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] instanceof Carryable){
			//attempt put in inventory (cant if at capacity)
			if(actingPlayer.getCurrentInventory().pickUpItem((Carryable) this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()])){
				//remove from entitiy cache because player picked it up
				this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);
				return true;
			}else{
				throw new RuntimeException("failed to pick up item"); //TODO: in reality if they cant put it in inventory, just do nothing
			}
		}else{//else return false
			throw new RuntimeException("no item aat this location to ickup: " + this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()]);//TODO: NOTE THAT WHEN WE PICK UP "NOTHING" WE ARE PICKING UP A NULL ENTITY WHICH IS "CARRYABLE".
			//THIS SHOULD NOT BE A PROBLEM BECAUSE IT JUST MEANS THAT WE WILL BE FILLING NullEntity SLOTS IN THE INVENTORY WITH OTHER NULL ENTITIES
			//return false;
		}

	}


///ATTEMPTS TO DROP EVENT AT SELECTED IDNEX IN INVENTORY ONTO THE CACHED ENTITIES ARRAY
	private boolean attemptDropEvent(Player actingPlayer,
			PlayerDropEvent eventWeNeedToUpdateStateWith) {

		//if this position in cached entitities is empty, we can drop
		if(this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] instanceof NullEntity){
			//set this position in cache to dropped item
			this.entitiesCache[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = actingPlayer.getCurrentInventory().dropItem();
			return true;
		}else{
			throw new RuntimeException("failed to drp item there is prob something already at that tile so u cant drop it broo");//TODO: sanitiy check
			//return false;
		}
	}



	///ADD ENTITIES TO THE ROOM///
	//USED TO TIDY UP OLD PORTAL GATES THAT HAVE BEEN REPLACED
	public void removeOldPortalGate(int x, int y){
		if(this.entities[x][y] instanceof Portal){
			this.entities[x][y] = new NullEntity(CardinalDirection.NORTH);
			return;
		}else if(this.entitiesCache[x][y] instanceof Portal){ //in the case that we are removing as the player goes through the portal and the player is standing on the gate so it is in the cache
			this.entitiesCache[x][y] = new NullEntity(CardinalDirection.NORTH);
			return;
		}
		throw new RuntimeException("was not able to remove the portal from the room array at the location specified");
		//return false;
	}


	//USED TO PUT THINGS IN THE ROOM. MAY BE USED BY A SMARTER SPAWNING ALGORITHM IMO. SO NEEDS NO SIDE EFFECTS IF FAILS.
	//DESTINATION MUST BE A Traversable
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
		throw new RuntimeException("teleporter's destination was not an instance of null entity");
		//return false;
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
		this.entities[myX][myY] = new StandardTeleporter(directionFaced, targetX, targetY, targetRoom);

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
		

		
		

		//USED FOR THE DAY NIGHT CYCLES
		public void setDark(boolean isDark) {
			this.isDark = isDark;
		}

		public boolean isDark() {
			return isDark;
		}

	
		//USED TO CHECK WHETHER PYLON ATTACKER IS MOVING/ATTACKING INTO A NON DAMGEABLE AND NON TRAVERSABLE ENTITY
		/**
		 * checks whether a movable entity is facing into an entity in the next position in the array that cannot be moved into or attacked.
		 * useful helper method for some of the npcs.
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
	public RenderRoomTile[][] generateDrawableTiles() {
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
	public RenderEntity[][] generateDrawableEntities() {

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
	public RenderEntity[][] generateDrawableEntitiesDarkRoom(int playerX, int playerY, boolean nightVision) {

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
	 */
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
		
		
		
		
		
		}
		

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



}
