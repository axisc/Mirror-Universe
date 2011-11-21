package mirroruniverse.g1;

import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class g1Player_new implements Player {

	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		
		boolean blnLOver = false;
		boolean blnROver = false;
		
		int lastXMove = -1;
	    int lastYMove = -1;
	    int intDeltaX = 0, intDeltaY = 0;
	    int skew = 2;
	    Info.initInfo();
	    
	    Info.updateGlobalView('l', aintViewL, intDeltaX, intDeltaY);
	    Info.updateGlobalView('r', aintViewR, intDeltaX, intDeltaY);
	    Info.updateLocalView('l', aintViewL);
	    Info.updateLocalView('r', aintViewR);
	    
	    /*
	     * Check if the Exits are visible.
	     */
	    boolean seeLeftExit = false;
        boolean seeRightExit = false;
        if (Info.scanMap(aintViewL, MapData.exit) != null) seeLeftExit = true;
		if (Info.scanMap(aintViewR, MapData.exit)!=null) seeRightExit = true;
		
		/*
		 * If both players can see the exits, we do an A* search algorithm.
		 * Else - we explore further.
		 */
		
		if (seeLeftExit && seeRightExit){
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
	                
	                } while (aintViewL[ aintViewL.length / 2 + intDeltaY ][ aintViewL.length / 2 + intDeltaX ] == 1);
	                lastXMove = intDeltaX ;
	                lastYMove = intDeltaY;
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
	                
	                } while (aintViewR[ aintViewR.length / 2 + intDeltaY ][ aintViewR.length / 2 + intDeltaX ] == 1);
	            	lastXMove = intDeltaX ;
					lastYMove = intDeltaY;
	                return MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ];
	            }
	        }
				lastXMove = intDeltaX ;
				lastYMove = intDeltaY;
	        return MUMap.aintMToD[ lastYMove + 1 ][ lastXMove + 1 ];
			}
		
		return Direction.EAST;
	}

}
