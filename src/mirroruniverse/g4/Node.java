package mirroruniverse.g4;

import java.util.ArrayList;

public class Node implements Comparable<Node>{
	
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
	
	private boolean p1HasReached;
	private boolean p2HasReached;
	
	public boolean getP1HasReached(){
		return p1HasReached;
	}
	public boolean getP2HasReached(){
		return p2HasReached;
	}
	

	// The value of this node
	private int value;
	
	// The depth of this node
	private int depth;
	
	// The path taken to get to this node
	private ArrayList<Integer> actionPath;
	
	// The positions of the exits for each player
	private static int p1ExitX = -1000;
	private static int p1ExitY = -1000;
	private static int p2ExitX = -1000;
	private static int p2ExitY = -1000;
	
	// This integer represents the degree away from perfect we can look at
	private static int degree = 0;
	
	public static void incDegree(){
		++degree;
	}
	
	public static int getDegree(){
		return degree;
	}
	
	// Setters for the exit positions
	public static void setExit1(int x, int y){
		p1ExitX = x;
		p1ExitY = y;
	}
	public static void setExit2(int x, int y){
		p2ExitX = x;
		p2ExitY = y;
	}
	
	
	// Constructor, for the first node, have parent = null;
	public Node(int p1X, int p1Y, int p2X, int p2Y, Node parent, int action){
		x1 = p1X;
		y1 = p1Y;
		x2 = p2X;
		y2 = p2Y;
		p1HasReached = false;
		p2HasReached = false;
		
		if(parent != null){
			p1HasReached = parent.p1HasReached || (x1 == p1ExitX && y1 == p1ExitY);
			p2HasReached = parent.p2HasReached || (x2 == p2ExitX && y2 == p2ExitY);
		}
		
		if(parent == null){
			depth = 0;
			actionPath = new ArrayList<Integer>();
		} else {
			depth = parent.getDepth() + 1;
			actionPath = ((ArrayList<Integer>) parent.getActionPath().clone());
			actionPath.add(action);
		}

		// Set the value of this node equal to our heuristic rating for it
		value = this.heuristic();
		
	}

	//Implement our actual heuristic here, right now just takes the total distance both players are away from the goal
	private int heuristic(){
		if(p1ExitX == -1000 || p2ExitX == -1000){
			return 10000 + depth;//Integer.MAX_VALUE;
		}
		int toReturn = Math.max(Math.abs(x1 - p1ExitX), Math.abs(y1 - p1ExitY)) + Math.max(Math.abs(x2 - p2ExitX), Math.abs(y2 - p2ExitY));
		if(x1 == p1ExitX && y1 == p1ExitY && (x2 != p2ExitX || y2 != p2ExitY)){
			if (toReturn > degree) {
				toReturn += 10000;
			}
		} else if (x2 == p2ExitX && y2 == p2ExitY && (x1 != p1ExitX || y1 != p1ExitY)){
			if (toReturn > degree) {
				toReturn += 10000;
			}
		}
		return toReturn + depth;
	}
	
	public static void reRunHeuristic(ArrayList<Node> set){
		System.out.println("Degree is now: " + degree);
		ArrayList<Node> newSet = new ArrayList<Node>();
		for(Node n : set){
//			if(n.getValue() > 9999){
				n.value = n.heuristic();
				newSet.add(n);
//			} else if (n.getValue() <= degree){
//				newSet.add(n);
//			}
		}
		set = newSet;
		System.out.println();
	}


	public int getValue() {
		return value - depth;
	}


	public int getDepth() {
		return depth;
	}


	public ArrayList<Integer> getActionPath() {
		return actionPath;
	}
	
	// Equals function, will return true if the corresponding x and y values are equal
	public boolean equals(Object o){
		if(!(o instanceof Node)){
			return false;
		}
		
		Node n = (Node) o;
		return (n.x1 == x1) && (n.x2 == x2) && (n.y1 == y1) && (n.y2 == y2); 
	}

	@Override
	public int compareTo(Node n) {
		if(this.getValue() > n.getValue()){
			return 1;
		} else if(this.getValue() < n.getValue()){
			return -1;
		} else {
		return 0;
		}
	}
	
	public boolean closeEnough(){
		if(x1 == p1ExitX && y1 == p1ExitY){
			if(getValue() <= degree){
				return p2HasReached;
			}
		} else if(x2 == p2ExitX && y2 == p2ExitY){
			if(getValue() <= degree){
				return p1HasReached;
			}
		}
		return false;
	}
	
	public String toString(){
		return "(P1: " + x1 + "," + y1 + "  P2: " + x2 + "," + y2 + "  Value: " + value + " Depth: " + depth + ")";
	}
}
