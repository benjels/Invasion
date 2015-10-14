package gamelogic;
/**
 * a node in the graph that is used for independent actor movement that represents a "place"/location in the game world that cannot be moved over
 * @author brownmax1
 *
 */
public class ImpassableNode extends MovementNode {

	public ImpassableNode(int x, int y) {
		super(x, y);
	}

}
