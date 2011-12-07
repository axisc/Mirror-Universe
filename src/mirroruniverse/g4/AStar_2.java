package mirroruniverse.g4;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar_2 {

	private static int[][] map1;
	private static int[][] map2;
	Node_2 root;
	PriorityQueue<Node_2> queue;
	ArrayList<Node_2> closed;
	ArrayList<Node_2> nodesToPutOff;
	private int numExitsFound = 0;
	public boolean debugging = false;
	private int maxNodes;
	private boolean increase;
	private boolean addedToDegree = false;
	private static boolean goNextToExit = false;
	private static int nextToVal;
	
	private int numAdded = 0;
	
	public static int[][] getMap1(){
		return map1;
	}
	
	public static int[][] getMap2(){
		return map2;
	}
	
	public AStar_2(int initialX1, int initialY1, int initialX2, int initialY2, int[][] kb_p1, int[][] kb_p2){
		map1 = kb_p1;
		map2 = kb_p2;
		root = new Node_2(initialX1, initialY1, initialX2, initialY2, null, 0);
		
		maxNodes = 0;
		int numOnes = 0;
		int numZeros = 0;
		for(int i = 0; i < kb_p1.length; ++i){
			for(int j = 0; j < kb_p1[0].length; ++j){
				/*if(/*kb_p1[i][j] != 1 && *//*kb_p1[i][j] != -5){
					++maxNodes;
				}*/
				if(kb_p1[i][j] == 1){
					++numOnes;
				}
				if(kb_p1[i][j] == 0){
					++numZeros;
				}
			}
		}
		if(numZeros < 300){
			maxNodes = 8 * (numOnes + numZeros);
		} else {
			maxNodes = 4 * numZeros;
		}
		
		queue = new PriorityQueue<Node_2>();
		closed = new ArrayList<Node_2>();
		nodesToPutOff = new ArrayList<Node_2>();
		if(debugging){
			prettyPrint(root);
		}
		increase = false;
		nextToVal = 3;
	}
	
	public static void setNextToVal(int n){
		nextToVal = n;
	}
	
	public static void invertGoNextToExit(){
		goNextToExit = !goNextToExit;
	}
	
	public static void setGoNextToExit(boolean b){
		goNextToExit = b;
	}
	
	public void setExit1(int x, int y){
		Node_2.setExit1(x, y);
		++numExitsFound;
		if(numExitsFound == 2){
			bothExits();
		}
	}
	
	public void setExit2(int x, int y){
		Node_2.setExit2(x, y);
		++numExitsFound;
		if(numExitsFound == 2){
			bothExits();
		}
	}
	
	private void bothExits(){
		root.resetHeuristic();
		queue.add(root);
		if(!exitTogether()){
			//addedToDegree = true;
			maxNodes /= 2;
			//Node_2.incDegree();
		}
	}
	
	private boolean exitTogether(){
		int exit1X = Node_2.getExit1X();
		int exit2X = Node_2.getExit2X();
		int exit1Y = Node_2.getExit1Y();
		int exit2Y = Node_2.getExit2Y();
		for(int y = -1; y <= 1; ++y){
			for(int x = -1; x <= 1; ++x){
				if(y == 0 && x == 0){
					continue;
				}
				if(map1[exit1Y + y][exit1X + x] == 0 && map2[exit2Y + y][exit2X + x] == 0){
					return true;
				}
			}
		}
		return false;
	}
	
	public void exitsFound(){
		if (increase) {
			if(addedToDegree){
				addedToDegree = false;
			} else {
				Node_2.incDegree();
			}
			Node_2.reRunHeuristic(nodesToPutOff);
			PriorityQueue<Node_2> tempQ = new PriorityQueue<Node_2>(nodesToPutOff);
			queue = tempQ;
			if (queue.isEmpty()) {
				System.out.println("Increasing limit");
				maxNodes *= 2;
				Node_2.resetDegree();
				Node_2.reRunHeuristic(closed);
				queue.addAll(closed);
				queue.add(root);
				closed.clear();
			}
			nodesToPutOff.clear();
		} else {
			Node_2.addDiff(closed);
			PriorityQueue<Node_2> tempQ = new PriorityQueue<Node_2>(closed);
			queue = tempQ;
			closed.clear();
		}
		increase = !increase;
		//closed.clear();
	}
	
	public Node_2 findZeroPath(){
		if(!exitTogether()){
			return null;
		}
		if(!closed.isEmpty()){
			queue.addAll(closed);
		}
		while(!queue.isEmpty() && queue.peek().getValue() != 0 && !queue.peek().closeEnough()){
			if(numAdded > maxNodes){
				queue.clear();
				break;
			}
			if(queue.peek().getValue() > 10000){
				nodesToPutOff.add(queue.poll());
				continue;
			}
			ArrayList<Node_2> nexts = successors(queue.poll());
			queue.addAll(nexts);
		}
		Node_2 toReturn = null;
		if(!queue.isEmpty()){
			toReturn = queue.poll();
		}
		queue.clear();
		return toReturn;
	}
	
	public ArrayList<Integer> startFinding(){
		if(!closed.isEmpty()){
			queue.addAll(closed);
		}
		return findPath();
	}
	
	public ArrayList<Integer> findPath(){
		while(!queue.isEmpty() && queue.peek().getValue() != 0 && !queue.peek().closeEnough()){
			if(numAdded > maxNodes){
				queue.clear();
				break;
			}
			if(goNextToExit && queue.peek().getRealValue() == nextToVal && !(queue.peek().getP1HasReached() || queue.peek().getP2HasReached())){
				//queue.clear();
				break;
			}
			if (debugging) {
				System.out.println(numAdded + " " + queue.size());
				//System.out.println("Expanding:" + queue.peek().getValue());
				System.out.println("Looking at:");
				System.out.println(queue.peek());//.getActionPath());
				//prettyPrint(queue.peek());
			}
			if(queue.peek().getValue() > 10000){
				nodesToPutOff.add(queue.poll());
				continue;
			}
			ArrayList<Node_2> nexts = successors(queue.poll());
			queue.addAll(nexts);
		}
		System.out.println("Done");
		if(queue.isEmpty()){
			numAdded = 0;
			System.out.println("Empty :(");
			increase = true;
			exitsFound();
			return findPath();
		} else {
			System.out.println("Found :)");
			System.out.println(queue.peek());
			System.out.println(queue.peek().getActionPath());
			closed.clear();
			nodesToPutOff.clear();
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
	
	public void prettyPrint(Node_2 n){
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
	private ArrayList<Node_2> successors(Node_2 n){
		closed.add(n);
		int x1;
		int x2;
		int y1;
		int y2;
		int action = 0;
		//int[] indexOfAction = {6,5,4,7,0,3,8,1,2};
		int[] indexOfAction = {4,5,6,3,0,7,2,1,8};
		ArrayList<Node_2> nexts = new ArrayList<Node_2>();
		
		for(int xChange = -1; xChange < 2; ++xChange){
			for(int yChange = -1; yChange < 2; ++yChange){
				
				// Dont want to add the current Node_2 to the path
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
				if(n.getP1HasReached()){
					x1 = n.getX1();
					y1 = n.getY1();
				}
				if(n.getP2HasReached()){
					x2 = n.getX2();
					y2 = n.getY2();
				}
				try {
					if(map1[y1][x1] == -5 || map2[y2][x2] == -5){
						++action;
						continue;
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
				Node_2 toAdd = new Node_2(x1, y1, x2, y2, n, indexOfAction[action]);
				//Node_2.addPathCost(toAdd, 1, map1, map2);
				if(x1 == 56 && y1 == 103 && x2 == 125 && y2 == 126){
					System.out.println();
				}
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
	
	private boolean shouldIAdd(Node_2 n){
		for(Node_2 q: queue){
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
		
		for(Node_2 c: closed){
			if(n.equals(c)){
				//if(n.getValue() + n.getDepth() < c.getValue() + c.getDepth()){
					//closed.remove(c);
					//return true;
				//} else {
					return false;
				//}
			}
		}

		if(n.getValue() > 10000){
			for(Node_2 o : nodesToPutOff){
				if (n.equals(o)){
					if(n.getDepth() < o.getDepth()){
						nodesToPutOff.remove(o);
						break;
					}
					return false;
				}
			}
			++numAdded;
			nodesToPutOff.add(n);
			return false;
		}
		return true;
	}
}
