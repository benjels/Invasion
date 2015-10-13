package gamelogic;

import gamelogic.entities.Coin;
import gamelogic.entities.GameEntity;
//THE GRAPH EXPRESSED AS A 2D ARRAY OF NODES WHERE NORMAL TRAVERSABLE PLACES ARE StandardNode
//, TELEPORTERS ARE TeleporterNode and things that cannot be moved onto (e.g. walls) are nulls.
import gamelogic.entities.Gun;
import gamelogic.entities.HealthKit;
import gamelogic.entities.KeyCard;
import gamelogic.entities.MazeWall;
import gamelogic.entities.MediumCarrier;
import gamelogic.entities.NightVisionGoggles;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.entities.Player;
import gamelogic.entities.Pylon;
import gamelogic.entities.SmallCarrier;
import gamelogic.entities.TeleporterGun;
import gamelogic.entities.Treasure;
import gamelogic.events.MovementEvent;
import gamelogic.events.PlayerMoveLeft;
import gamelogic.events.PlayerMoveUp;
import gamelogic.tiles.HarmfulTile;

//FIRST THE 2D ARRAY IS CREATED BY TRAVERSING THE ENTITIES OF THE ROOM THAT THIS GRAPH IS FOR.
//THEN (ONCE THE 2D ARRAY IS FILLED WITH NODES AND NULLS APPROPRIATELY, WE TRAVERSE ALL OF THOSE TO ADD EDGES TO THE GRAPH). E.G.
//if spaces[0][0] is a TraversableNode and spaces[0][1] is a TraversableNode, we should add each of them to the other's collection of
//links

//note that one of the reasons that our collection of nodes is a 2d array is that it makes it very easy to do some operations. e.g. if
//an actor is using the graph for pathfinding and their position in the room arrays is [10][5], then the start node for their pathfinding
//should be the node in the graph array at [10][5]

public class RoomMovementGraph {
	
	private MovementNode[][] graph;

	
	RoomMovementGraph(GameEntity[][] entities){
		//first create the array of nodes
		this.graph = generateGraphFromEntities(entities);
		//now we need to link our nodes together.
		linkNodes();
		//now we have finished forming our graph. ready to be used with pathfinding algorithms etc
		
		//debugPrintGraph();
	}



	//USED TO CONVERT A 2D ARRAY OF ENTITIES INTO A 2D ARRAY OF NODES. I.E. THE GRAPH FOR THIS ROOM
	private MovementNode[][] generateGraphFromEntities(GameEntity[][] entities) {
		
		//make the array to house the nodes
		
		MovementNode[][] graph = new MovementNode[entities.length][entities[0].length];
		//we need to traverse the array of entities and fill in the array of nodes appropriately. e.g. ent[1][1] instanceof Traversable, node[1][1] = new TraversableNode 
		for(int i = 0; i < entities.length ;  i++){
			for(int j = 0; j < entities[i].length; j++){
				//if it is traversable, we put a TraversableNode into the graph
				if(entities[i][j] instanceof Traversable){
					graph[i][j] = new TraversableNode();
				}else{//if it is not traversable, we put an ImpassableNode into the graph
					graph[i][j] = new ImpassableNode();
				}
				//TODO: probably do not need any more kinds of nodes because we will jsut make the teleporters become the "target" if no player in same room as zombie.
				//NOTE PUT LOCKED TELEPORTER OUT OF THE WAY OF ZOMBIES!!!
			}
		}
		
		//we created the node array, so return it
		return graph;
	}
	

	
	//USED TO CREATE THE "EDGES"/ASSOCIATIONS BETWEEN NODES IN THE 2D ARRAY
	private void linkNodes() {
		//traverse the nodes
		for(int i = 0 ; i < this.graph.length; i ++){
			for(int j = 0; j < this.graph[i].length; j ++){
				int neighAmount = 0; //TODO: for debugggg
				
				//if it is a traversable node, then we might have to add neighbours
				if(this.graph[i][j] instanceof TraversableNode){
					//ADD NORTH NEIGHBOUR
					if(j > 0 && this.graph[i][j - 1] instanceof TraversableNode){
						neighAmount++;
						this.graph[i][j].addAdjacentNode(this.graph[i][j - 1]);
					}
					//ADD SOUTH NEIGHBOUR
					if(j < this.graph[0].length - 1 && this.graph[i][j + 1] instanceof TraversableNode){
						neighAmount++;
						this.graph[i][j].addAdjacentNode(this.graph[i][j + 1]);
					}
					//ADD EAST NEIGHBOUR
					if(i < this.graph.length - 1 && this.graph[i + 1][j] instanceof TraversableNode){
						neighAmount++;
						this.graph[i][j].addAdjacentNode(this.graph[i + 1][j]);
					}
					//ADD WEST NEIGHBOUR
					if(i > 0 && this.graph[i - 1][j] instanceof TraversableNode){
						neighAmount++;
						this.graph[i][j].addAdjacentNode(this.graph[i - 1][j]);
					}
				}
			
				//System.out.println("finished adding the neighbours to the node at: " + i + " " + j + " and it has the following amount of neighbours:"  + neighAmount);
			}
		}
	}

	
	
	//FINDS THE SHORTEST PATH BETWEEN A SPECIFIED START AND END NODE COORDINATE.
	//AT THE MOMENT THIS METHOD JUSE RETURNS A DIRECTION WHICH IS THE DIRECTION THAT SHOULD BE MOVED IN
	//FROM THE START LOCATION TO REACH THE END LOCATION ALONG THE SHORTEST PATH THAT WAS JUST FOUND
	protected MovementEvent getShortestPathMove(int startX, int startY, int endX, int endY, int uidOfMover){
		assert(uidOfMover > 29 && uidOfMover < 40);
		return new PlayerMoveLeft(30);
		//do this shit fam
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void debugPrintGraph() {
		for(int i = 0; i < this.graph.length ; i ++){
			for(int j = 0; j < this.graph[0].length ; j ++){
				 if(this.graph[j][i] instanceof TraversableNode){
					System.out.print("T  ");
				}else if(this.graph[j][i] instanceof ImpassableNode){
					System.out.print("N  ");
				}
				else{

					throw new RuntimeException("some kind of unrecogniesd entity was attempted to drawraw. you prob added an entity to the game and forgot to add it here");
				}

			}
			
		System.out.println("\n");
		}
	
	}
	
	
	
	
	
	
}
