package gamelogic;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;




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
import gamelogic.events.PlayerMoveDown;
import gamelogic.events.PlayerMoveLeft;
import gamelogic.events.PlayerMoveRight;
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
				if(entities[i][j] instanceof Traversable || entities[i][j] instanceof Player){ //NOTE: Player is counted as a traversable node because it is usually the destination of the pathfind. we need to be able to treat it as a place that can be reached because otherwise we cannot enqueue an element for its location.
					graph[i][j] = new TraversableNode(i, j);
				}else{//if it is not traversable, we put an ImpassableNode into the graph
					graph[i][j] = new ImpassableNode(i, j);
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
		}
	}

	//for any found path, we should expect that all nodes except for the first and maybe last (if
	//the target is player not teleporter) are traversable nodes
	
	
	//FINDS THE SHORTEST PATH BETWEEN A SPECIFIED START AND END NODE COORDINATE.
	//AT THE MOMENT THIS METHOD JUSE RETURNS A DIRECTION WHICH IS THE DIRECTION THAT SHOULD BE MOVED IN
	//FROM THE START LOCATION TO REACH THE END LOCATION ALONG THE SHORTEST PATH THAT WAS JUST FOUND
	protected MovementEvent getShortestPathMove(int startX, int startY, int endX, int endY, int uidOfMover){
		System.out.println("tasked with finding the shortest path from" + startX + "." + startY + " to: " + endX + "." + endY);
		//DO THE SETUP FOR THE ALGORITHM
		
		//make the queue that all of the queue elements will be placed into
		Queue<PathfindingQueueElement> djikstraQueue = new PriorityQueue<>();
		
		//set all of the nodes in the graph as unvisited
		for(int i = 0; i < this.graph.length; i++){
			for(int j = 0; j < this.graph[i].length; j++){
					this.graph[i][j].setUnvisited(); 
					System.out.print(this.graph[i][j].getAdjacentNodes().size());
			}
			System.out.println(" ");
		}
		//get out start and end node from the supplied start and end coordinates
		MovementNode startNode =  this.graph[startX][startY];
		assert(startNode.getAdjacentNodes().size() > 0):"where the adjacents at at";
		MovementNode endNode =  this.graph[endX][endY];
		System.out.println("so about to find a path from the start:" + startNode.getX() + "." + startNode.getY() + " to the end: " + endNode.getX() + "." + endNode.getY());
		
		//enqueue the starting element from the startnode
		djikstraQueue.add(new PathfindingQueueElement(startNode.getAdjacentNodes(), 0, null, startNode));
		
		
		//BEGIN THE ALGORITHM PROPER
		boolean endNodeDequeued = false;
		PathfindingQueueElement currentDequeuedElement = null;
		
		while(!endNodeDequeued){
			
			assert(!djikstraQueue.isEmpty());
			
			//discard any already visited elements from the front ofqueue
			while(djikstraQueue.peek().getBoardNode().isVisited()){
				djikstraQueue.poll();
			}
			
			//get the element from the head of the queue whose neighbours we will enqueue etc
			currentDequeuedElement = djikstraQueue.poll();
			System.out.println("dequeued the elem at:" + currentDequeuedElement.getBoardNode().getX() + "." + currentDequeuedElement.getBoardNode().getY());
				
			
			//if we just dequeued the destination node, then the currentDequeuedNode contins the "trace back" path to the start that we desire	
			if(currentDequeuedElement.getBoardNode() == endNode){
				System.out.println("FOUND THE DESTINATION, IT'S DISTANCE IS: " + currentDequeuedElement.getPathDistance());
				endNodeDequeued = true;
			}else{
				//the node that we dequeued is not the destination node, so we need to continue
				//we now need to enqueue element versions of all of the neighbours of the node that we just dequeued. They are
				//placed in the priority queue on the basis that nodes that have a shorter total distance to them go at front
				assert(currentDequeuedElement.getNeighbours().size() > 0 && currentDequeuedElement.getNeighbours().size() < 5):"wrong size of neighbours:" + currentDequeuedElement.getNeighbours().size();
				for(MovementNode eachNode: currentDequeuedElement.getNeighbours()){
					//enqueue all of the unvisited neighbours
					if(!eachNode.isVisited()){
						djikstraQueue.add(new PathfindingQueueElement(eachNode.getAdjacentNodes(), currentDequeuedElement.getPathDistance() + 1, currentDequeuedElement, eachNode));
					}
				}	
			}
			//so now we have made enqueued element versions of all of the node neighbours of the element
			//that we pulled out of the head of the queue. Now we just start again from the top and keep dequeing
			//elements and enqueuing their invisited neighbours until we reach our destination
			currentDequeuedElement.getBoardNode().setVisited();
		}
		
		//so now currentDequedElement is the element of the node which represents the place in the room where the target is
		//we trace back through all of the elements using the fromElement field until we reach the last element (whose fromElement was set to null)
		//we compare the x and y of the penultimate and final elements in this backwardsly linked list to determine which direction the actor should move
		//in to take this shortest path
		
		PathfindingQueueElement steppingBackThrough = currentDequeuedElement;
		while(steppingBackThrough.getFromElement().getFromElement() != null){ //looking backwards two places in linked list because we want to reach the penultimate element to compare its x and y with final element
			steppingBackThrough = steppingBackThrough.getFromElement();
		}
		
		//we found the penultimate elem in the linked list, so compare its x and y to the starting x and y and return the movement the actor should perform to go to that location.
		int goToX = steppingBackThrough.getBoardNode().getX();
		int goToY = steppingBackThrough.getBoardNode().getY();
		if(startX == goToX && startY - 1 == goToY){//should go up
			return new PlayerMoveUp(uidOfMover);
		}else if(startX == goToX && startY + 1 == goToY){//should go down
			return new PlayerMoveDown(uidOfMover);
		}else if(startX - 1 == goToX && startY == goToY){//should go left
			return new PlayerMoveLeft(uidOfMover);
		}else if(startX + 1 == goToX && startY == goToY){//should go right
			return new PlayerMoveRight(uidOfMover);
		}else{
			assert false: "the difference should be one of those above";
		}
		
		
		System.out.println("and we reached the element of the node that represents the location: " + steppingBackThrough.getBoardNode().getX() + "." + steppingBackThrough.getBoardNode().getY());
		//hey if it all fucked up moving down cant hurt right?
		return new PlayerMoveDown(uidOfMover);

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
