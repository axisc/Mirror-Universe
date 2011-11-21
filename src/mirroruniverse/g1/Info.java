package mirroruniverse.g1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import mirroruniverse.g1.Direction;

public class Info {

	//Local Views for both the boards
	static int aintLocalViewL [][] ;
	static int aintLocalViewR [][] ;

	
	//Global Views for both the boards
		static int aintGlobalViewL [][];
		static int aintGlobalViewR [][];
		
		static ArrayList<LinkedList<Node>> path = new ArrayList<LinkedList<Node>>();
		static ArrayList<Node> searchGraph = new ArrayList<Node>();
		static ArrayList<Node> open = new ArrayList<Node>();
		static ArrayList<Node> closed = new ArrayList<Node>();
		static ArrayList <Node> m = new ArrayList<Node>();
		static ArrayList <Node> came_from = new ArrayList<Node>();
	/*
	 * Default Constructor
	 */
		public Info (){
			aintGlobalViewL = new int [200][200];
			aintGlobalViewR = new int [200][200];
			
			aintLocalViewL = new int [3][3];
			aintLocalViewR = new int [3][3];
		}
		
	/*
	 * This method checks if the board has been explored
	 * completely. 
	 * This can be checked by hunting for an array of size 100x100
	 * that doesn't contain a value '4' for any of the squares.
	 */
	public boolean isExplorationComplete(char side){
		
		return false;
	}
	
	
	/*
	 * This method checks if the exit has been spotted
	 * in the local view of the player specified.
	 * It takes a character 'side' as the parameter.
	 */
	public boolean isExitSpotted (char side){
		int tempLocalView[][] = new int [3][3];
		if (side =='l'){
			tempLocalView = aintLocalViewL;
		}else
			if (side == 'r'){
				tempLocalView = aintLocalViewR;
			}
		for (int i =0; i<tempLocalView.length ;i++)
			for(int j=0;j<tempLocalView[i].length ;j++)
				if (tempLocalView[i][j] == 2)
					return true;
		return false;
	}
	
	/*
	 * This method updates the Global View of each player.
	 * It takes as parameter the side, and the current view
	 * that needs to be added to the Global View
	 */
	public void updateGlobalView (char side, int [][]view){
		int tempGlobalView [][]= new int [3][3];
		
		if (side == 'r'){
			this.aintGlobalViewR= tempGlobalView;
		}
		else if (side == 'l'){
			this.aintGlobalViewL = tempGlobalView;
		}
		
	}
	
	/*
	 * This method updates the Local View of each player.
	 * It takes as parameter the side, and the current view 
	 * that needs to be updated.
	 */
	public void updateLocalView (char side, int [][] view){
		
		int tempLocalView [][] = new int [3][3];
		
		if (side == 'r'){
			this.aintLocalViewR = tempLocalView;
		}
		else if (side == 'l'){
			this.aintLocalViewL = tempLocalView;
		}
	}
	
	public Node scanMap (int globalView[][], int x){
		int i=0,j=0;
		for (i= 0;i<globalView.length ; i++)
			for (j=0 ; j<globalView[i].length ; j++)
				{
					if (globalView [i][j] == x)
					break;
					else
						continue;
				}
		Node ret = new Node(i,j);
		return ret;
	}
	
	public ArrayList<Node> generateSuccessors ( Node n){
		ArrayList<Node> successors = new ArrayList<Node>();
		
		int i=0,j=0;
		for (i =-1 ; i <= 1; i++ )
			for ( j=-1 ; j<=1 ;j++){
				Node temp = new Node(n.getX()+i, n.getY()+j);
				if (n != temp)
					successors.add(temp);
			}
		return successors;
				
		
		
	}
	
