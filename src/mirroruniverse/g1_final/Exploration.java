package mirroruniverse.g1_final;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import mirroruniverse.g1_final.Info;
import mirroruniverse.g1_final.MapData;
import mirroruniverse.g1_final.Mirrim;
import mirroruniverse.sim.MUMap;

public class Exploration {

	private int[][] lArrPossiblyConnecting;
	private int[][] rArrPossiblyConnecting;
		
	private ArrayList<Coord> lALPossiblyConnecting, rALPossiblyConnecting;
	
	boolean leftFinished, rightFinished;
	
	Coord target;
	
	public Exploration(){
		lArrPossiblyConnecting = new int[198][198];
		rArrPossiblyConnecting = new int[198][198];

		lALPossiblyConnecting = new ArrayList<Coord>();
		rALPossiblyConnecting = new ArrayList<Coord>();
		
		leftFinished = false;
		rightFinished = false;
		
		target = null;
	}
	
	/*
	 * Look at view from each player and determine which are the newly explored squares
	 * If any of those new squares are 0, add them to list 
	 * Remove from list any squares not on the outside of our view radius (incl player square)
	 */
	public void updatePossibleConnects(int[][] lLocalView, int[][] rLocalView){
		//removing inner cells from list
		int index = -1;
		if(!leftFinished){
			for(int i = 1; i < lLocalView.length - 1; i++){
				for(int j = 1; j < lLocalView.length - 1; j++){
					if((index = lALPossiblyConnecting.indexOf(new Coord(i+99+Info.getCurrLY(), j+99+Info.getCurrLX()))) != -1){
						lArrPossiblyConnecting[j+99+Info.getCurrLY()][i+99+Info.getCurrLX()] = 0;
						if(lALPossiblyConnecting.get(index).equals(target))
							target = null;
						lALPossiblyConnecting.remove(index);
					}
				}
			}
			for(int i = 0; i < lLocalView.length; i += lLocalView.length - 1){
				for(int j = 0; j < lLocalView.length; j += lLocalView.length - 1){
					if(Info.GlobalViewL[j+99+Info.getCurrLY()][i+99+Info.getCurrLX()] == 4 && lLocalView[j][i] == 0){
						lArrPossiblyConnecting[j+99+Info.getCurrLY()][i+99+Info.getCurrLX()] = 1;
						if(rALPossiblyConnecting.get(index).equals(target))
							target = null;
						lALPossiblyConnecting.add(new Coord(j+99+Info.getCurrLY(), i+99+Info.getCurrLX()));
					}
				}
			}
			leftFinished = lALPossiblyConnecting.isEmpty();
		}
		if(!rightFinished){
			for(int i = 1; i < rLocalView.length - 1; i++){
				for(int j = 1; j < rLocalView.length - 1; j++){
					if((index = rALPossiblyConnecting.indexOf(new Coord(i+99+Info.getCurrRY(), j+99+Info.getCurrRX()))) != -1){
						rArrPossiblyConnecting[j+99+Info.getCurrRY()][i+99+Info.getCurrRX()] = 1;
						rALPossiblyConnecting.remove(index);
					}
				}
			}
			for(int i = 0; i < rLocalView.length; i += rLocalView.length - 1){
				for(int j = 0; j < rLocalView.length; j += rLocalView.length - 1){
					if(Info.GlobalViewR[j+99+Info.getCurrRY()][i+99+Info.getCurrRX()] == 4 && rLocalView[j][i] == 0){
						rArrPossiblyConnecting[j+99+Info.getCurrRY()][i+99+Info.getCurrRX()] = 1;
						rALPossiblyConnecting.add(new Coord(j+99+Info.getCurrRY(), i+99+Info.getCurrRX()));
					}
				}
			}
			rightFinished = rALPossiblyConnecting.isEmpty();
		}
	}
	


	 /*
	 * If any new squares were added, pick one of them and make it the target
	 * Otherwise, pick one square from possiblyConnecting list and set it as target
	 * While target remains in list and not at target, use astar to move towards target
	 */
	public int explore(int[][] lLocalView, int[][] rLocalView, int lastDirection){
		//TODO make more intelligent
		if(target == null){
			if(!leftFinished)
				target = lALPossiblyConnecting.get(0);
			else
				target = rALPossiblyConnecting.get(0);
		}
		
		
		//TODO astar to target
		
		
		return 1;
	}

	
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

class Coord{
	
	int x,y;
	
	public Coord(int y, int x){
		this.y = y;
		this.x = x;
	}
	
	public boolean equals(Coord c){
		return y == c.getY() && x == c.getX();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
