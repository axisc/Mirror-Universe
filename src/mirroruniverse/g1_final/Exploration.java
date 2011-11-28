package mirroruniverse.g1_final;

import java.util.Map;
import java.util.Random;

import mirroruniverse.g1_final.Info;
import mirroruniverse.g1_final.MapData;
import mirroruniverse.g1_final.Mirrim;
import mirroruniverse.sim.MUMap;

public class Exploration {

	
	/*
	 * This move checks where the new direction lands 
	 * the player, if executed.
	 */
	public static int whereWillThisDirectionTakeMe(char side, int direction){
		int result = 0;
		int lastXMove = MUMap.aintDToM[direction][0];
		int lastYMove = MUMap.aintDToM[direction][1];
		
		if (side == 'r')
		result = Info.LocalViewR [Info.LocalViewR.length / 2 + lastXMove][Info.LocalViewR.length / 2 + lastYMove];
		else
			result = Info.LocalViewL [Info.LocalViewL.length / 2 + lastXMove][Info.LocalViewL.length / 2 + lastYMove];
		
		switch (result){
		case MapData.EXIT :
			return MapData.EXIT;
		case MapData.FREESPACE :
			return MapData.FREESPACE;
		case MapData.WALL : 
			return MapData.WALL;
		default :
			return result;
		}
	}
	
	/*
	 * Check if the move is null.
	 */
	public static boolean nullMove(int direction){
		if (direction == 0) 
			return false;
		else
			return true;
	}
	
	/*
	 * Check if the Move is legal.
	 */
	public static boolean legalMove(int direction){
		boolean retValue = true ;
		int lastXMove = MUMap.aintDToM[direction][0];
		int lastYMove = MUMap.aintDToM[direction][1];
		
		/*
		 * If one player has seen the exit, he shouldn't be allowed to
		 * step on it till the other player sees the exit. 
		 */
		if (!Info.endGameStrategy && !Mirrim.seeLeftExit && Info.LocalViewR [Info.LocalViewR.length / 2 + lastXMove][Info.LocalViewR.length / 2 + lastYMove] == MapData.EXIT)
			retValue = false;
		if (!Info.endGameStrategy && !Mirrim.seeRightExit && Info.LocalViewL[Info.LocalViewL.length / 2 + lastXMove][Info.LocalViewL.length / 2 + lastYMove]== MapData.EXIT)
			retValue = false;
		
		if (Info.LocalViewR [Info.LocalViewR.length / 2 + lastXMove][Info.LocalViewR.length / 2 + lastYMove] == MapData.OBSTACLE)
			retValue = false;
		if (Info.LocalViewL [Info.LocalViewL.length / 2 + lastXMove][Info.LocalViewL.length / 2 + lastYMove] == MapData.OBSTACLE)
			retValue = false;

		return retValue;
	}
	
	
	/*
	 * Returns a random move, except for a Null Move
	 */
	public static int randomMove(){
		
		Random rdm = new Random();
		int d = 0;
		int nextX = 0, nextY = 0;
		
		do {
			nextX = rdm.nextInt(3);
			nextY = rdm.nextInt(3);
			
			d = MUMap.aintMToD[nextX][nextY];
		}while ( !nullMove(d) || !legalMove(d));
		
		return d;
		
	}
}
