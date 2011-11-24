package mirroruniverse.g1;

import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class g1Player_new implements Player {

	boolean blnLOver = false;
	boolean blnROver = false;
	int lastXMove = 0;
	int lastYMove = 0;
	static boolean seeLeftExit = false;
	static boolean seeRightExit = false;
	boolean initialized = false;
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		
		
	    int intDeltaX = 0, intDeltaY = 0;
	    int skew = 2;
	    if(!initialized){
	    	Info.initInfo(aintViewL.length, aintViewR.length);
	    	initialized = true;
	    }
	    
	    Info.updateGlobalView('l', aintViewL, lastXMove, lastYMove);
	    Info.updateGlobalView('r', aintViewR, lastXMove, lastYMove);
	    Info.updateLocalView('l', aintViewL);
	    Info.updateLocalView('r', aintViewR);
	    
	    /*
	     * Check if the Exits are visible.
	     */
        if (Info.scanMap(Info.aintGlobalViewL, MapData.exit) != null) seeLeftExit = true;
		if (Info.scanMap(Info.aintGlobalViewR, MapData.exit)!=null) seeRightExit = true;
		
		/*
		 * If both players can see the exits, we do an A* search algorithm.
		 * Else - we explore further.
		 */
		
		if (seeLeftExit && seeRightExit){
			if (seeLeftExit) System.out.println("Left player can see exit");
			if (seeRightExit)System.out.println("Right player can see exit");
			System.out.println("Time for A*");
		}
		else
		if (!seeLeftExit || !seeRightExit){
			Random rdmTemp = new Random();
			/*
			 * 80% repeat last move - unless blocked by wall.
			 */
			if (!blnLOver){
	            if(aintViewL[aintViewL.length / 2 + lastYMove][aintViewL.length / 2 + lastXMove] == 1 || rdmTemp.nextInt( 5 ) == 4){
	                //favor diagonals
	                do{
	                    intDeltaX = rdmTemp.nextInt(5) - skew;
	                    if(intDeltaX < 0)		intDeltaX = -1;
	                    else if(intDeltaX > 0)	intDeltaX = 1;
	                    
	                    intDeltaY = rdmTemp.nextInt(5) - skew;
	                    if(intDeltaY < 0)		intDeltaY = -1;
	                    else if(intDeltaY > 0)	intDeltaY = 1;
	                
	                } while (aintViewL[ aintViewL.length / 2 + intDeltaY ][ aintViewL.length / 2 + intDeltaX ] == 1
	                		 || (intDeltaY == 0 && intDeltaX == 0));
	                lastXMove = intDeltaX ;
	                lastYMove = intDeltaY;
	                System.out.println("We are moving in " + MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ] + "direction");
	                return MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ];
	                
	            }

			}
			else if (!blnROver){
	            if(aintViewR[aintViewR.length / 2 + lastYMove][aintViewR.length / 2 + lastXMove] == 1 || rdmTemp.nextInt( 5 ) == 4){
	                do{
	                    intDeltaX = rdmTemp.nextInt(5) - skew;
	                    if(intDeltaX < 0)		intDeltaX = -1;
	                    else if(intDeltaX > 0)	intDeltaX = 1;
	                    
	                    intDeltaY = rdmTemp.nextInt(5) - skew;
	                    if(intDeltaY < 0)		intDeltaY = -1;
	                    else if(intDeltaY > 0)	intDeltaY = 1;
	                
	                } while (aintViewR[ aintViewR.length / 2 + intDeltaY ][ aintViewR.length / 2 + intDeltaX ] == 1
	                		 || (intDeltaY == 0 && intDeltaX == 0));
	            	lastXMove = intDeltaX ;
					lastYMove = intDeltaY;
					System.out.println("We are moving in " + MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ] +" direction");
	                return MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ];
	            }
	        }
			System.out.println("We are moving in "+MUMap.aintMToD[ lastYMove + 1 ][ lastXMove + 1 ] + "direction");
	        return MUMap.aintMToD[ lastYMove + 1 ][ lastXMove + 1 ];
			}
		
		return Direction.EAST;
	}

}
