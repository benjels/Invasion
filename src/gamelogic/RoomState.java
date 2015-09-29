package gamelogic;

import gamelogic.entities.GameEntity;
import gamelogic.entities.ImpassableColomn;
import gamelogic.entities.KeyCard;
import gamelogic.entities.MovableEntity;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Player;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.Teleporter;
import gamelogic.events.IDedPlayerEvent;
import gamelogic.events.IDedPlayerMoveDown;
import gamelogic.events.IDedPlayerMoveLeft;
import gamelogic.events.IDedPlayerMoveRight;
import gamelogic.events.IDedPlayerMoveUp;
import gamelogic.tiles.GameRoomTile;
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

	public RoomState(GameRoomTile[][] tiles, GameEntity[][] entities, int width, int height, int roomId) {
		this.tiles = tiles;
		this.entities = entities;
		this.roomWidth = width;
		this.roomHeight = height;
		this.roomId = roomId;
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
	 * attempt to cause an event on the game world by a certain player. e.g.
	 * attempt to move north. This will probably
	 *
	 * @param actingPlayer
	 *            the player who is attempting to cause the action
	 * @param eventWeNeedToUpdateStateWith
	 *            the event that this player is attempting to cause
	 * @return true if the event was applied successfully, else false.
	 */
	boolean attemptGameMapEventByPlayer(Player actingPlayer,
			IDedPlayerEvent eventWeNeedToUpdateStateWith) {
		// determine which kind of map event this is
		if (eventWeNeedToUpdateStateWith instanceof MovementEvent) {
			return attemptMovementEvent(actingPlayer,
					(MovementEvent) eventWeNeedToUpdateStateWith);
		} else {
			throw new RuntimeException("that's not a valid event atm");
		}

	}

	/**
	 * used to attempt to move a player around this room
	 *
	 * @param actingPlayer
	 *            the player attempting to move
	 * @param eventWeNeedToUpdateStateWith
	 *            the kind of move they are attempting
	 * @return bool true if the move was applied successfully, else false
	 */
	private boolean attemptMovementEvent(Player actingPlayer,
			MovementEvent playerMove) {
		//TODO: morph the requested move event depending on current perspective. e.g.
		// if player chose up, but the current perspective treats east as up,
		// change the event to IDedPlayerMoveLeft

		//TODO: note that the only kinds of moves that are acceptable at the moment are 1 square hops. if we change these move distances in the player
		//strategies, we need to make these move checkers more sophisticated ELSE make this just check the strategy and for all strategies except phase walker make them move 1 and for phase walker
		//make it move two and make it j...that sounds p hacky actually

		// decide which kind of move submethod we are going to use for this move
		System.out.println("so the player is at the following x and y in this room: " + actingPlayer.getxInRoom() + " " + actingPlayer.getyInRoom() + " and we are attempting to: " + playerMove);
		System.out.println("now printing out a crude representation of the board");


	this.debugDraw();



		if (playerMove instanceof IDedPlayerMoveUp) {
			return attemptOneSquareMove(actingPlayer, -1, 0);
		} else if (playerMove instanceof IDedPlayerMoveLeft) {
			return attemptOneSquareMove(actingPlayer, 0, -1);
		} else if (playerMove instanceof IDedPlayerMoveRight) {
			return attemptOneSquareMove(actingPlayer, 0, 1);
		} else if (playerMove instanceof IDedPlayerMoveDown) {
			return attemptOneSquareMove(actingPlayer, 1, 0); //TODO: maybe declare these as constrans (seems uncesseasttrssrdsffrry tho)
		} else {
			throw new RuntimeException(
					"this is not a valid move at the moment: " + playerMove);
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
						
	
					//we are moving out of a position so fill that position in the entities array with the cached entity at same location
					this.entities[actingEntity.getxInRoom()][actingEntity.getyInRoom()] = this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()];
					
					//update the player's entity position in the array
					this.entities[actingEntity.getxInRoom() + xOffset][actingEntity.getyInRoom() + yOffset] =  actingEntity;
					
					//update the player's internal x and y coordinates
					actingEntity.setyInRoom(actingEntity.getyInRoom() + yOffset);
					actingEntity.setxInRoom(actingEntity.getxInRoom() + xOffset);
					
				
					///DEBUG SHIT
					System.out.println("HAVING ATTEMPTED THE MOVE...");
	
					System.out.println("so the player is at the following x and y in this room: " + actingEntity.getxInRoom() + " " + actingEntity.getyInRoom() + " and we went down");
					this.debugDraw();
					/////////////


					//WE ARE NOT DONE MOVING YET, WE NEED TO SEE IF WE HAVE TELEPORTED
					//TODO: SHOULD HAVE A GENERAL USE spatialHitDetecion() helper method that has shit like this in it

					if(this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()] instanceof Teleporter){//if they are standing on a tele
						int oldX = actingEntity.getxInRoom();
						int oldY = actingEntity.getyInRoom(); //used so that we know where the teleporter is that we need to put back into entities (if we teleport the player, their internal x and y will change so cant use that)
						Teleporter theTele = (Teleporter) this.entitiesCache[actingEntity.getxInRoom()][actingEntity.getyInRoom()];
						//if we move player successfully, clean up afterwards (remove their old instance on the board)
						if(theTele.teleportEntity(actingEntity)){
							this.entities[oldX][oldY] = this.entitiesCache[oldX][oldY];
						}else{
							throw new RuntimeException("cannot tele there prob something in the way of dest");//TODO: handle differently in final release
						}
					
					
					}

					//we moved the player so we return true
					return true;

				}else{//in the case that we cannot move to the desired tile
					throw new RuntimeException("attempted to move to an invalid positon in the room");
					//TODO:will be return false
					}
	}
	
		






	///ADD ENTITIES TO THE ROOM///


	/**
	 * attempt to put the player somewhere in the room
	 * @param player the player that we are adding to the room
	 * @return the location that the player was added to in the board IF THE ADDING WAS CORRECT, else null !!!
	 */
	public RoomLocation spawnPlayerInRoom(Player player) {//TODO: make this smarter i.e. centre of room or some shit
		//for now we start at the top left and try to find a free square
		for(int i = 0; i < this.tiles.length; i++){
			for(int j = 0; j < this.tiles[i].length; j++){
				if(this.tiles[i][j] instanceof Traversable && this.entities[i][j] instanceof NullEntity){// if the square is available, put the player there
					this.entities[i][j] = player;
					System.out.println("put the player at :" + i + " " + j);
					return new RoomLocation(i, j);
				}else{
					System.out.println("cant spawn the player here because not a free square" + i + " " + j + "the entity here is: " + this.entities[i][j] + " and the traversablitiy of this tile is: " + "and the tile is " + this.tiles[i][j]);
					if(this.entities[i][j] instanceof Traversable){
						System.out.println("true");
					}
				}
			}
		}

		throw new RuntimeException("was not able to place the player");//TODO: note that in the final release should resolve this contingency more safely

	}

	//USED TO PUT THINGS IN THE ROOM. MAY BE USED BY A SMARTER SPAWNING ALGORITHM IMO. SO NEEDS NO SIDE EFFECTS IF FAILS.
	public boolean attemptToPlaceEntityInRoom(MovableEntity entToMove, int destinationx, int destinationy) {
		//if the teleporter receiver tile has entities on it, we cannot teleport
		if(this.entities[destinationx][destinationy] instanceof NullEntity){
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
	public void spawnTeleporter(CardinalDirection directionFaced, int myX, int myY, int targetX, int targetY, RoomState targetRoom) {
		this.entities[myX][myY] = new Teleporter(directionFaced, targetX, targetY, targetRoom);

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
	 * @return the 2d array of drawable tiles
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
	 * draws simple room.used for debug
	 */
		public void debugDraw() {
			/// DEBUG DRAWING THAT DRAWS THE ROOMSTATE BEFORE EVERY ACTION ATTEMPTED BY A PLAYER ///


			for(int i = 0; i < this.roomHeight ; i ++){
				for(int j = 0; j < this.roomWidth ; j ++){

					if(this.entities[j][i] instanceof Teleporter){
						System.out.print("D  ");
						continue;
					}




					if(this.entities[j][i] instanceof NullEntity){
						System.out.print("n  ");
					}else if(this.entities[j][i] instanceof ImpassableColomn){
						System.out.print("i  ");
					}else if(this.entities[j][i] instanceof OuterWall){
						System.out.print("x  ");
					}else if (this.entities[j][i] instanceof Player){
						System.out.print("p  ");
					}
					else if(this.entities[j][i] instanceof KeyCard){
						System.out.print("k  ");
					}

				}
				System.out.println("\n");
			}

		}

	public int getId() {
		return this.roomId;
	}








}
