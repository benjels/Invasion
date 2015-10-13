package gamelogic;

import java.util.ArrayList;




//A NODE THAT IS USED TO FORM THE GRAPH
public abstract class MovementNode {
		//collection of the other nodes in the graph that are adjacent to this one (i.e. the places that are one step away)
		private ArrayList<MovementNode> adjacentNodes = new ArrayList<>();
		//keeps track of whether this node has been visited yet on a given pass of the algorithm. Obviously these need to be set
		//back to false after the algorithm runs/before it runs again
		private boolean visited = false;//!!!this is important because it is possible to have the same node object enqueued many times in the algorithm queue (i.e. the same node object has been enqueued several times with a different fromNode value), so we need to be able to check whether a certain node has had its shortest path found yet.
	
		
		//USED TO ADD A NODE AS A NEIGHBOURING NODE THAT CAN BE MOVED TO IN ONE STEP FROM THIS ONE
		protected void addAdjacentNode(MovementNode neighbour){
			this.adjacentNodes.add(neighbour);
		}
	
}
