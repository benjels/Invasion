package Tests;

import static org.junit.Assert.*;
import gamelogic.CardinalDirection;
import gamelogic.PylonRoomState;
import gamelogic.entities.GameEntity;
import gamelogic.entities.MazeWall;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Pylon;
import gamelogic.entities.Treasure;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.InteriorStandardTile;

import org.junit.Test;

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
	
	//Test to see if able to pickup items 
	@Test
	public void pickUpItem(){
		
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
}
