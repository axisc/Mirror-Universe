package mirroruniverse.g1_final;

import java.util.ArrayList;

public class Node_NonExit implements Comparable<Node_NonExit> {

	// The two players' positions
	private int x1;
	private int y1;
	private int x2;
	private int y2;

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}



	Node_NonExit parent;

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
	public Node_NonExit(int p1X, int p1Y, int p2X, int p2Y, Node_NonExit expanded, int action){
		x1 = p1X;
		y1 = p1Y;
		x2 = p2X;
		y2 = p2Y;
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
	public Node_NonExit(int p1X, int p1Y, int p2X, int p2Y, int xExit, int yExit) {
		x1 = p1X;
		y1 = p1Y;
		x2 = p2X;
		y2 = p2Y;
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
		if (!(o instanceof Node_NonExit)) {
			return false;
		}

		Node_NonExit n = (Node_NonExit) o;
		return (n.x1 == x1) && (n.y1 == y1) && (n.x2 == x2) && (n.y2 == y2);
	}

	@Override
	public int compareTo(Node_NonExit n) {
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
		return "(P1: " + x1 + "," + y1 + ". P2: " + x2 + "," + y2 + "   Value: " + value + " Depth: "
				+ depth + ")";
	}
}
