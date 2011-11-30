package mirroruniverse.g1_final;

import java.util.ArrayList;

import mirroruniverse.sim.Player;

public class Mirrim2 implements Player {

	static boolean initialized = false;
	static boolean seeLeftExit = false, seeRightExit = false;
	static int directionForThisRound = 0 , directionForPreviousRound = 0;
	ArrayList<Integer> path = new ArrayList<Integer>();
	
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		// TODO Auto-generated method stub
		
		if (!initialized){
			// TODO Initialize Info Class
			Info.InitInfo(aintViewL.length, aintViewR.length);
			initialized = true;
			path = new ArrayList<Integer>();
			
			if (Config.DEBUG) System.out.println("Info class initialized");			
		}
		
		//Increment the count of the player
		Info.incrementCount();
		
		// TODO Update Global Location
		Info.updateGlobalLocation2('l', aintViewL, directionForPreviousRound);
		Info.updateGlobalLocation2('r', aintViewR, directionForPreviousRound);
		
		/*
		 * If both players see their exit, activate endGameStrategy
		 */
		if (seeLeftExit && seeRightExit && !Info.endGameStrategy){
			System.out.println("Time for A*");
			Info.activateEndGameStrategy();
		}
		
		/*
		 * If end Game strategy kicks in, call A* and use the path.
		 */
		if (Info.endGameStrategy){
			if (path.isEmpty()){
				// TODO Generate Path
			
				
				
			}
			else{
				// TODO Follow path
				directionForThisRound = path.remove(0);
				directionForPreviousRound = directionForThisRound;
				return directionForThisRound;
			}
			
		}
			
		
		return 0;
	}
	

}
