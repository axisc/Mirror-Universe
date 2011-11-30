package mirroruniverse.g1_final;

import java.util.LinkedList;

import mirroruniverse.g1_final.Info;
import mirroruniverse.g1_final.MapData;
import mirroruniverse.g1_final.Config;
import mirroruniverse.g1_final.Node;
import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class Mirrim implements Player {

	static boolean initialized = false;
	static boolean seeLeftExit = false, seeRightExit = false;
	
	static int directionForThisRound = 0, directionForPreviousRound = 0;
	static boolean updateLeft = false;
	static boolean updateRight = false;
	static boolean aStarAlreadyCalledForLeftPlayer = false;
	static boolean aStarAlreadyCalledForRightPlayer = false;

	boolean blnLOver = false;
	boolean blnROver = false;
	int lastXMove;
	int lastYMove;
	int[] directionL;
	int[] directionR;
	LinkedList<Node> pathL;
	LinkedList<Node> pathR;
	int nextMoveL;
	int nextMoveR;
	
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		// TODO Auto-generated method stub
		
		if (!initialized){
			Info.InitInfo(aintViewL.length, aintViewR.length);
			initialized = true;
			
			if (Config.DEBUG) System.out.println("Info class initialized");
		}
		
		Info.incrementCount();
		
		/*
		 * If Left player moved 
		 */
		if (updateLeft){
		Info.updateGlobalLocation('l', aintViewL,directionForPreviousRound);
		updateLeft = false;
		}
		/*
		 * If Right player moved
		 */
		if (updateRight){
		Info.updateGlobalLocation('r', aintViewR, directionForPreviousRound);
		updateRight = false;
		}

		if (Info.scanMapBool(Info.GlobalViewL, MapData.EXIT)) {
			seeLeftExit = true;
			if (!Config.DEBUG) System.out.println("Left Player has spotted the Exit");
		}
		if (Info.scanMapBool(Info.GlobalViewR, MapData.EXIT)) {
			seeRightExit= true;
			if (!Config.DEBUG) System.out.println("Right Player has spotted the Exit");
		}
		/*
		 * If both exits are visible, use A* to get
		 * the next direction
		 */
		if (seeLeftExit && seeRightExit){
			System.out.println("Time for A*");
			
			Info.activateEndGameStrategy();
		
		}
		if (Info.endGameStrategy){
			
			if (!aStarAlreadyCalledForLeftPlayer){
				pathL = Info.aStar3(Info.GlobalViewL, 'l');
				aStarAlreadyCalledForLeftPlayer = true;
				}
			
			if (pathL != null){
			directionL = Info.directionToMove(pathL, Info.GlobalViewL);
			}else
				if (Config.DEBUG) System.out.println("Path L is not initialized");

			
			
			if(!blnLOver) {
				nextMoveL++;
				directionForThisRound = directionL[nextMoveL];
			}
			
			if (!aStarAlreadyCalledForRightPlayer){
			pathR = Info.aStar3(Info.GlobalViewR,'r');
			aStarAlreadyCalledForRightPlayer = true;
			}
			directionR = Info.directionToMove(pathR, Info.GlobalViewR);
			
			if(!blnROver) {
				nextMoveR++;
				directionForThisRound = directionR[nextMoveR];
			}	
		}
		/*
		 * Else move randomly, and explore
		 */
		directionForThisRound = Exploration.randomMove();
		
		if ( aintViewL[aintViewL.length / 2 + MUMap.aintDToM[directionForThisRound][0]][aintViewL.length / 2 + MUMap.aintDToM[directionForThisRound][1]] != 1){
			updateLeft = true;
			if (Config.DEBUG)
				System.out.println("We need to update Left position");
		}
		if ( aintViewR[aintViewR.length / 2 + MUMap.aintDToM[directionForThisRound][0]][aintViewR.length / 2 + MUMap.aintDToM[directionForThisRound][1]] != 1){
			updateRight = true;
			if (Config.DEBUG)
				System.out.println("We need to update Right position");
		}
		directionForPreviousRound = directionForThisRound;
		return directionForThisRound;

		
	}

}
