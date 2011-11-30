package mirroruniverse.g1_final;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import mirroruniverse.g1_final.Node;
import mirroruniverse.g1_final.Config;
import mirroruniverse.g1_final.MapData;
import mirroruniverse.sim.MUMap;

public class Info {
	
	static int LocalViewL [][] , LocalViewR [][];
	static int GlobalViewL [][], GlobalViewR [][];
	
	static boolean endGameStrategy;
	static int currRX,currRY, currLX, currLY;
	
	static int count = 0 ;
	static ArrayList<LinkedList<Node>> path = new ArrayList<LinkedList<Node>>();
	static ArrayList<Node> searchGraph = new ArrayList<Node>();
	static ArrayList<Node> open = new ArrayList<Node>();
	static ArrayList<Node> closed = new ArrayList<Node>();
//	static ArrayList <Node> m = new ArrayList<Node>();
	static ArrayList <Node> came_from = new ArrayList<Node>();
	public static final int[][] aintDToM = { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 },  { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
	static Node startL, endL, startR, endR;
	static Map<Node, Double> tentCost = new HashMap<Node, Double>();
	
	public static void incrementCount(){
		count ++;
		if (Config.DEBUG)
		System.out.println("Step number " + count);
	}
	public static void InitInfo(int visibilityRadiusL, int visibilityRadiusR){
		LocalViewL = new int [visibilityRadiusL] [visibilityRadiusL];
		LocalViewR = new int [visibilityRadiusR] [visibilityRadiusR];
		
		GlobalViewR = new int [2*(Config.MAX_MAP_SIZE + visibilityRadiusR)][2*(Config.MAX_MAP_SIZE + visibilityRadiusR)];
		GlobalViewL = new int [2*(Config.MAX_MAP_SIZE + visibilityRadiusL)][2*(Config.MAX_MAP_SIZE + visibilityRadiusL)];
		
		endGameStrategy = false;
		currRX = 99; currLX = 99; currRY = 99;	currLY = 99;
		
		Config.setDEBUG(true);
		for (int i=0; i<199 ; i++)
			for (int j=0; j<199 ; j++)
				GlobalViewL[i][j] = GlobalViewR [i][j] = MapData.UNKNOWN;
	}

	public static void activateEndGameStrategy (){
		endGameStrategy =  true;
	}
	
	public static void updateRelativeLocation ( char side, int directionLastStep){
		if (Config.DEBUG){
			System.out.println("Update relative location for " + side);
		}
		
		int lastXMove = MUMap.aintDToM [directionLastStep] [0];
		int lastYMove = MUMap.aintDToM [directionLastStep] [1];
		if (side == 'r') {
			currRX += lastXMove;
			currRY += lastYMove;
		}
		else if (side == 'l'){
			currLX += lastXMove;
			currLY += lastYMove;
		}
		
		if (Config.DEBUG) {
			System.out.println("Current X and Y for Right Player " + currRX + " " + currRY);
			System.out.println("Current X and Y for Left Player  " + currLX + " " + currLY);
		}
		
		
		
	}
	
	public static void updateGlobalLocation (char side, int localView [][], int directionLastStep){
//		updateRelativeLocation ( side, directionLastStep);
		
		int relativePlayerPosX = localView.length /2;
		int relativePlayerPosY = localView.length /2;
			for (int i = -localView.length/2 ; i<localView.length/2 ; i++)
				for (int j = -localView[i+relativePlayerPosX].length/2 ; j<localView[i+relativePlayerPosX].length/2 ; j++){
					if (side == 'l'){
						if (!legalPosition(i+ currLX /*+ relativePlayerPosX*/) || !legalPosition(j+currLY /*+ relativePlayerPosY*/)) continue;
						else GlobalViewL[i+ currLX /*+ relativePlayerPosX */][j+currLY /*+ relativePlayerPosY*/] 
								=	localView [i+relativePlayerPosX] [j+relativePlayerPosY];
						if (localView [i+relativePlayerPosX] [j+relativePlayerPosY] == MapData.EXIT){
							Mirrim.exitL = new Node(i+currLX, j+currLY);
							Mirrim.seeLeftExit = true;
						}
					}
					else if (side == 'r'){
						if (!legalPosition(i+ currRX /*+ relativePlayerPosX*/) || !legalPosition(j+currRY /*+ relativePlayerPosY*/)) continue;
						else GlobalViewR[i+ currRX /*+ relativePlayerPosX*/ ][j+currRY /*+ relativePlayerPosY*/] 
								=	localView [i+relativePlayerPosX] [j+relativePlayerPosY];
						
						if (localView [i+relativePlayerPosX] [j+relativePlayerPosY] == MapData.EXIT){
							Mirrim.exitR = new Node(i+currRX, j+currRY);
							Mirrim.seeRightExit = true;
						}
					}
					
				}
			
	
		
	}
	
	public static void updateGlobalLocation2 (char side, int localView [][], int directionLastStep){
		//updateRelativeLocation ( side, directionLastStep);
		
		int scewX = localView.length;
		int scewY = localView.length;
			for (int i = 0 ; i<localView.length ; i++)
				for (int j = 0 ; j<localView[0].length ; j++){
					if (side == 'l'){
					//	if (!legalPosition(i+ currLX-scewX /*+ relativePlayerPosX*/) || !legalPosition(j+currLY-scewY /*+ relativePlayerPosY*/)) continue;
						/*else*/ GlobalViewL[i+ currLX - scewX /*+ relativePlayerPosX */][j+currLY - scewY/*+ relativePlayerPosY*/] 
								=	localView [i] [j];
						if (localView [i] [j] == MapData.EXIT){
							Mirrim.exitL = new Node(i+currLX, j+currLY);
							Mirrim.seeLeftExit = true;
						}
					}
					else if (side == 'r'){
					//	if (!legalPosition(i+ currRX - scewX/*+ relativePlayerPosX*/) || !legalPosition(j+currRY - scewY /*+ relativePlayerPosY*/)) continue;
						/*else*/ GlobalViewR[i+ currRX - scewX /*+ relativePlayerPosX*/ ][j+currRY - scewY/*+ relativePlayerPosY*/] 
								=	localView [i] [j];
						
						if (localView [i] [j] == MapData.EXIT){
							Mirrim.exitR = new Node(i+currRX, j+currRY);
							Mirrim.seeRightExit = true;
						}
					}
				}
	}
	
	public static boolean legalPosition(int l){
		boolean isItTheLegalPosition = false;
		if (l>-1 && l<GlobalViewL.length)
			isItTheLegalPosition = true;
		
		return isItTheLegalPosition;
	}
	
	public static boolean scanMapBool (int view [][], int x){
		System.out.println("In the scanMapBool Function");
		for (int i = 0; i<view.length ; i++)
			for ( int j=0; j<view.length ; j++)
				if (view [i][j] == x) {
					if (Config.DEBUG) System.out.println("RETURNING TRUE");
					return true;
				}
				
		if (Config.DEBUG) System.out.println("RETURNING FALSE");
		return false;
	}
	
	public static Node scanMap ( int view [][], int x){
		int i=0,j=0;
		Node ret ;
		for (i= 0;i<view.length ; i++)
			for (j=0 ; j<view[i].length ; j++)
				{
					if (view [i][j] == x){
						ret = new Node(i,j);
						return ret;
					}
					
				}

		return null;

	}
	
	public static int[] directionToMove(LinkedList<Node> path, int[][] globalView) {
		int [] direction = new int[path.size()-1];
		Node currPosn = scanMap(globalView, MapData.PLAYERPOSITION);
		
		int k = 0;
		
		for(int i = 1 ; i <= path.size() ; i++ ) {
			for( int x= -1; x <= 1 ; x++ )
				for( int y= -1; y <= 1 ; y++ )
					if(new Node(currPosn.getX() + x , currPosn.getY() + y ) == path.get(i)) {
						for(int j = 0 ; j < MUMap.aintDToM.length; i++)
							if(x == MUMap.aintDToM[j][0] && y == MUMap.aintDToM[j][1]) {
								direction [k] = j;
								k++;
							} 
					}
						
			}

			return direction;
		}
	
	public static LinkedList<Node> aStar3 (int [][] globalView, char side ){

		Node start, exit;
		if (side == 'r' /*&& startR != null*/) {
			start= new Node(currRX , currRY);
		}
		else if (side == 'l' /*&& startL != null*/){
			start = new Node(currLX, currLY);
		}
		else return null;
		
		
//		if (side == 'r'/* && endR != null*/) 
//			//exit = endR;
//			exit = scanMap(globalView, MapData.EXIT);
//		else if (side == 'l' /*&& endL != null*/)
//			//exit = endL;
//			exit = scanMap(globalView, MapData.EXIT);
//		else return null;

		exit = scanMap(globalView, MapData.EXIT);

		if (start == null && exit == null )
			System.out.println("One of the start nodes is null");
		
		open.add(start);

		ArrayList<Node> succesors = new ArrayList<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		
		tentCost.put(start, 0.0);
		path.add(start);

		double min = Double.MAX_VALUE;
		Node minCostNode = new Node(start.getX(), start.getY());
		Node tempMin = new Node(start.getX(), start.getY());
		
		do{
			/*
			 * Looking for the node in open set having the least
			 * fvalue.
			 */
			for(Node m : open){
				if(min > tentCost.get(m)/*(gfunc(m, exit)+ hfunc(m,exit,globalView))*/){
					minCostNode = m;
					min = tentCost.get(m);
					}
			}
	
				open.remove(minCostNode);
				closed.add(minCostNode);
				tempMin = minCostNode;

			boolean isBetter = true;

			if(tempMin.getX() == exit.getX() && tempMin.getY() == exit.getY())
				return path;

			else {
				succesors = generateSuccessors(globalView, tempMin);
//				if(succesors.contains(minCostNode)) succesors.remove(minCostNode);
				
				for(int i=0; i< succesors.size(); i++) {
					Node m = succesors.get(i);
					if(!Node.contained(closed, m)){	

						if(!Node.contained(open, m)) {
							open.add(m);
							if(Config.DEBUG) System.out.println("node added to open: " + m.getX()+" "+m.getY());
							tentCost.put(m, (gfunc(tempMin,exit)+ gfunc(tempMin,m)));
							if(Config.DEBUG) System.out.println("node cost::" + tentCost.get(m));
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
			}
			if (Config.DEBUG) System.out.println("Still in A*- Relax");
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
	
	public static ArrayList<Node> generateSuccessors (int[][]globalView, Node n){
		ArrayList<Node> successors = new ArrayList<Node>();
		
		for (int i =-1 ; i <= 1; i++ )
			for (int j=-1 ; j<=1 ;j++){
				Node temp = new Node(n.getX()+i, n.getY()+j);
				if(i==0 && j==0)
					continue;
				if ((globalView[temp.getX()][temp.getY()]!= MapData.UNKNOWN))
					successors.add(temp);
			}
		
		return successors;
	}

	public static int getCurrRX() {
		return currRX;
	}
	public static void setCurrRX(int currRX) {
		Info.currRX = currRX;
	}
	public static int getCurrRY() {
		return currRY;
	}
	public static void setCurrRY(int currRY) {
		Info.currRY = currRY;
	}
	public static int getCurrLX() {
		return currLX;
	}
	public static void setCurrLX(int currLX) {
		Info.currLX = currLX;
	}
	public static int getCurrLY() {
		return currLY;
	}
	public static void setCurrLY(int currLY) {
		Info.currLY = currLY;
	}
}
