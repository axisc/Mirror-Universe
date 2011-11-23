package mirroruniverse.g1;

import java.util.LinkedList;
import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class g1Player_new implements Player {

	boolean blnLOver = false;
	boolean blnROver = false;
	int lastXMove;
	int lastYMove;
	static boolean seeLeftExit = false;
	static boolean seeRightExit = false;
	boolean initialized = false;
	int[] directionL;
	int[] directionR;
	LinkedList<Node> pathL;
	LinkedList<Node> pathR;
	int nextMoveL;
	int nextMoveR;
	boolean firstMove = true;
	int visibilityRadiusL;
	int visibilityRadiusR;
	
	public g1Player_new(){
		
		System.out.println("Player 1 init");
		pathL = new LinkedList<Node>();
		pathR = new LinkedList<Node>();
		nextMoveL = -1;
		nextMoveR = -1;
		//Info.initInfo(3,3);
		lastXMove = 0 ;
		lastYMove = 0;
		
	}
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {

		if(firstMove) {
			visibilityRadiusL = aintViewL.length-1;
			visibilityRadiusR = aintViewR.length-1;
			Info.initInfo(visibilityRadiusL, visibilityRadiusR);
			firstMove = false;
		}
			
	    int intDeltaX = 0, intDeltaY = 0;
	    int skew = 2;
	    //if(!initialized){
	    	//Info.initInfo();
	    	//initialized = true;
	   // }

	    Info.updateGlobalView('l', aintViewL, lastXMove, lastYMove);
	    Info.updateGlobalView('r', aintViewR, lastXMove, lastYMove);
	    Info.updateLocalView('l', aintViewL);
	    Info.updateLocalView('r', aintViewR);

	    /*
	     * Check if the Exits are visible.
	     */
        //if (Info.scanMap(Info.aintGlobalViewL, MapData.exit) != null) seeLeftExit = true;
		//if (Info.scanMap(Info.aintGlobalViewR, MapData.exit)!=null) seeRightExit = true;

		/*
		 * If both players can see the exits, we do an A* search algorithm.
		 * Else - we explore further.
		 */

		if (seeLeftExit && seeRightExit){
			System.out.println("Time for A*");
			pathL = Info.aStar3(Info.aintGlobalViewL);
			directionL = Info.directionToMove(pathL, Info.aintGlobalViewL);

			pathR = Info.aStar3(Info.aintGlobalViewR);
			directionR = Info.directionToMove(pathR, Info.aintGlobalViewR);
			
			if(!blnLOver) {
				nextMoveL++;
				return directionL[nextMoveL];
			}
			
			else if(!blnROver) {
				nextMoveR++;
				return directionR[nextMoveR];
			}	
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
	                
	                for ( int i = 0; i <= 2; i ++ )
	        		{
	        			for ( int j = 0; j <= 2; j ++ )
	        			{
	        				if ( aintViewL[ i ][ j ] == 2 )
	        					seeLeftExit = true;
	        			}
	        		}
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
	                
					for ( int i = 0; i <= 2; i ++ )
	        		{
	        			for ( int j = 0; j <= 2; j ++ )
	        			{
	        				if ( aintViewR[ i ][ j ] == 2 )
	        					seeRightExit = true;
	        			}
	        		}
	                
					
					return MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ];
	            }
	        }
	        return MUMap.aintMToD[ lastYMove + 1 ][ lastXMove + 1 ];
			}

		return Direction.EAST;
	}

	
	public void traverse(int[] direction) {
		
	}
	
}