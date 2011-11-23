package mirroruniverse.g1;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import mirroruniverse.g1.Direction;
import mirroruniverse.sim.MUMapConfig;

public class Info {

	//Local Views for both the boards
	static int aintLocalViewL [][] ;
	static int aintLocalViewR [][] ;


	//Global Views for both the boards
		static int aintGlobalViewL [][];
		static int aintGlobalViewR [][];

		static private int currLX = 0, currLY = 0, currRX = 0, currRY = 0;

		static ArrayList<LinkedList<Node>> path = new ArrayList<LinkedList<Node>>();
		static ArrayList<Node> searchGraph = new ArrayList<Node>();
		static ArrayList<Node> open = new ArrayList<Node>();
		static ArrayList<Node> closed = new ArrayList<Node>();
		static ArrayList <Node> m = new ArrayList<Node>();
		static ArrayList <Node> came_from = new ArrayList<Node>();
		public static final int[][] aintDToM = { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 },  { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
	/*
	 * Default Constructor
	 */
		public Info(){

		}

		public static void initInfo(int visibilityRadiusL, int visibilityRadiusR){
			aintGlobalViewL = new int [200][200];
			aintGlobalViewR = new int [200][200];

			for ( int i=0 ; i<200 ;i++)
				for (int j=0; j<200 ;j++)
					aintGlobalViewL [i][j] = aintGlobalViewR[i][j] = MapData.unknown;

			aintLocalViewL = new int [visibilityRadiusL][visibilityRadiusL];
			aintLocalViewR = new int [visibilityRadiusR][visibilityRadiusR];
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


	public static void updateLocation(char side, int lastYMove, int lastXMove){
		//TODO add [100+lastXMove+currRX]
		if(side == 'r'){
			if(aintGlobalViewR[100+lastYMove][100+lastXMove] != 1){
				currRX += lastXMove;
				currRY += lastYMove;
			}
		}
		else
			if (side == 'l'){
				if(aintGlobalViewL[100+lastYMove][100+lastXMove]!=1){
					currLX += lastXMove;
					currLY += lastYMove;
				}
			}
	}

	/*
	 * This method updates the Global View of each player.
	 * It takes as parameter the side, and the current view
	 * that needs to be added to the Global View
	 */
	public static void updateGlobalView (char side, int [][]view, int lastYMove, int lastXMove){

		updateLocation(side, lastYMove, lastXMove);
		if (side == 'r'){
			for (int i=0; i<view.length; i++)
				for (int j=0; j<view[i].length; j++)
					aintGlobalViewR[i+100+currRY][j+100+currRX] = view [i][j];
		}
		else if (side == 'l'){
			for (int i=0; i<view.length; i++)
				for (int j=0;j<view[i].length; j++)
					aintGlobalViewL[i+100+currLY][j+100+currLX] = view [i][j];
		}

	}

	/*
	 * This method updates the Local View of each player.
	 * It takes as parameter the side, and the current view 
	 * that needs to be updated.
	 */
	public static  void updateLocalView (char side, int [][] view){

		int tempLocalView [][] = new int [3][3];
		tempLocalView = view;

		if (side == 'r'){
			aintLocalViewR = tempLocalView;
		}
		else if (side == 'l'){
			aintLocalViewL = tempLocalView;
		}
	}

	public static Node scanMap (int view[][], int x){
		int i=0,j=0;
		Node ret = new Node();
		for (i= 0;i<view.length ; i++)
			for (j=0 ; j<view[i].length ; j++)
				{
					if (view [i][j] == x){
						ret.setX(i);
						ret.setY(j);
						return ret;
					}
					else
						continue;
				}

		return null;
	}

	public static ArrayList<Node> generateSuccessors ( Node n){
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


	public static double hfunc (Node start, Node end, int globalView[][]){
		Double hValue = (double) (Math.abs(start.getX()-end.getX())+ Math.abs(start.getY()-end.getY()));
		if(globalView[start.getX()][start.getY()] == 1)
			hValue += 1000;
		return hValue;
	}
	public static double gfunc(Node start, Node end){
		Double gValue = (double) (Math.abs(start.getX()-end.getX())+ Math.abs(start.getY()-end.getY()));
		return gValue;
	}


	public static LinkedList<Node> aStar2 (int [][] globalView ){

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

public static LinkedList<Node> aStar3 (int [][] globalView ){

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
			Node tempMin = new Node(start.getX(), start.getY());

			/*
			 * Looking for the node in open set having the least
			 * fvalue.
			 */
			for(Node m : open){
				if(min > (gfunc(m, exit)+ hfunc(m,exit,globalView))){
					minCostNode = m;
					min = gfunc(m, exit)+ hfunc(m,exit,globalView);
					}
			}
				open.remove(minCostNode);
				closed.add(minCostNode);
				tempMin = minCostNode;


			boolean isBetter = true;

			if(tempMin == exit)
				return path;

			else {
				succesors = generateSuccessors(tempMin);

				for(Node m : succesors) {
					if(closed.contains(m))	continue;

					if(!open.contains(m)) {
						open.add(m);
						tentCost.put(m, (gfunc(tempMin,exit)+ gfunc(tempMin,m)));
					}

					if(tentCost.get(m) < gfunc(m,exit)) {
						isBetter = true;
					}
					else isBetter = false;

					if(isBetter) {
						path.add(m);
						double f = tentCost.get(m) + hfunc(m, exit, globalView); 
						tentCost.remove(m);
						tentCost.put(m, f);
					}

				}
			}

		}while(!open.isEmpty());

		return path;
	}

public static int[] directionToMove(LinkedList<Node> path, int[][] globalView) {
	int [] direction = new int[path.size()-1];
	Node currPosn = scanMap(globalView, MapData.playerPosition);
	
	int k = 0;
	
	for(int i = 1 ; i <= path.size() ; i++ ) {
		for( int x= -1; x <= 1 ; x++ )
			for( int y= -1; y <= 1 ; y++ )
				if(new Node(currPosn.getX() + x , currPosn.getY() + y ) == path.get(i)) {
					for(int j = 0 ; j < aintDToM.length; i++)
						if(x == aintDToM[j][0] && y == aintDToM[j][1]) {
							direction [k] = j;
							k++;
						} 
				}
					
		}

		return direction;
	}


}

	