package tests;

import static org.junit.Assert.*;
import gamelogic.CardinalDirection;
import gamelogic.PylonRoomState;
import gamelogic.RoomState;
import gamelogic.StandardTeleporter;
import gamelogic.Teleporter;
import gamelogic.entities.Carrier;
import gamelogic.entities.Coin;
import gamelogic.entities.GameEntity;
import gamelogic.entities.Gun;
import gamelogic.entities.HealthKit;
import gamelogic.entities.MazeWall;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Pylon;
import gamelogic.entities.SmallCarrier;
import gamelogic.entities.Treasure;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.InteriorStandardTile;

import org.junit.Test;
/**
 * AllTests
 * Test cases for some of the classes
 * @author Miguel Orevillo
 *
 */
public class AllTests {
	
	public GameEntity[][] dummyEntities;
	//==============================================//
	//==================SERVER TESTS================//
	//==============================================//
	
	//Test to see if we removed entity successfully
	@Test
	public void removeEntity(){
		generateRoom();
		dummyEntities[11][11] = new NullEntity(CardinalDirection.NORTH);
		assertFalse(dummyEntities[11][11] instanceof Pylon);
	}
	
	//Test to see if you can add in an entity to a tile with something in it already
	@Test
	public void placeInvalidEntity(){
		generateRoom();		
		dummyEntities[11][11] = new Treasure(CardinalDirection.NORTH);
		assertFalse(dummyEntities[11][11] instanceof Pylon);
	}
	
	//TODO Spawn a teleporter
	@Test
	public void spawnStandardTeleporter(){
		generateRoom();
		StandardTeleporter teleport = new StandardTeleporter(0, 0, null, 0, 0, null);
	}
	
	//Test to see if able to pickup items 
	@Test
	public void invalidPickUpItem(){
		generateRoom();
		SmallCarrier carry = new SmallCarrier(CardinalDirection.NORTH);
		Gun gun = new Gun(CardinalDirection.NORTH);
		assertFalse(carry.pickUpItem(gun));
		
	}
	
	@Test
	public void validPickUpItem(){
		SmallCarrier carry = new SmallCarrier(CardinalDirection.NORTH);
		assertTrue(carry.pickUpItem(new HealthKit(CardinalDirection.NORTH)));
	}
	
