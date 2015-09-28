package gamelogic;

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

	private GameRoomTile[][] tiles;// 2d array of the tiles in this room. [x][y]. first x is on the far left. first y is on the top.
	private GameEntity[][] entities; // 2d array of the items in this room. ordered in same way as tiles.


	public RoomState(GameRoomTile[][] tiles, GameEntity[][] entities, int width, int height) {
		this.tiles = tiles;
		this.entities = entities;
		this.roomWidth = width;
		this.roomHeight = height;

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
			return attemptMoveUp(actingPlayer, actingPlayer.getMoveDistance());
		} else if (playerMove instanceof IDedPlayerMoveLeft) {
			return attemptMoveLeft(actingPlayer, actingPlayer.getMoveDistance());
		} else if (playerMove instanceof IDedPlayerMoveRight) {
			return attemptMoveRight(actingPlayer, actingPlayer.getMoveDistance());
		} else if (playerMove instanceof IDedPlayerMoveDown) {
			return attemptMoveDown(actingPlayer, actingPlayer.getMoveDistance());
		} else {
			throw new RuntimeException(
					"this is not a valid move at the moment: " + playerMove);
		}


	}




	private boolean attemptMoveUp(Player actingPlayer, int attemptedMoveOffset) {
		//check that we are moving within allowed array boundaries
		if(actingPlayer.getyInRoom() - attemptedMoveOffset <= 0){
			throw new RuntimeException("attemmpting to move outside of array boundaries not rly an exception but ye");
			//return false;
		}


		//check that the square that we are moving to is a traversable and that there is no other entity in that position
		if(this.tiles[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() - attemptedMoveOffset] instanceof TraversableTile &&
				this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() - attemptedMoveOffset] instanceof TraversableEntity){

			//set the player's old position to be empty now
			this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() - attemptedMoveOffset] = new NullEntity(CardinalDirection.NORTH);//setting null entities as north as a default value

			//update the player's entity position in the array
			this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() - attemptedMoveOffset] =  actingPlayer;
			//update the player's internal x and y coordinates
			actingPlayer.setyInRoom(actingPlayer.getyInRoom() - attemptedMoveOffset);

			System.out.println("HAVING ATTEMPTED THE MOVE...");

			System.out.println("so the player is at the following x and y in this room: " + actingPlayer.getxInRoom() + " " + actingPlayer.getyInRoom() + " and we went up");
			this.debugDraw();


			//we moved the player so we return true
			return true;

		}else{//in the case that we cannot move to the desired tile

			throw new RuntimeException("attempted to move to an invalid positon in the room");
			//return false;
			}
	}

	private boolean attemptMoveDown(Player actingPlayer, int attemptedMoveOffset) {
		System.out.println("attempting to move out of bounds ???: " + actingPlayer.getyInRoom() + " " + attemptedMoveOffset);
		//check that we are moving within allowed array boundaries
				if(actingPlayer.getyInRoom() + attemptedMoveOffset >= this.roomHeight ){

					throw new RuntimeException("attemmpting to move outside of array boundaries not rly an exception but ye");
					//return false;
				}

				//check that the square that we are moving to is a traversable and that there is no other entity in that position
				if(this.tiles[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() + attemptedMoveOffset] instanceof TraversableTile &&
						this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() + attemptedMoveOffset] instanceof TraversableEntity){

					//set the player's old position to be empty now
					this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);

					//update the player's entity position in the array
					this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom() + attemptedMoveOffset] =  actingPlayer;
					//update the player's internal x and y coordinates
					actingPlayer.setyInRoom(actingPlayer.getyInRoom() + attemptedMoveOffset);


					System.out.println("HAVING ATTEMPTED THE MOVE...");

					System.out.println("so the player is at the following x and y in this room: " + actingPlayer.getxInRoom() + " " + actingPlayer.getyInRoom() + " and we went down");
					this.debugDraw();
					//we are not done moving yet. check whether we moved onto a teleporter


					if(this.tiles[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] instanceof DoorTile){
						DoorTile theDoor = (DoorTile) this.tiles[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()];
						//if we move player successfully, clean up afterwards (remove their old instance on the board)
						if(theDoor.teleportEntity(actingPlayer)){
							this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);
						}
					}



					//we moved the player so we return true
					return true;

				}else{//in the case that we cannot move to the desired tile
					throw new RuntimeException("attempted to move to an invalid positon in the room");
					//return false;
					}
	}

	private boolean attemptMoveRight(Player actingPlayer, int attemptedMoveOffset) {
		//check that we are moving within allowed array boundaries
		if(actingPlayer.getxInRoom() + attemptedMoveOffset >= this.roomWidth){
			throw new RuntimeException("attemmpting to move outside of array boundaries not rly an exception but ye");
			//return false;
		}


		//check that the square that we are moving to is a traversable and that there is no other entity in that position
		if(this.tiles[actingPlayer.getxInRoom() + attemptedMoveOffset][actingPlayer.getyInRoom()] instanceof TraversableTile &&
				this.entities[actingPlayer.getxInRoom() + attemptedMoveOffset][actingPlayer.getyInRoom()] instanceof TraversableEntity){

			//set the player's old position to be empty now
			this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);

			//update the player's entity position in the array
			this.entities[actingPlayer.getxInRoom() + attemptedMoveOffset][actingPlayer.getyInRoom()] =  actingPlayer;

			//update the player's internal x and y coordinates
			actingPlayer.setxInRoom(actingPlayer.getxInRoom() + attemptedMoveOffset);

			System.out.println("HAVING ATTEMPTED THE MOVE...");

			System.out.println("so the player is at the following x and y in this room: " + actingPlayer.getxInRoom() + " " + actingPlayer.getyInRoom() + " and we went right");
			this.debugDraw();

			//we moved the player so we return true
			return true;

		}else{//in the case that we cannot move to the desired tile
			if(!(this.entities[actingPlayer.getxInRoom() + attemptedMoveOffset][actingPlayer.getyInRoom()] instanceof NullEntity)){
				System.out.println("you are trying to move through something that is impassable: " + this.entities[actingPlayer.getxInRoom() + attemptedMoveOffset][actingPlayer.getyInRoom()]);
			}
			throw new RuntimeException("attempted to move to an invalid positon in the room");
			//return false;
			}

	}

	private boolean attemptMoveLeft(Player actingPlayer, int attemptedMoveOffset) {
		//check that the square that we are moving to is a traversable and that there is no other entity in that position
		//check that we are moving within allowed array boundaries
				if(actingPlayer.getxInRoom() - attemptedMoveOffset < 0){
					throw new RuntimeException("attemmpting to move outside of array boundaries not rly an exception but ye");
					//return false;
				}


				//check that the square that we are moving to is a traversable and that there is no other entity in that position
				if(this.tiles[actingPlayer.getxInRoom() - attemptedMoveOffset][actingPlayer.getyInRoom()] instanceof TraversableTile &&
						this.entities[actingPlayer.getxInRoom() - attemptedMoveOffset][actingPlayer.getyInRoom()] instanceof TraversableEntity){





					//set the player's old position to be empty now
					this.entities[actingPlayer.getxInRoom()][actingPlayer.getyInRoom()] = new NullEntity(CardinalDirection.NORTH);


					//update the player's entity position in the array
					this.entities[actingPlayer.getxInRoom() - attemptedMoveOffset][actingPlayer.getyInRoom()] =  actingPlayer;

					//update the player's internal x and y coordinates
					actingPlayer.setxInRoom(actingPlayer.getxInRoom() - attemptedMoveOffset);

					System.out.println("HAVING ATTEMPTED THE MOVE...");

					System.out.println("so the player is at the following x and y in this room: " + actingPlayer.getxInRoom() + " " + actingPlayer.getyInRoom() + " and we went right");
					this.debugDraw();

					//we moved the player so we return true
					return true;

				}else{//in the case that we cannot move to the desired tile

					throw new RuntimeException("attempted to move to an invalid positon in the room (move left)");
					//return false;
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
				if(this.tiles[i][j] instanceof TraversableTile && this.entities[i][j] instanceof NullEntity){// if the square is available, put the player there
					this.entities[i][j] = player;
					System.out.println("put the player at :" + i + " " + j);
					return new RoomLocation(i, j);
				}else{
					System.out.println("cant spawn the player here because not a free square" + i + " " + j + "the entity here is: " + this.entities[i][j] + " and the traversablitiy of this tile is: " + "and the tile is " + this.tiles[i][j]);
					if(this.entities[i][j] instanceof TraversableTile){
						System.out.println("true");
					}
				}
			}
		}

		throw new RuntimeException("was not able to place the player");//TODO: note that in the final release should resolve this contingency more safely

	}

	//USED TO PUT THINGS IN THE ROOM. MAY BE USED BY A SMARTER SPAWNING ALGORITHM IMO. SO NEEDS NO SIDE EFFECTS IF FAILS.
	public void attemptToPlaceEntityInRoom(GameEntity entToMove) {


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

					if(this.tiles[j][i] instanceof DoorTile){
						System.out.print("D  ");
						continue;
					}




					if(this.entities[j][i] instanceof NullEntity){
						System.out.print("n  ");
					}else if(this.entities[j][i] instanceof ImpassableColomn){
						System.out.print("r  ");
					}else if(this.entities[j][i] instanceof OuterWall){
						System.out.print("x  ");
					}else if (this.entities[j][i] instanceof Player){
						System.out.print("p  ");
					}
					else{
						//throw new RuntimeException("that is not a known kind of entity");
					}
				}
				System.out.println("\n");
			}

		}







}
