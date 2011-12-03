package mirroruniverse.g4;

import java.util.ArrayList;

public class Node_Single implements Comparable<Node_Single> {

	// The two players' positions
	private int x1;
	private int y1;

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	Node_Single parent;

	// The value of this node
	private double value;

	// The depth of this node
	private int depth;

	// The positions of the exits for each player
	private int exitX;
	private int exitY;
	
	// The path taken to get to this node
	private ArrayList<Integer> actionPath;

	public ArrayList<Integer> getActionPath() {
		return actionPath;
	}

	// Constructor, do not use for the root node.
	public Node_Single(int p1X, int p1Y, Node_Single expanded, int action){
		x1 = p1X;
		y1 = p1Y;
		parent = expanded;
		exitX = parent.exitX;
		exitY = parent.exitY;
		depth = parent.getDepth() + 1;
		

		actionPath = (ArrayList<Integer>) parent.getActionPath().clone();
		actionPath.add(action);

		// Set the value of this node equal to our heuristic rating for it
		value = this.heuristic();
	}

	// Constructor for the first node
	public Node_Single(int p1X, int p1Y, int xExit, int yExit) {
		x1 = p1X;
		y1 = p1Y;
		exitX = xExit;
		exitY = yExit;

		depth = 0;

		actionPath = new ArrayList<Integer>();

		// Set the value of this node equal to our heuristic rating for it
		value = this.heuristic();
	}

	// Implement our actual heuristic here, right now just takes the total
	// distance both players are away from the goal
	private double heuristic() {
		int distance = Math.max(Math.abs(x1 - exitX), Math.abs(y1 - exitY));
		return distance + depth;
	}

	public double getValue() {
		return value;
	}

	public int getDepth() {
		return depth;
	}

	// Equals function, will return true if the corresponding x and y values are
	// equal
	public boolean equals(Object o) {
		if (!(o instanceof Node_Single)) {
			return false;
		}

		Node_Single n = (Node_Single) o;
		return (n.x1 == x1) && (n.y1 == y1);
	}

	@Override
	public int compareTo(Node_Single n) {
		if (this.getValue() > n.getValue()) {
			return 1;
		} else if (this.getValue() < n.getValue()) {
			return -1;
		} else if (this.getDepth() < n.getDepth()) {
			return 1;
		} else if (this.getDepth() > n.getDepth()) {
			return -1;
		} else {
			return 0;
		}
	}

	public String toString() {
		return "(P1: " + x1 + "," + y1 + "   Value: " + value + " Depth: "
				+ depth + ")";
	}
}
