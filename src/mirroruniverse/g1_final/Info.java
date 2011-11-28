package mirroruniverse.g1_final;

import mirroruniverse.g1_final.Node;
import mirroruniverse.g1_final.Config;
import mirroruniverse.sim.MUMap;

public class Info {
	
	static int LocalViewL [][] , LocalViewR [][];
	static int GlobalViewL [][], GlobalViewR [][];
	
	static boolean endGameStrategy;
	static int currRX,currRY, currLX, currLY;
	
	static int count = 0 ;
	
	public static void incrementCount(){
		count ++;
		if (Config.DEBUG)
		System.out.println("Step number " + count);
	}
	public static void InitInfo(int visibilityRadiusL, int visibilityRadiusR){
		LocalViewL = new int [visibilityRadiusL] [visibilityRadiusL];
		LocalViewR = new int [visibilityRadiusR] [visibilityRadiusR];
		
		GlobalViewR = new int [Config.getMaxGlobalViewSize()][Config.getMaxGlobalViewSize()];
		GlobalViewL = new int [Config.getMaxGlobalViewSize()][Config.getMaxGlobalViewSize()];
		
		endGameStrategy = false;
		currRX = 99; currLX = 99; currRY = 99;	currLY = 99;
		
		Config.setDEBUG(true);
		for (int i=0; i<Config.getMaxGlobalViewSize() ; i++)
			for (int j=0; j<Config.getMaxGlobalViewSize() ; j++)
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
		updateRelativeLocation ( side, directionLastStep);
		
		int relativePlayerPosX = localView.length /2;
		int relativePlayerPosY = localView.length /2;
			for (int i = -localView.length/2 ; i<localView.length/2 ; i++)
				for (int j = -localView[i+relativePlayerPosX].length/2 ; j<localView[i+relativePlayerPosX].length/2 ; j++){
					if (side == 'l'){
						if (!legalPosition(i+ currLX + relativePlayerPosX) || !legalPosition(j+currLY + relativePlayerPosY)) continue;
						else GlobalViewL[i+ currLX + relativePlayerPosX ][j+currLY + relativePlayerPosY] 
								=	localView [i+relativePlayerPosX] [j+relativePlayerPosY];
					}
					else if (side == 'r'){
						if (!legalPosition(i+ currRX + relativePlayerPosX) || !legalPosition(j+currRY + relativePlayerPosY)) continue;
						else GlobalViewR[i+ currRX + relativePlayerPosX ][j+currRY + relativePlayerPosY] 
								=	localView [i+relativePlayerPosX] [j+relativePlayerPosY];
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
						ret = new Node();
						ret.setX(i);
						ret.setY(j);
						return ret;
					}
					else
						continue;
				}

		return null;

	}
}
