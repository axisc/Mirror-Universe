package mirroruniverse.g1_final;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar_NonExit {

	private int[][] map1, map2;
	Node_NonExit root;
	PriorityQueue<Node_NonExit> queue;
	ArrayList<Node_NonExit> closed;
	public boolean debugging = false;
	boolean trackingP2 = true;
	
	public AStar_NonExit(int initialX1, int initialY1, int exitX, int exitY, int initialX2, int initialY2, int[][] kb1, int[][] kb2){
		root = new Node_NonExit(initialX1, initialY1, initialX2, initialY2, exitX, exitY);
		map1 = kb1;
		map2 = kb2;
		
		queue = new PriorityQueue<Node_NonExit>();
		queue.add(root);
		closed = new ArrayList<Node_NonExit>();
	}
	
	/*public void exitsFound(){
		if (increase) {
			Node_NonExit.incDegree();
			Node_NonExit.reRunHeuristic(nodesToPutOff);
			PriorityQueue<Node_NonExit> tempQ = new PriorityQueue<Node_NonExit>(nodesToPutOff);
			queue = tempQ;
			if (queue.isEmpty()) {
				System.out.println("Increasing limit");
				maxNodes *= 2;
				Node_NonExit.resetDegree();
				Node_NonExit.reRunHeuristic(closed);
				queue.addAll(closed);
				queue.add(root);
				closed.clear();
			}
			nodesToPutOff.clear();
		} else {
			Node_NonExit.addDiff(closed);
			PriorityQueue<Node_NonExit> tempQ = new PriorityQueue<Node_NonExit>(closed);
			queue = tempQ;
			closed.clear();
		}
		increase = !increase;
		//closed.clear();
	}*/
	
//	public Node_NonExit findPath(){
//		while(!queue.isEmpty() && queue.peek().getValue() != queue.peek().getDepth()){
//			ArrayList<Node_NonExit> nexts = nonExitSuccessors(queue.poll());
//			queue.addAll(nexts);
//		}
//		//System.out.println("Done");
//		if(queue.isEmpty()){
//			//System.out.println("Empty :(");
//			//exitsFound();
//			return null;
//		} else {
//			//System.out.println("Found :)");
//			//System.out.println(queue.peek());
//			return queue.peek();
//		}
//	}
	
	// Will generate the possible next moves 
//	private ArrayList<Node_NonExit> successors(Node_NonExit n){
//		closed.add(n);
//		int x1;
//		int y1;
//		int x2;
//		int y2;
//		int action = 0;
//		//int[] indexOfAction = {6,5,4,7,0,3,8,1,2};
//		int[] indexOfAction = {4,5,6,3,0,7,2,1,8};
//		ArrayList<Node_NonExit> nexts = new ArrayList<Node_NonExit>();
//		
//		for(int xChange = -1; xChange < 2; ++xChange){
//			for(int yChange = -1; yChange < 2; ++yChange){
//				
//				// Dont want to add the current Node_NonExit to the path
//				if(xChange == 0 && yChange == 0){
//					++action;
//					continue;
//				}
//				
//				x1 = n.getX1() + xChange;
//				y1 = n.getY1() + yChange;
//				x2 = n.getX2() + xChange;
//				y2 = n.getY2() + yChange;
//				
//				try {
//					if (map1[y1][x1] == 1 || map1[y1][x1] == 4) {
//						x1 -= xChange;
//						y1 -= yChange;
//						x2 -= xChange;
//						y2 -= yChange;
//					}
//				} catch (ArrayIndexOutOfBoundsException e) {
//					x1 -= xChange;
//					y1 -= yChange;
//				}
//				Node_NonExit toAdd = new Node_NonExit(x1, y1, n, indexOfAction[action]);
//				
//				if(!n.equals(toAdd) && shouldIAdd(toAdd)){
//					nexts.add(toAdd);
//				}
//				++action;
//			}
//		}
//		
//		return nexts;
//	}
	
	public Node_NonExit findNonExitPath(){
		while(!queue.isEmpty() && queue.peek().getValue() != queue.peek().getDepth()){
			ArrayList<Node_NonExit> nexts = nonExitSuccessors(queue.poll());
			queue.addAll(nexts);
		}
		//System.out.println("Done");
		if(queue.isEmpty()){
			//System.out.println("Empty :(");
			//exitsFound();
			return null;
		} else {
			//System.out.println("Found :)");
			//System.out.println(queue.peek());
			return queue.peek();
		}
	}
	
	// Will generate the possible next moves 
	private ArrayList<Node_NonExit> nonExitSuccessors(Node_NonExit n){
		closed.add(n);
		int x1;
		int y1;
		int x2;
		int y2;
		int action = 0;
		//int[] indexOfAction = {6,5,4,7,0,3,8,1,2};
		int[] indexOfAction = {4,5,6,3,0,7,2,1,8};
		ArrayList<Node_NonExit> nexts = new ArrayList<Node_NonExit>();
		
		for(int xChange = -1; xChange < 2; ++xChange){
			for(int yChange = -1; yChange < 2; ++yChange){
				
				// Dont want to add the current Node_NonExit to the path
				if(xChange == 0 && yChange == 0){
					++action;
					continue;
				}
				
				x1 = n.getX1() + xChange;
				y1 = n.getY1() + yChange;
				x2 = n.getX2() + xChange;
				y2 = n.getY2() + yChange;
				if(!trackingP2){
					x2 = Integer.MAX_VALUE;
					y2 = Integer.MAX_VALUE;
				}
				
				try{
					if(trackingP2 && map2[y2][x2] == 4){
						trackingP2 = false;
						x2 = Integer.MAX_VALUE;
						y2 = Integer.MAX_VALUE;
					}
						
				} catch (ArrayIndexOutOfBoundsException e) {
					trackingP2 = false;
					x2 = Integer.MAX_VALUE;
					y2 = Integer.MAX_VALUE;
				}
				
				
				try {
					
					if(trackingP2){
						if (map1[y1][x1] == 1 || map1[y1][x1] == 4 || map1[y1][x1] == 2 ||  map2[y2][x2] == 2) {
							x1 -= xChange;
							y1 -= yChange;
							x2 -= xChange;
							y2 -= yChange;
						}
						else if(map2[y2][x2] == 1){
							x2 -= xChange;
							y2 -= yChange;
						}
					}
					else{
						if (map1[y1][x1] == 1 || map1[y1][x1] == 4 || map1[y1][x1] == 2) {
							x1 -= xChange;
							y1 -= yChange;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					x1 -= xChange;
					y1 -= yChange;
					if(trackingP2){
						x2 -= xChange;
						y2 -= yChange;
					}
				}
				
				Node_NonExit toAdd = new Node_NonExit(x1, y1, x2, y2, n, indexOfAction[action]);
				
				if(!n.equals(toAdd) && shouldIAdd(toAdd)){
					nexts.add(toAdd);
				}
				++action;
			}
		}
		
		return nexts;
	}
	
	private boolean shouldIAdd(Node_NonExit n){
		for(Node_NonExit q: queue){
			if(n.equals(q)){
				if(n.getDepth() < q.getDepth()){
					queue.remove(q);
					return true;
				} else {
					return false;
				}
			}
		}
		
		for(Node_NonExit c: closed){
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
