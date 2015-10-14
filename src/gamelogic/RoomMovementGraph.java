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

/**
 * a graph of a room that can be used to determine how to pathfind through this room
 * //note that one of the reasons that our collection of nodes is a 2d array is that it makes it very easy to do some operations. e.g. if
//an actor is using the graph for pathfinding and their position in the room arrays is [10][5], then the start node for their pathfinding
//should be the node in the graph array at [10][5]
 * @author brownmax1
 *
 */


public class RoomMovementGraph {

	private MovementNode[][] graph;//the graph where each node is at the xy position of the location in the room that it represents


	RoomMovementGraph(GameEntity[][] entities){
		//first create the array of nodes
		this.graph = generateGraphFromEntities(entities);
		//now we need to link our nodes together.
		linkNodes();
		//now we have finished forming our graph. ready to be used with pathfinding algorithms etc

	}



	/**
	 * converts a 2d array of game entities into a graph
	 * @param entities the entities in this room that we are generating a graph for
	 * @return the graph for this room
	 */
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



	/**
	 * used to create all of the "edges" between nodes that can be traversed in a single step
	 */
	private void linkNodes() {

		//traverse the nodes
		for(int i = 0 ; i < this.graph.length; i ++){
			for(int j = 0; j < this.graph[i].length; j ++){
				int neighAmount = 0;

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





	/**
	 * find the shortest path between a start location and an end location and then determines which kind of move the entity should make to take that path
	 * @param startX the starting x location of the path
	 * @param startY the starting y location of the path
	 * @param endX the ending x location of the path
	 * @param endY the ending y location of the path
	 * @param uidOfMover the uid of the entity that is having its path computed
	 * @return
	 */
	protected MovementEvent getShortestPathMove(int startX, int startY, int endX, int endY, int uidOfMover){

		//DO THE SETUP FOR THE ALGORITHM

		//make the queue that all of the queue elements will be placed into
		Queue<PathfindingQueueElement> djikstraQueue = new PriorityQueue<>();

		//set all of the nodes in the graph as unvisited
		for(int i = 0; i < this.graph.length; i++){
			for(int j = 0; j < this.graph[i].length; j++){
					this.graph[i][j].setUnvisited();

			}

		}
		//get out start and end node from the supplied start and end coordinates
		MovementNode startNode =  this.graph[startX][startY];
		assert(startNode.getAdjacentNodes().size() > 0):"where the adjacents at at";
		MovementNode endNode =  this.graph[endX][endY];

		//enqueue the starting element from the startnode
		djikstraQueue.add(new PathfindingQueueElement(startNode.getAdjacentNodes(), 0, null, startNode));


		//BEGIN THE ALGORITHM PROPER
		boolean endNodeDequeued = false;
		PathfindingQueueElement currentDequeuedElement = null;

		while(!endNodeDequeued){

			assert(!djikstraQueue.isEmpty());


			//discard any already visited elements from the front of the queue
			//this slightly hacky code is a good way of detecting whether the the graph of traversable nodes has become disjoint and
			//no path can be found
			while(true){
				if(djikstraQueue.peek() == null|| djikstraQueue.peek().getBoardNode() == null){
					return exceptionalCase(uidOfMover);
				}else{
					if(djikstraQueue.peek().getBoardNode().isVisited()){
						djikstraQueue.poll();
					}else{//in the case that we pulled all the visited ones off the front
						break;
					}
				}
			}


			//get the element from the head of the queue whose neighbours we will enqueue etc
			currentDequeuedElement = djikstraQueue.poll();



			//if we just dequeued the destination node, then the currentDequeuedNode contins the "trace back" path to the start that we desire
			if(currentDequeuedElement.getBoardNode() == endNode){
				endNodeDequeued = true;
			}else{
				//the node that we dequeued is not the destination node, so we need to continue
				//we now need to enqueue element versions of all of the neighbours of the node that we just dequeued. They are
				//placed in the priority queue on the basis that nodes that have a shorter total distance to them go at front

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
		//
		//if the the actor is already on their destination (e.g. just waiting for a teleporter exit to become clear, just do nothing)
		if(steppingBackThrough.getFromElement() == null){
			return exceptionalCase(uidOfMover);
		}

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


		//
		return exceptionalCase(uidOfMover);

	}



	/**
	 * //this method is used to generate a placeholder event in the case where the graph cannot be relied upon to generate a reasonable move to the target.
	//this mostly happens when the graph of traversable nodes has become disjoint
	 * @param uidOfMover the entity that needs to move
	 * @return the useless event that the entity performs until a path is found
	 */
	private MovementEvent exceptionalCase(int uidOfMover) {
		return new PlayerMoveUp(uidOfMover);
	}









}
