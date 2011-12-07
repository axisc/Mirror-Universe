package mirroruniverse.g1_final;

import java.util.ArrayList;

import mirroruniverse.g1_final.AStar_2;
import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class Mirrim2 implements Player {

	static boolean initialized = false;
	static boolean seeLeftExit = false, seeRightExit = false;
	static boolean leftExitPath = false, rightExitPath = false;
	static int directionForThisRound = 0 , directionForPreviousRound = 0;
	static int minMovesOut = Integer.MAX_VALUE;
	private ArrayList<Integer> path = new ArrayList<Integer>();
	private AStar_2 starTester;
	static Node exitL, exitR;
	Exploration ex;
	Info myInfo;
	ArrayList<Integer> allPrevMoves = new ArrayList<Integer>();
	static int estimatedMinMoves = 0;
	boolean totallyExploredExits = false;
	
	public void calculateMinMovesOut() {
		boolean matchFound = false;
		int moveL = -1;
		int moveR = -1;
		int iter = 0;
		
		while(!matchFound) {
			for(int i = -1 ; i <= 1 ; i++) {
				for(int j = -1; j <= 1; j++) {
					if(Info.GlobalViewL[exitL.x + iter + i][exitL.y + j+ iter] == 0 
							&& Info.GlobalViewR[exitR.x + i + iter ][exitR.y + j + iter] == 0) {
						moveL = convertToOppDirection(i,j);
						moveR = convertToOppDirection(i,j);
					}
					if(moveL == moveR && moveR != -1) {
						matchFound = true;
						return;
					}
				}
			}
			estimatedMinMoves++;
			iter++;
			if(estimatedMinMoves >= 1)
				return;
		}
	}
	
	public static int convertToOppDirection(int i, int j) {
		int move = 0;
		
		for(int x = 0; x < MUMap.aintDToM.length; x++) {
			if(MUMap.aintDToM[x][0] == i && MUMap.aintDToM[x][1] == j) {
				if (x == 0)
					continue;
				else if(x < 5)
					move = x+4;
				else
					move = x-4;
			}
		}
		
		return move;
	}

	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		// TODO Auto-generated method stub

		if (!initialized){
			// TODO Initialize Info Class
			Info.InitInfo(aintViewL.length, aintViewR.length);
			initialized = true;
			path = new ArrayList<Integer>();
			ex = new Exploration();
			if (Config.DEBUG) System.out.println("Info class initialized");			
		}

		//Increment the count of the player
		Info.incrementCount();

		// TODO Update the local view of the player
		Info.updateLocalView('l', aintViewL);
		Info.updateLocalView('r', aintViewR);

		ex.updatePossibleConnects(aintViewL, aintViewR);

		boolean newInfoThisTurn = false;
		// TODO Update Global Location
		newInfoThisTurn = Info.updateGlobalLocation('l', aintViewL, directionForPreviousRound);
		newInfoThisTurn = Info.updateGlobalLocation('r', aintViewR, directionForPreviousRound) || newInfoThisTurn;

		/*
		 * If both players see their exit, activate endGameStrategy
		 */
		if(seeLeftExit && !leftExitPath){
			AStar_Single astar = new AStar_Single(Info.currLX, Info.currLY, exitL.x, exitL.y, Info.GlobalViewL);
			Node_Single node = astar.findPath();
			leftExitPath = node != null;
		}
		if(seeRightExit && !rightExitPath){
			AStar_Single astar = new AStar_Single(Info.currRX, Info.currRY, exitR.x, exitR.y, Info.GlobalViewR);
			Node_Single node = astar.findPath();
			rightExitPath = node != null;
		}
		if (seeLeftExit && seeRightExit && !Info.endGameStrategy){
			if(leftExitPath && rightExitPath){
				if(!totallyExploredExits){
					calculateMinMovesOut();
					if(!(ex.leftExitTotallyExplored && ex.rightExitTotallyExplored))
					totallyExploredExits = true;
				}

				if((ex.leftFinished && ex.rightFinished) || (ex.turns % 300 == 0 && newInfoThisTurn)) {
					System.out.println("Time for A*");
					Node startPlayerPositionL = new Node(Info.currLX, Info.currLY);
					Node startPlayerPositionR = new Node(Info.currRX, Info.currRY);

					starTester = new AStar_2(startPlayerPositionL.getX(), startPlayerPositionL.getY(), 
							startPlayerPositionR.getX(), startPlayerPositionR.getY(), Info.GlobalViewL, Info.GlobalViewR);


					starTester.setExit1(exitL.getX(), exitL.getY());
					starTester.setExit2(exitR.getX(), exitR.getY());

					path = starTester.findPath();
				}

				
				if((ex.leftFinished && ex.rightFinished) || minMovesOut <= estimatedMinMoves ){
					Info.activateEndGameStrategy();
					System.out.println("estimated min moves out:" + estimatedMinMoves);
				}

			}
		}

		/*
		 * If end Game strategy kicks in, call A* and use the path.
		 */
		if (Info.endGameStrategy){

				// TODO Follow path
			if (Config.DEBUG) System.out.print("Following Path by A*  Direction = ");
				directionForThisRound = path.remove(0);
				directionForPreviousRound = directionForThisRound;
				if (Config.DEBUG) System.out.println(directionForThisRound);
				return directionForThisRound;

		}
		else{
			// TODO General Exploration Strategy
			if (Config.DEBUG) System.out.println("General Exploration Strategy");
			directionForThisRound = ex.explore(aintViewL, aintViewR, directionForPreviousRound);
		}

		//TODO have these return bools for if each player moved
		Info.updateRelativeLocation('l', directionForThisRound);
		Info.updateRelativeLocation('r', directionForThisRound);

		directionForPreviousRound = directionForThisRound;
		Info.addToListOfAllMoves(directionForThisRound);
		return directionForThisRound;	
	}


}
