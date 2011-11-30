package mirroruniverse.g1_final;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import mirroruniverse.g1_final.Info;
import mirroruniverse.g1_final.MapData;
import mirroruniverse.g1_final.Config;
import mirroruniverse.g1_final.Node;
import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;
import mirroruniverse.g4.*;

public class Mirrim implements Player {

	static boolean initialized = false;
	static boolean seeLeftExit = false, seeRightExit = false;
	
	static int directionForThisRound = 0, directionForPreviousRound = 0;
	static boolean updateLeft = false, updateRight = false;
	static boolean aStarAlreadyCalledForLeftPlayer = false;
	static boolean aStarAlreadyCalledForRightPlayer = false;
	private ArrayList<Integer> path;

	boolean blnLOver = false;
	boolean blnROver = false;
	int lastXMove,lastYMove;
	int directionL[], directionR[];
	LinkedList<Node> pathL,pathR;
	int nextMoveL, nextMoveR;
	int direction;
	AStar_2 starTester ;
	static Node exitR, exitL;
	
	void printLocalView (int view [][]){
		for (int i =0 ; i<view.length ; i++){
			for ( int j = 0; j<view[i].length ; j++)
				System.out.print(view [i][j] + " ");
		System.out.println();
		}
		System.out.println();
	}
	
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		// TODO Auto-generated method stub
		
		printLocalView(aintViewL);
		printLocalView(aintViewR);
		
		if (!initialized){
			Info.InitInfo(aintViewL.length, aintViewR.length);
			initialized = true;
			path = new ArrayList<Integer>();
			
			if (Config.DEBUG) System.out.println("Info class initialized");
		}
		
		//	Increment the count of the player
		Info.incrementCount();
		
		/* If Left player moved */
//		if (updateLeft){
//		Info.updateGlobalLocation2('l', aintViewL,directionForPreviousRound);
//		updateLeft = false;
//		}
//		/* If Right player moved */
//		if (updateRight){
//		Info.updateGlobalLocation2('r', aintViewR, directionForPreviousRound);
//		updateRight = false;
//		}
		Info.updateGlobalLocation2('l', aintViewL,directionForPreviousRound);
		Info.updateGlobalLocation2('r', aintViewR,directionForPreviousRound);
		
//
//		if (!seeLeftExit && Info.scanMapBool(Info.GlobalViewL, MapData.EXIT)) {
//			seeLeftExit = true;
//			exitL =  Info.scanMap(Info.GlobalViewL, MapData.EXIT);
//			if (Config.DEBUG) System.out.println("Left Player has spotted the Exit");
//		}
//		if (!seeRightExit && Info.scanMapBool(Info.GlobalViewR, MapData.EXIT)) {
//			seeRightExit= true;
//			exitR = Info.scanMap(Info.GlobalViewR, MapData.EXIT);
//			if (Config.DEBUG) System.out.println("Right Player has spotted the Exit");
//		}
		
		/*
		 * If both players see their exit, activate endGameStrategy
		 */
		if (seeLeftExit && seeRightExit && !Info.endGameStrategy){
			System.out.println("Time for A*");
			Info.activateEndGameStrategy();
		}
		
		/*
		 * In the end game strategy, call A* for both players and find path.
		 */
		if (Info.endGameStrategy){
			if (path.isEmpty()){
				Node startPlayerPositionL = new Node(Info.currLX, Info.currLY);
				Node startPlayerPositionR = new Node(Info.currRX, Info.currRY);
				
				starTester = new AStar_2(startPlayerPositionL.getY(), startPlayerPositionL.getX(), 
						startPlayerPositionR.getY(), startPlayerPositionR.getX(), Info.GlobalViewL, Info.GlobalViewR);
				
				
				starTester.setExit1(exitL.getY(), exitL.getX());
				starTester.setExit2(exitR.getY(), exitR.getX());
				
				path = starTester.findPath();
				
			}
			else{
//				directionForThisRound = actualDirection(path.remove(0));
				directionForThisRound =  path.remove(0);
				directionForPreviousRound = directionForThisRound;
				System.out.println("THIS IS FROM THE PATH " + directionForThisRound);
				Info.updateRelativeLocation('l', directionForThisRound);
				Info.updateRelativeLocation('r', directionForThisRound);
				
				return directionForThisRound;
			}
		}
		
		/*
		 * Else move randomly, and explore
		 */
		else {
		
		directionForThisRound = Exploration.randomMove();
		}
		
//		if ( aintViewL[aintViewL.length / 2 + MUMap.aintDToM[directionForThisRound][1]][aintViewL.length / 2 + MUMap.aintDToM[directionForThisRound][0]] != 1){
//			updateLeft = true;
//			Info.updateRelativeLocation('l', directionForThisRound);
//			if (Config.DEBUG)
//				System.out.println("We need to update Left position");
//		}
//		if ( aintViewR[aintViewR.length / 2 + MUMap.aintDToM[directionForThisRound][1]][aintViewR.length / 2 + MUMap.aintDToM[directionForThisRound][0]] != 1){
//			updateRight = true;
//			Info.updateRelativeLocation('r', directionForThisRound);
//			if (Config.DEBUG)
//				System.out.println("We need to update Right position");
//		}
		
		Info.updateRelativeLocation('l', directionForThisRound);
		Info.updateRelativeLocation('r', directionForThisRound);
		
		directionForPreviousRound = directionForThisRound;
		return directionForThisRound;

		
	}


	private int actualDirection(Integer remove) {
		// TODO Auto-generated method stub
		
		switch(remove){
		case 4:	return 4;
		case 8: return 8;
		case 5: return 3;
		case 1: return 7;
		case 7: return 1;
		case 2: return 6;
		case 6: return 2;
		case 3: return 5;
		}
		return 0;
	}

}
