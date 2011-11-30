package mirroruniverse.g1_final;

import java.util.ArrayList;

public class Node {	
	int x;
	int y;
	
	public Node (int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public static boolean contained(ArrayList<Node> astarPath, Node m) {
		
		for(Node n : astarPath)
			if(m.getX() == n.getX() && m.getY() == n.getY())
				return true;
		return false;
	
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}
