package mirroruniverse.g1_final;

import java.util.ArrayList;

import mirroruniverse.g1_final.AStar_2;
import mirroruniverse.sim.Player;

public class Mirrim2 implements Player {

	static boolean initialized = false;
	static boolean seeLeftExit = false, seeRightExit = false;
	static boolean leftExitPath = false, rightExitPath = false;
	static int directionForThisRound = 0 , directionForPreviousRound = 0;
	static int minMovesOut = Integer.MAX_VALUE;
	ArrayList<Integer> path = new ArrayList<Integer>();
	AStar_2 starTester ;
	static Node exitL, exitR;
	Exploration ex;
	
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
		newInfoThisTurn = newInfoThisTurn || Info.updateGlobalLocation('r', aintViewR, directionForPreviousRound);
		
		/*
		 * If both players see their exit, activate endGameStrategy
		 */
		if (seeLeftExit && seeRightExit && !Info.endGameStrategy && newInfoThisTurn){
			if(!leftExitPath){
				AStar_Single astar = new AStar_Single(Info.currLX, Info.currLY, exitL.x, exitL.y, Info.GlobalViewL);
				Node_Single node = astar.findPath();
				leftExitPath = node != null;
			}
			if(!rightExitPath){
				AStar_Single astar = new AStar_Single(Info.currRX, Info.currRY, exitR.x, exitR.y, Info.GlobalViewR);
				Node_Single node = astar.findPath();
				rightExitPath = node != null;
			}
			if(leftExitPath && rightExitPath){
				if (Config.DEBUG) System.out.println("Time for A*");
				Node startPlayerPositionL = new Node(Info.currLX, Info.currLY);
				Node startPlayerPositionR = new Node(Info.currRX, Info.currRY);
				
				starTester = new AStar_2(startPlayerPositionL.getX(), startPlayerPositionL.getY(), 
						startPlayerPositionR.getX(), startPlayerPositionR.getY(), Info.GlobalViewL, Info.GlobalViewR);
				
				
				starTester.setExit1(exitL.getX(), exitL.getY());
				starTester.setExit2(exitR.getX(), exitR.getY());
				
				path = starTester.findPath();
				if(minMovesOut == 0 || (ex.leftFinished && ex.rightFinished))
					Info.activateEndGameStrategy();
				
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
				System.out.println(directionForThisRound);
				return directionForThisRound;
			
		}
		else{
			// TODO General Exploration Strategy
			if (Config.DEBUG) System.out.println("General Exploration Strategy");
			directionForThisRound = ex.explore(aintViewL, aintViewR, directionForPreviousRound);
		}
			
		Info.updateRelativeLocation('l', directionForThisRound);
		Info.updateRelativeLocation('r', directionForThisRound);
		
		directionForPreviousRound = directionForThisRound;
		return directionForThisRound;	
	}
	

}