	/*
	 * We call this method once the exits on both the maps 
	 * have been spotted.
	 * 
	 * Pseudo code
	 * 
Expand node n, generating the set M, of its successors that are not already ancestors of n in G. Install these members of M as successors of n in G.
Establish a pointer to n from each of those members of M that were not already in G (i.e., not already on either OPEN or CLOSED). Add these members of M to OPEN. For each member, m, of M that was already on OPEN or CLOSED, redirect its pointer to n if the best path to m found so far is through n. For each member of M already on CLOSED, redirect the pointers of each of its descendants in G so that they point backward along the best paths found so far to these descendants.
Reorder the list OPEN in order of increasting f values. (Ties among minimal f values are resolved in favor of the deepest node in the search tree.)
Go to Step 3.
	 */
	public ArrayList<Node> aStar(int [][] globalView){

		/*
		 * Initialize the List of nodes.
		 */
		
		/*
		 * Add source node to Open List.
		 */
		open.add(scanMap(globalView, MapData.playerPosition));
		searchGraph.add(open.get(0));
		/*
		 * If OPEN is empty, exit with failure.
		 */
		if (open.isEmpty()) return null;
		/*
		 *Select the first node on OPEN, remove it from OPEN, and put it on CLOSED. Called this node n. 
		 */
		Node n = open.remove(0);
		closed.add(n);
		/*
		 * If n is a goal node, exit successfully with the solution obtained by tracing a path along the 
		 * pointers from n to no in G. (The pointers define a search tree and are established in Step 7.)
		 */
		if ( n == scanMap(globalView, MapData.exit)) return searchGraph;
		else {
			ArrayList<Node > successors = generateSuccessors (n);
			for(Node temp : successors){
				if(!open.contains(temp)) {
					if(!closed.contains(temp))
						open.add(temp);
					else continue;}
				else {
					
				}
			}
			
		}
		return null;	
		}
	

	public double hfunc (Node start, Node end, int globalView[][]){
		Double hValue = (double) (Math.abs(start.getX()-end.getX())+ Math.abs(start.getY()-end.getY()));
		if(globalView[start.getX()][start.getY()] == 1)
			hValue += 1000;
		return hValue;
	}
	public double gfunc(Node start, Node end){
		Double gValue = (double) (Math.abs(start.getX()-end.getX())+ Math.abs(start.getY()-end.getY()));
		return gValue;
	}
	

	public LinkedList<Node> aStar2 (int [][] globalView ){
		
		Node start = scanMap(globalView, MapData.playerPosition);
		Node exit = scanMap(globalView, MapData.exit);
		
		open.add(start);
		
		ArrayList<Node> succesors = new ArrayList<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		Map<Node, Double> tentCost = new HashMap<Node, Double>();
		
		path.add(start);
		
		do{
			double min = Double.MAX_VALUE;
			Node minCostNode = new Node(start.getX(), start.getY());
			Node tempStart = new Node(start.getX(), start.getY());
			
			for(Node m : open){
				if(min > (gfunc(m, exit)+ hfunc(m,exit,globalView)))
					minCostNode = m;
				open.remove(m);
				closed.add(m);
				tempStart = m;
			}
	
			boolean isBetter = true;
			
			if(tempStart == exit)
				return path;
	
			else {
				succesors = generateSuccessors(tempStart);
			
				for(Node m : succesors) {
					if(!closed.contains(m)){
						continue;
					}
					if(!open.contains(m)) {
						open.add(m);
						tentCost.put(m, (gfunc(tempStart,exit)+ gfunc(tempStart,m)));
					}
					if(tentCost.get(m) < gfunc(m,exit)) {
						isBetter = true;
					}
				
					else isBetter = false;
					
					if(isBetter) {
						path.add(m);
						
					}
						
				}
			}
				/*	
				min = Double.MAX_VALUE;
			
				for(Node n : succesors){
					double fValue = gfunc(tempStart, exit) + hfunc(tempStart, exit, globalView);
					if(min > fValue) {
						min = fValue;
						minCostNode = n;
				}	
			}	
				
			path.add(minCostNode);
		*/		
		}while(!open.isEmpty());
		
		return path;
	}

}