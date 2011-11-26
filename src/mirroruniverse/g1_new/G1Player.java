package mirroruniverse.g1_new;

import java.util.LinkedList;
import java.util.Random;

import mirroruniverse.g1_new.Info;
import mirroruniverse.g1_new.Node;
import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class G1Player implements Player {
	Random rdmTemp = new Random();
	static boolean initialized = false;
	static Exploration explore = new Exploration();
	

	boolean blnLOver = false;
	boolean blnROver = false;
	int lastXMove;
	int lastYMove;
	static boolean seeLeftExit = false;
	static boolean seeRightExit = false;
	int[] directionL;
	int[] directionR;
	LinkedList<Node> pathL;
	LinkedList<Node> pathR;
	int nextMoveL;
	int nextMoveR;
	boolean firstMove = true;
	static int directionForThisRound = 0, directionForPreviousRound = 0;
	static boolean updateLeft = false;
	static boolean updateRight = false;
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		// TODO Auto-generated method stub
		/*
		 * Initialize the player.
		 */
		if (!initialized){
			Info.initInfo(aintViewL.length, aintViewR.length);
			initialized = true;
			System.out.println("Called INITIALIZED ONCE ONLY");
		}
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

		if (Info.scanMap(Info.aintGlobalViewL, MapData.exit) != null) seeLeftExit = true;
		if (Info.scanMap(Info.aintGlobalViewR, MapData.exit) != null) seeRightExit= true;
		/*
		 * If both exits are visible, use A* to get
		 * the next direction
		 */
		if (seeLeftExit && seeRightExit){
			System.out.println("Time for A*");
			pathL = Info.aStar3(Info.aintGlobalViewL);
			directionL = Info.directionToMove(pathL, Info.aintGlobalViewL);

			pathR = Info.aStar3(Info.aintGlobalViewR);
			directionR = Info.directionToMove(pathR, Info.aintGlobalViewR);
			
			if(!blnLOver) {
				nextMoveL++;
				directionForThisRound = directionL[nextMoveL];
			}
			
			else if(!blnROver) {
				nextMoveR++;
				directionForThisRound = directionR[nextMoveR];
			}	
		}
		/*
		 * Else move randomly, and explore
		 */
		directionForThisRound = explore.randomMove();
		
		if ( aintViewL[aintViewL.length / 2 + MUMap.aintDToM[directionForThisRound][0]][aintViewL.length / 2 + MUMap.aintDToM[directionForThisRound][1]] != 1){
			updateLeft = true;
			System.out.println("We need to update Left position");
		}
		if ( aintViewR[aintViewR.length / 2 + MUMap.aintDToM[directionForThisRound][0]][aintViewR.length / 2 + MUMap.aintDToM[directionForThisRound][1]] != 1){
			updateRight = true;
			System.out.println("We need to update Right position");
		}
		directionForPreviousRound = directionForThisRound;
		return directionForThisRound;
	}

}
