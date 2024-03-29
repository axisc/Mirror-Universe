package mirroruniverse.g1_new;

import mirroruniverse.g1_new.Node;
import mirroruniverse.g1_new.MapData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import mirroruniverse.sim.MUMap;

public class Info {

	/*
	 * Local views for both players
	 */
	static int aintLocalViewL [][];
	static int aintLocalViewR [][];
	
	static Astar astar ;
	/*
	 * Global Views for both players
	 */
	static int aintGlobalViewL [][];
	static int aintGlobalViewR [][];
	
	static int currLX = 0, currLY = 0, currRX = 0, currRY = 0;
	

	static ArrayList<LinkedList<Node>> path = new ArrayList<LinkedList<Node>>();
	static ArrayList<Node> searchGraph = new ArrayList<Node>();
	static ArrayList<Node> open = new ArrayList<Node>();
	static ArrayList<Node> closed = new ArrayList<Node>();
	static ArrayList <Node> m = new ArrayList<Node>();
	static ArrayList <Node> came_from = new ArrayList<Node>();
	public static final int[][] aintDToM = { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 },  { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
	static Node startL, endL, startR, endR;
	
	
	public static void initInfo ( int visibilityRadiusL, int visibilityRadiusR){
		astar = new Astar();
		aintGlobalViewL = new int [199][199];
		aintGlobalViewR = new int [199][199];
		
		for ( int i=0 ; i<199 ;i++)
			for (int j=0; j<199 ;j++)
				aintGlobalViewL [i][j] = aintGlobalViewR[i][j] = MapData.unknown;
		
	aintLocalViewL = new int [visibilityRadiusL][visibilityRadiusL];
	aintLocalViewR = new int [visibilityRadiusR][visibilityRadiusR];
	
	startL = new Node();
	endL = new Node();
	startR = new Node();
	endR = new Node();
	}
	
	public static void updateRelativeLocation(char side,int intDirection){
		System.out.println("Currently updating the new Relative Location");
		int[] aintMove = MUMap.aintDToM[ intDirection ];
		/*
		 *  TODO check if X and Y need to be flipped here.
		 *  This thing honestly confuses me.
		 */
		int intDeltaY = aintMove[ 1 ];
		int intDeltaX = aintMove[ 0 ];
		
		System.out.println("Okay, now the Delta Y and X are " + intDeltaY + " " + intDeltaX);
		if (side == 'l' && aintGlobalViewL[intDeltaX+99+currLX][intDeltaY+99+currLY] != 1){
			System.out.println("Updating Relative Location for Left side");
			currLX += intDeltaX;
			currLY += intDeltaY;
		}
		
		else if (side == 'r' && aintGlobalViewR[intDeltaX+99+currRX][intDeltaY+99+currRY] != 1){
			System.out.println("Updating Relative Location for Right side");
			currRX += intDeltaX;
			currRY += intDeltaY;
		}
		
		System.out.println("Exiting the Relative Location update");
	}
	
	public static boolean legalPosition(int l){
		boolean isItTheLegalPosition = false;
		if (l>-1 && l<aintGlobalViewL.length)
			isItTheLegalPosition = true;
		
		return isItTheLegalPosition;
	}
	
	/*
	 * This method update the current Global View of the system.
	 * It takes as parameter the last direction we moved in.
	 * This call is made from G1Player.java 
	 * The direction is taken from the previous round, but the view 
	 * is taken from the current round.
	 */
	public static void updateGlobalLocation(char side, int view[][], int lastDirectionThatPlayerMovedIn){
		
		if (side == 'l'){
			System.out.println("Updating Global view for left player");
			
			startL.setX(currLX + 99);
			startL.setY(currLY + 99);
			
			for (int i=0; i<view.length; i++)
				for (int j=0; j<view[i].length; j++){
						if (!legalPosition(i+99+currLY)|| !legalPosition(j+99+currLX)) continue;
						else aintGlobalViewL[j+99+currLX][i+99+currLY] = view [j][i];
						
						if ( aintGlobalViewL[j+99+currLX][i+99+currLY] == MapData.exit){
							endL.setY(i+99+currLY);
							endL.setX(j+99+currLX);
						}
					}
		}
		else if (side == 'r'){
			startR.setX(currRX + 99);
			startR.setY(currRY + 99);
			
			System.out.println("Updating Global view for right player");
			for (int i=0; i<view.length; i++)
				for (int j=0; j<view[i].length; j++)
				{	if (!legalPosition(i+99+currRY) || !legalPosition(j+99+currRX)) continue;
					else aintGlobalViewR[j+99+currRX][i+99+currRY] = view [j][i];
			
					if ( aintGlobalViewR[j+99+currRX][i+99+currRY] == MapData.exit){
						endR.setY(i+99+currRY);
						endR.setX(j+99+currRX);
					}
				}
					
		}
		System.out.println("Global View for side " + side + "has been updated");
	}
	
	/*
	 * This method updates the Local View of each player.
	 * It takes as parameter the side, and the current view 
	 * that needs to be updated.
	 */
	public static  void updateLocalView (char side, int [][] view){
		
		int tempLocalView [][] = new int [view.length][view.length];
		tempLocalView = view;
		
		if (side == 'r'){
			aintLocalViewR = tempLocalView;
			
		}
		else if (side == 'l'){
			aintLocalViewL = tempLocalView;
		}
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
	
	public static LinkedList<Node> aStar3 (int [][] globalView, char side ){

		Node start, exit;
		
		if (side == 'r' && startR != null) 
			start = startR;
		else if (side == 'l' && startL != null)
			start = startL;
		else return null;
		
		
		if (side == 'r' && endR != null) 
			exit = endR;
		else if (side == 'l' && endL != null)
			exit = endL;
		else return null;
		

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
	
	public static int[][] getLGlobalView(){
		return aintGlobalViewL;
	}
	
	public static int[][] getRGlobalView(){
		return aintGlobalViewR;
	}

	public static int getCurrLX() {
		return currLX;
	}

	public static int getCurrLY() {
		return currLY;
	}

	public static int getCurrRX() {
		return currRX;
	}

	public static int getCurrRY() {
		return currRY;
	}
	

}