	/**
	 * Helper class to generate and get a room(specific to a pylon room)
	 * @return PylonRoomState
	 */
	private void generateRoom(){
		int width = 23;
		int height = 23;


		GameRoomTile[][] dummyTiles = new GameRoomTile[width][height];
		dummyEntities = new GameEntity[width][height];
		//fill up tha tiles
		for(int i = 0; i < width ; i++){
			for(int j = 0; j < height ; j++){
				dummyTiles[i][j] = new InteriorStandardTile();
			}
		}
		//fill up the entities with null
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height ; j++){
				dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
			}
		}
		//add the walls to edge locations
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//insert walls at the top and bottom
				//insert walls at the top and bottom
				if( i == 0   ){//walls on right with standard orientation
					dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
				}
				if(i == width - 1){//walls on the left with standard orientation
					dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
				}
				if(j == 0 ){//walls up top
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
				if(j == height - 1){//walls at bottom
					dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
				}
			}
		}

		//fill in the corners with null entities for drawing
		dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
		dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
		dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
		dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);


		//add a pylon
		Pylon bottomPylon = new Pylon(CardinalDirection.NORTH);
		dummyEntities[11][11] = bottomPylon;
		
		
		
		//add some maze walls /impassable colomn
		//below

		dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

		dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[13][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);




		//above

		dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[9][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

		dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[14][7] = new MazeWall(CardinalDirection.NORTH);


		//left
		dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][9] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

		dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);

		//right

		dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][9] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

		dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);



		PylonRoomState pylonRoom1 = new PylonRoomState(dummyTiles, dummyEntities, width, height, 5, "bottom pylon room");
		//return pylonRoom1;
	}
	
	/**
	 * Helper method to generate a room with entities we can pick up
	 */
	public void generateRoom2(){
		int width = 23;
		int height = 23;


		GameRoomTile[][]dummyTiles = new GameRoomTile[width][height];
		dummyEntities = new GameEntity[width][height];
		//fill up tha tiles
		for(int i = 0; i < width ; i++){
			for(int j = 0; j < height ; j++){
				dummyTiles[i][j] = new InteriorStandardTile();
			}
		}
		//fill up the entities with null
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height ; j++){
				dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north
			}
		}
		//add the walls to edge locations
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//insert walls at the top and bottom
				//insert walls at the top and bottom
				if( i == 0   ){//walls on right with standard orientation
					dummyEntities[i][j] = new OuterWall(CardinalDirection.WEST);
				}
				if(i == width - 1){//walls on the left with standard orientation
					dummyEntities[i][j] = new OuterWall(CardinalDirection.EAST);
				}
				if(j == 0 ){//walls up top
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
				if(j == height - 1){//walls at bottom
					dummyEntities[i][j] = new OuterWall(CardinalDirection.SOUTH);
				}
			}
		}
		//fill in the corners with null entities for drawing
		dummyEntities[0][0] = new NullEntity(CardinalDirection.NORTH);
		dummyEntities[0][height - 1] = new NullEntity(CardinalDirection.NORTH);
		dummyEntities[width - 1][0] = new NullEntity(CardinalDirection.NORTH);
		dummyEntities[width - 1][height - 1] = new NullEntity(CardinalDirection.NORTH);



		//center walls
		dummyEntities[11][11] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[11][12] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[11][13] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[11][14] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[11][10] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[11][9] = new MazeWall(CardinalDirection.EAST);

		//add the treasure
		dummyEntities[12][12] = new Treasure(CardinalDirection.NORTH);

		dummyEntities[4][9] = new MediumCarrier(CardinalDirection.NORTH);


		//add a health kit
		dummyEntities[8][2] = new HealthKit(CardinalDirection.NORTH);
		//add a health kit
		dummyEntities[18][7] = new HealthKit(CardinalDirection.NORTH);
		//add a health kit
		dummyEntities[20][2] = new HealthKit(CardinalDirection.NORTH);
		//add a health kit
		dummyEntities[5][11] = new HealthKit(CardinalDirection.NORTH);



		dummyEntities[11][19] = new Coin(CardinalDirection.NORTH);
		dummyEntities[11][20] = new Coin(CardinalDirection.NORTH);
		dummyEntities[11][18] = new Coin(CardinalDirection.NORTH);
		dummyEntities[11][17] = new Coin(CardinalDirection.NORTH);

		//INNER LAYER
		//below

		dummyEntities[8][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[9][15] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[10][15] = new MazeWall(CardinalDirection.SOUTH);

		dummyEntities[12][15] = new MazeWall(CardinalDirection.SOUTH);

		dummyEntities[14][15] = new MazeWall(CardinalDirection.SOUTH);
		//above

		dummyEntities[8][7] = new MazeWall(CardinalDirection.NORTH);

		dummyEntities[10][7] = new MazeWall(CardinalDirection.NORTH);

		dummyEntities[12][7] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[13][7] = new MazeWall(CardinalDirection.NORTH);
		//left
		dummyEntities[7][8] = new MazeWall(CardinalDirection.WEST);

		dummyEntities[7][10] = new MazeWall(CardinalDirection.WEST);

		dummyEntities[7][12] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][13] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[7][14] = new MazeWall(CardinalDirection.WEST);
		//right

		dummyEntities[15][8] = new MazeWall(CardinalDirection.EAST);

		dummyEntities[15][10] = new MazeWall(CardinalDirection.EAST);

		dummyEntities[15][12] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][13] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[15][14] = new MazeWall(CardinalDirection.EAST);


		//MIDDLE LAYER
		//add some maze walls /impassable colomn
		//below
		dummyEntities[8][18] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[9][18] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[10][18] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[12][18] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[13][18] = new MazeWall(CardinalDirection.SOUTH);
		dummyEntities[14][18] = new MazeWall(CardinalDirection.SOUTH);

		//above

		dummyEntities[8][5] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[9][5] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[10][5] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[12][5] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[13][5] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[14][5] = new MazeWall(CardinalDirection.NORTH);


		//left
		dummyEntities[5][8] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[5][9] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[5][10] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[5][12] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[5][13] = new MazeWall(CardinalDirection.WEST);
		dummyEntities[5][14] = new MazeWall(CardinalDirection.WEST);


		//right
		dummyEntities[18][8] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[18][9] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[18][10] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[18][12] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[18][13] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[18][14] = new MazeWall(CardinalDirection.EAST);


		//OUTERMOST LAYER
		//add some maze walls /impassable colomn
		//below
		dummyEntities[6][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[7][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[8][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[9][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[10][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[12][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[14][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[15][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[16][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[17][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[18][20] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[19][20] = new MazeWall(CardinalDirection.NORTH);
		//above
		dummyEntities[6][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[7][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[8][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[9][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[10][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[12][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[13][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[14][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[15][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[16][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[17][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[18][3] = new MazeWall(CardinalDirection.NORTH);
		dummyEntities[19][3] = new MazeWall(CardinalDirection.NORTH);





		//left
		dummyEntities[3][4] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][5] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][6] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][7] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][8] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][9] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][10] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][12] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][13] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][14] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][15] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][16] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][17] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][18] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][19] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[3][12] = new MazeWall(CardinalDirection.EAST);


		//right
		dummyEntities[20][4] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][5] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][6] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][7] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][8] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][9] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][10] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][13] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][14] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][15] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][16] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][17] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][18] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][19] = new MazeWall(CardinalDirection.EAST);
		dummyEntities[20][12] = new MazeWall(CardinalDirection.EAST);


		RoomState mazeRoom2 = new RoomState(dummyTiles, dummyEntities, width, height, 1, "right top maze");
	}
}
