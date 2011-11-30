package mirroruniverse.g4;

import java.util.ArrayList;

public class Node_2 implements Comparable<Node_2>{
	
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
	private double value;
	
	// The depth of this node
	private int depth;
	
	// The path taken to get to this node
	private ArrayList<Integer> actionPath;
	
	// The positions of the exits for each player
	private static int p1ExitX = -1000;
	private static int p1ExitY = -1000;
	private static int p2ExitX = -1000;
	private static int p2ExitY = -1000;
	
	private Node_2 parent;
	
	// This integer represents the degree away from perfect we can look at
	private static int degree = 0;
	
	private int selfDegree;
	
	public int getSelfDegree(){
		return selfDegree;
	}
	
	public static void incDegree(){
		++degree;
	}
	
	public static void resetDegree(){
		degree = 0;
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
	public Node_2(int p1X, int p1Y, int p2X, int p2Y, Node_2 expanded, int action){
		x1 = p1X;
		y1 = p1Y;
		x2 = p2X;
		y2 = p2Y;
		p1HasReached = false;
		p2HasReached = false;
		parent = expanded;
		
		updateSelfDegree();
		
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
	
	private void updateSelfDegree(){
		if(parent != null){
			p1HasReached = parent.p1HasReached || (x1 == p1ExitX && y1 == p1ExitY);
			p2HasReached = parent.p2HasReached || (x2 == p2ExitX && y2 == p2ExitY);
		}
		
		if((!p1HasReached || !p2HasReached) && (p1HasReached || p2HasReached)){
			selfDegree = parent.selfDegree + 1;
		} else {
			selfDegree = 0;
		}
	}

	//Implement our actual heuristic here, right now just takes the total distance both players are away from the goal
	private double heuristic(){
		if(p1ExitX == -1000 || p2ExitX == -1000){
			return 10000 + depth;//Integer.MAX_VALUE;
		}
		
		int diffExitX = p1ExitX - p2ExitX;
		int diffPX = x1-x2;
		int diffExitY = p1ExitY - p2ExitY;
		int diffPY = y1-y2;
		
		int diff = Math.abs(diffExitX - diffPX) + Math.abs(diffExitY - diffPY);
		
		int p1Distance = Math.max(Math.abs(x1 - p1ExitX), Math.abs(y1 - p1ExitY));
		int p2Distance = Math.max(Math.abs(x2 - p2ExitX), Math.abs(y2 - p2ExitY));
		int toReturn = /*diff + */selfDegree + p1Distance + p2Distance;// + Math.max(p1Distance, p2Distance);
		
		if(selfDegree > degree){
			toReturn += 10000;
		}
		/*if(x1 == p1ExitX && y1 == p1ExitY && (x2 != p2ExitX || y2 != p2ExitY)){
			if (selfDegree > degree) {
				toReturn += 10000;
			}
		} else if (x2 == p2ExitX && y2 == p2ExitY && (x1 != p1ExitX || y1 != p1ExitY)){
			if (selfDegree > degree) {
				toReturn += 10000;
			}
		}*/
		return toReturn + (0.8 * depth);
	}
	
	public static void reRunHeuristic(ArrayList<Node_2> set){
		System.out.println("Degree is now: " + degree);
		ArrayList<Node_2> newSet = new ArrayList<Node_2>();
		for(Node_2 n : set){
//			if(n.getValue() > 9999){
				n.value = n.heuristic();
				n.updateSelfDegree();
				newSet.add(n);
//			} else if (n.getValue() <= degree){
//				newSet.add(n);
//			}
		}
		set = newSet;
		System.out.println();
	}

	public static void addPathCost(Node_2 n, int playerNum, int[][] map1, int[][] map2){
		int x;
		int y;
		int exitX;
		int exitY;
		int[][] map;
		if(playerNum == 1){
			x = n.getX1();
			y = n.getY1();
			exitX = p1ExitX;
			exitY = p1ExitY;
			map = map1;
		} else {
			x = n.getX2();
			y = n.getY2();
			exitX = p2ExitX;
			exitY = p2ExitY;
			map = map2;
		}
		int toAdd = 0;
		while(x != exitX && y != exitY){
			if (x != exitX){
				if(exitX > x){
					++x;
				} else {
					--x;
				}
			}
			if (y != exitY){
				if(exitY > y){
					++y;
				} else {
					--y;
				}
			}
			if(map[y][x] != 0){
				++toAdd;
			}
		}
		n.value += toAdd;
		if(playerNum == 1){
			addPathCost(n, playerNum+1, map1, map2);
		}
	}
	
	public double getValue() {
		return value;// - depth;
	}


	public int getDepth() {
		return depth;
	}


	public ArrayList<Integer> getActionPath() {
		return actionPath;
	}
	
	// Equals function, will return true if the corresponding x and y values are equal
	public boolean equals(Object o){
		if(!(o instanceof Node_2)){
			return false;
		}
		
		Node_2 n = (Node_2) o;
		return (n.x1 == x1) && (n.x2 == x2) && (n.y1 == y1) && (n.y2 == y2); 
	}

	@Override
	public int compareTo(Node_2 n) {
		if(this.getValue() > n.getValue()){
			return 1;
		} else if(this.getValue() < n.getValue()){
			return -1;
		} else if(this.getDepth() < n.getDepth()){
			return 1;
		} else if(this.getDepth() > n.getDepth()){
			return -1;
		} else {
		return 0;
		}
	}
	
	public boolean closeEnough(){
		if(x1 == p1ExitX && y1 == p1ExitY){
			if(selfDegree <= degree){
				return p2HasReached;
			}
		} else if(x2 == p2ExitX && y2 == p2ExitY){
			if(selfDegree <= degree){
				return p1HasReached;
			}
		}
		return false;
	}
	
	public String toString(){
		return "(P1: " + x1 + "," + y1 + "  P2: " + x2 + "," + y2 + "  Value: " + value + " Depth: " + depth + ")";
	}
}
