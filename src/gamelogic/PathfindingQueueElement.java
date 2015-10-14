package gamelogic;



import java.util.ArrayList;


/**
 * elements that is used with the queue when djikstra's shortest path algorithm is run.
 * Essentially, every time we are "enqueueing" a tile, we are creating a NodeElement from the BoardNode that we want to enqueue
 * The adjacent tiles to this one that we have enqueued are stored as BoardNodes, so when an instance of this class is dequeued, we need to create
 * NodeElement versions of its BoardNode neighbour and enqueue those in the queue.
 * @author maxbrown0101
 *
 */
public class PathfindingQueueElement implements Comparable{
// the "true" nodes that the "true" node that this eleemnt was made from is adjacent with
	private ArrayList<MovementNode> adjacentNodes = new ArrayList<>();//these are used to create queue elements when this elemnet is dequeued
	//the distance (amount of steps) from the start node to this node along the patht hat was used to enqueue this node element
	private final int pathDistance;//note that this is not strictly necessary information because we should be able to find the lentgth of the back by traversing back trhough the path of fromNodes
	//the NODEELEMENT that this node element was enqueued from. eventually once we enqueue the final node, this field can be used to tracethe true path
	private final PathfindingQueueElement fromElement;
	//the "true" BoardNode that this NodeElement was made from. This is set upon construction of an instace of this class. We use this to mark a BoardNode as visited when we dequeue one of these during the runnng of the algorithm
	private final MovementNode myTrueNode; 
	
	
	
	
	/**
	 * constructs the element to be put into the queue
	 * @param adjacentNodes the BoardNodes that are adjacent to the BoardNode that this NodeElement was former from
	 * @param pathDistance the distance so far from the start to this node along the path that this node was reached from
	 * @param fromElement the NodeElement that this node was enqueued from when it was visited
	 */
	PathfindingQueueElement(ArrayList<MovementNode> adjacentNodes, int pathDistance, PathfindingQueueElement fromElement, MovementNode trueNode){
		this.adjacentNodes = adjacentNodes;
		this.pathDistance = pathDistance;
		this.fromElement = fromElement;
		this.myTrueNode = trueNode;
	}

	
	
	
	/**
	 * node elements are sorted on the basis of distance.
	 * Nodes with higher distance values (i.e. further from the start node) are "larger"
	 * returns - 1 if o has greater distance than this
	 * returns 0 if o has equal distance to this
	 * returns 1 if o has lesser distance than this
	 */
		@Override
		public int compareTo(Object o) {
			if(!(o instanceof PathfindingQueueElement)){
				throw new RuntimeException("attempted to compare to an object of different type");
			}
			PathfindingQueueElement other = (PathfindingQueueElement)o;
			
			if(this.pathDistance == other.getPathDistance()){
				return 0;
			}else if(this.pathDistance < other.getPathDistance()){
				return -1;
			}else{
				return 1;
			}
		}

	
	public int getPathDistance(){
		return this.pathDistance;
	}
	
	/**
	 * used to get the movement node that this queue element was created for
	 * @return the node from the graph taht this queue element represents
	 */
	public MovementNode getBoardNode(){
		return this.myTrueNode;
	}
	
	
	public ArrayList<MovementNode> getNeighbours(){
		return this.adjacentNodes;
	}
	
	public PathfindingQueueElement getFromElement(){
		return this.fromElement;
	}
	
}
