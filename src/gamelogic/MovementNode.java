package gamelogic;

import java.util.ArrayList;




/**
 * a node in the graph that is used by the pathfinding algorithm. Represents a location in a room.
 * @author brownmax1
 *
 */
public abstract class MovementNode {

	//collection of the other nodes in the graph that are adjacent to this one (i.e. the places that are one step away)
		private ArrayList<MovementNode> adjacentNodes = new ArrayList<>();

		//keeps track of whether this node has been visited yet on a given pass of the algorithm. Obviously these need to be set
		//back to false after the algorithm runs/before it runs again
		private boolean visited = false;//!!!this is important because it is possible to have the same node object enqueued many times in the algorithm queue (i.e. the same node object has been enqueued several times with a different fromNode value), so we need to be able to check whether a certain node has had its shortest path found yet.

		//the coordinates of the location that this node represents
		private final int xPositionInRoom;
		private final int yPositionInRoom;


		public MovementNode(int x, int y) {
			this.xPositionInRoom = x;
			this.yPositionInRoom = y;
		}



		protected void addAdjacentNode(MovementNode neighbour){
			this.adjacentNodes.add(neighbour);

		}




		/**
		 * returns whether this node has been visited yet or not by this run of the algorithm
		 * @return true if the algorithm has visited this BoardNode yet on this run of the algorithm, else false
		 */
		public boolean isVisited(){
		return this.visited;
		}


		/**
		 * sets this node as visited (we have found the shortest path to this node)
		 */
		public void setVisited(){
			if(this.visited){
				throw new RuntimeException("why are you trying to set a visited node as visited (you cannot visit a node twice)");
			}
			this.visited = true;
		}



		/**
		 * Used as a reset method between runs of the algorithm. We need to do this because e.g. in a two player game if we are
		 * running the algorithm on the second player's turn. we do not care which tiles were "visited" on the first player's turn.
		 */
		public void setUnvisited(){
			this.visited = false;
		}





		public int getX() {
			return xPositionInRoom;
		}


		public int getY() {
			return yPositionInRoom;
		}



		public ArrayList<MovementNode> getAdjacentNodes() {
			return adjacentNodes;
		}




}
