package mirroruniverse.g4;

import java.util.ArrayList;
import java.util.PriorityQueue;

import mirroruniverse.sim.MUMap;

public class AStar {

	private int[][] map1;
	private int[][] map2;
	Node root;
	PriorityQueue<Node> queue;
	ArrayList<Node> closed;
	private int numExitsFound = 0;
	public boolean debugging = false;
	
	private int numAdded = 0;
	
	public AStar(int initialX1, int initialY1, int initialX2, int initialY2, int[][] kb_p1, int[][] kb_p2){
		root = new Node(initialX1, initialY1, initialX2, initialY2, null, 0);
		map1 = kb_p1;
		map2 = kb_p2;
		queue = new PriorityQueue<Node>();
		queue.add(root);
		closed = new ArrayList<Node>();
		closed.add(root);
		if(debugging){
			prettyPrint(root);
		}
	}
	
	public void setExit1(int x, int y){
		Node.setExit1(x, y);
		++numExitsFound;
		if(numExitsFound == 2){
			exitsFound();
		}
	}
	
	public void setExit2(int x, int y){
		Node.setExit2(x, y);
		++numExitsFound;
		if(numExitsFound == 2){
			exitsFound();
		}
	}
	
	public void exitsFound(){
		Node.reRunHeuristic(closed);
		PriorityQueue<Node> tempQ = new PriorityQueue<Node>(closed);
		queue = tempQ;
		closed.clear();
	}
	
	public ArrayList<Integer> findPath(){
		while(!queue.isEmpty() && queue.peek().getValue() != 0 && !queue.peek().closeEnough()){
			if (debugging) {
				System.out.println(numAdded + " " + queue.size());
				//System.out.println("Expanding:" + queue.peek().getValue());
				System.out.println("Looking at:");
				System.out.println(queue.peek().getActionPath());
				prettyPrint(queue.peek());
			}
			ArrayList<Node> nexts = successors(queue.poll());
			queue.addAll(nexts);
		}
		System.out.println("Done");
		if(queue.isEmpty()){
			numAdded = 0;
			System.out.println("Empty :(");
			Node.incDegree();
			exitsFound();
			return findPath();
		} else {
			System.out.println("Found :)");
			System.out.println(queue.peek().getActionPath());
			return queue.peek().getActionPath();
		}
	}
	
	/*public static void main(String[] args){
		int[][] temp = {{0,0,0,0,0},{0,0,0,0,0},{1,0,0,0,1},{1,0,1,1,1},{0,0,0,0,0}};
		AStar a = new AStar(1, 0, 2, 0, temp, temp);
		
		a.setExit1(1, 1);
		a.setExit2(4, 4);
		
		System.out.println(a.findPath());
	}*/
	
	public void prettyPrint(Node n){
		for(int y = 0; y < map1.length; ++y){
			for(int x = 0; x < map1[0].length; ++x){
				if(x == n.getX1() && y == n.getY1()){
					System.out.print("3 ");
				} else {
					System.out.print(map1[y][x] + " ");
				}
			}
			System.out.println();
		}
		System.out.println("---------");
		for(int y = 0; y < map1.length; ++y){
			for(int x = 0; x < map1[0].length; ++x){
				if(x == n.getX2() && y == n.getY2()){
					System.out.print("3 ");
				} else {
					System.out.print(map2[y][x] + " ");
				}
			}
			System.out.println();
		}
		System.out.println("+++++++++++++");
	}
	
	// Will generate the possible next moves 
	private ArrayList<Node> successors(Node n){
		closed.add(n);
		int x1;
		int x2;
		int y1;
		int y2;
		int action = 0;
		//int[] indexOfAction = {6,5,4,7,0,3,8,1,2};
		int[] indexOfAction = {4,5,6,3,0,7,2,1,8};
		ArrayList<Node> nexts = new ArrayList<Node>();
		
		for(int xChange = -1; xChange < 2; ++xChange){
			for(int yChange = -1; yChange < 2; ++yChange){
				
				// Dont want to add the current node to the path
				if(xChange == 0 && yChange == 0){
					++action;
					continue;
				}
				
				x1 = n.getX1() + xChange;
				y1 = n.getY1() + yChange;
				x2 = n.getX2() + xChange;
				y2 = n.getY2() + yChange;
				
				try {
					if (map1[y1][x1] == 1) {
						x1 -= xChange;
						y1 -= yChange;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					x1 -= xChange;
					y1 -= yChange;
				}
				try {
					if (map2[y2][x2] == 1) {
						x2 -= xChange;
						y2 -= yChange;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					x2 -= xChange;
					y2 -= yChange;
				}
				if(x1 == 1 && y1 == 1 && x2 == 4 && y2 == 4)
					System.out.println();
				Node toAdd = new Node(x1, y1, x2, y2, n, indexOfAction[action]);
				
				if(!n.equals(toAdd) && shouldIAdd(toAdd)){
					nexts.add(toAdd);
					++numAdded;
					if(debugging){
						System.out.println(indexOfAction[action]);
						prettyPrint(toAdd);
					}
				}
				
				
				++action;
			}
		}
		
		return nexts;
	}
	
	private boolean shouldIAdd(Node n){
		for(Node q: queue){
			if(n.equals(q)){
				if(n.getDepth() < q.getDepth()){
					queue.remove(q);
					--numAdded;
					return true;
				} else {
					return false;
				}
			}
		}
		
		for(Node c: closed){
			if(n.equals(c)){
				//if(n.getValue() + n.getDepth() < c.getValue() + c.getDepth()){
					//closed.remove(c);
					//return true;
				//} else {
					return false;
				//}
			}
		}
		return true;
	}
}
