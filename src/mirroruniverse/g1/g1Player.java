package mirroruniverse.g1;

import java.util.LinkedList;
import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;
import mirroruniverse.g1.Direction;
public class g1Player implements Player {

	public Info info;
	
	public int returnDirection (Node source , Node destination){
		int direction=0;
		int diffX = destination.getX() - source.getX();
		int diffY = destination.getY()- source.getY();
		
		if (diffX >0  && diffY>0 )	return Direction.NORTHEAST;
		else if (diffX>0 && diffY <0 )	return Direction.SOUTHEAST;
		else if (diffX >0 && diffY ==0)	return Direction.EAST;
		else if (diffX<0 && diffY>0)	return Direction.NORTHWEST;
		else if (diffX<0 && diffY<0)	return Direction.SOUTHWEST;
		else if (diffX<0 && diffY ==0)	return Direction.WEST;
		return 0;
	}
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
	
	
		boolean blnLOver = false;
		boolean blnROver = false;
		 //arbitrary diagonal initial last move dir
	    int lastXMove = -1;
	    int lastYMove = -1;
		
		Info.updateLocalView('r', aintViewR);
		Info.updateLocalView('l', aintViewL);
		
		boolean seeLeftExit = false;
        boolean seeRightExit = false;
        if (Info.scanMap(aintViewL, MapData.exit) != null) seeLeftExit = true;
		if (Info.scanMap(aintViewR, MapData.exit)!=null) seeRightExit = true;
		
		LinkedList<Node> pathL = Info.aStar2(info.aintGlobalViewL);
		LinkedList<Node> pathR = Info.aStar2(info.aintGlobalViewR);
		
		
		
		if (seeLeftExit && seeRightExit)
		{
		int decision = (int) (Math.random() * 2);
		
		if (decision ==1)
			returnDirection (Info.scanMap(Info.aintGlobalViewL, MapData.playerPosition), pathL.getLast());
		else
			returnDirection (Info.scanMap(Info.aintGlobalViewR, MapData.playerPosition), pathR.getLast());
		}
		else
		{
			Random rdmTemp = new Random();
	        int intDeltaX;
	        int intDeltaY;
	        // 80% do last move again, unless wall
	        if ( !blnLOver )
	        {
	            if(aintViewL[aintViewL.length / 2 + lastYMove][aintViewL.length / 2 + lastXMove] == 1 || rdmTemp.nextInt( 5 ) == 4){
	                //favor diagonals
	                do{
	                    intDeltaX = rdmTemp.nextInt(5) - 2;
	                    if(intDeltaX < 0)
	                        intDeltaX = -1;
	                    else if(intDeltaX > 0)
	                        intDeltaX = 1;
	                    intDeltaY = rdmTemp.nextInt(5) - 2;
	                    if(intDeltaY < 0)
	                        intDeltaY = -1;
	                    else if(intDeltaY > 0)
	                        intDeltaY = 1;
	                } while (aintViewL[ aintViewL.length / 2 + intDeltaY ][ aintViewL.length 
	/ 2 + intDeltaX ] == 1 || (intDeltaY == 0 && intDeltaX == 0));
	                System.out.println(MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ]);
	                return MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ];
	            }
	        }
	        else
	        {
	            if(aintViewR[aintViewR.length / 2 + lastYMove][aintViewR.length / 2 + lastXMove] == 1 || rdmTemp.nextInt( 5 ) == 4){
	                do{
	                    intDeltaX = rdmTemp.nextInt(5) - 2;
	                    if(intDeltaX < 0)
	                        intDeltaX = -1;
	                    else if(intDeltaX > 0)
	                        intDeltaX = 1;
	                    intDeltaY = rdmTemp.nextInt(5) - 2;
	                    if(intDeltaY < 0)
	                        intDeltaY = -1;
	                    else if(intDeltaY > 0)
	                        intDeltaY = 1;
	                } while (aintViewR[ aintViewR.length / 2 + intDeltaY ][ aintViewR.length 
	/ 2 + intDeltaX ] == 1 || (intDeltaY == 0 && intDeltaX == 0));
	                return MUMap.aintMToD[ intDeltaY + 1 ][ intDeltaX + 1 ];
	            }
	        }
	        return MUMap.aintMToD[ lastYMove + 1 ][ lastXMove + 1 ];
	    }
		
		return 0;
	}

}
