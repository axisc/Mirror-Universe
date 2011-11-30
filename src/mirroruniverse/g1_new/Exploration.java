package mirroruniverse.g1_new;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.g1_new.Info;


public class Exploration {
	
	private int[][] lArrPossiblyConnecting;
	private int[][] rArrPossiblyConnecting;
		
	private ArrayList<Coord> lALPossiblyConnecting, rALPossiblyConnecting;
	
	boolean leftFinished, rightFinished;
	
	Coord target;
	
	public Exploration(){
		lArrPossiblyConnecting = new int[199][199];
		rArrPossiblyConnecting = new int[199][199];

		lALPossiblyConnecting = new ArrayList<Coord>();
		rALPossiblyConnecting = new ArrayList<Coord>();
		
		leftFinished = false;
		rightFinished = false;
		
		target = null;
	}
	
	/*
	 * Implements a random Move.Making sure 
	 * that a 0 move is not returned. 
	 */
	
	public boolean isMoveLegal(int direction){
		boolean retValue =  true;
		int lastYMove = MUMap.aintDToM [direction][0];
		int lastXMove = MUMap.aintDToM [direction][1];
		
		if (!G1Player.seeLeftExit && Info.aintLocalViewR [Info.aintLocalViewR.length / 2 + lastXMove][Info.aintLocalViewR.length / 2 + lastYMove] == MapData.exit)
			retValue = false;
		if (!G1Player.seeRightExit && Info.aintLocalViewL[Info.aintLocalViewL.length / 2 + lastXMove][Info.aintLocalViewL.length / 2 + lastYMove]== MapData.exit)
			retValue = false;
		
		System.out.println("Returning value " + retValue);
		return retValue;
	}
	public int randomMove (){
		
		Random rdmTemp = new Random();
		int d=0;
		int nextX =0 ,nextY = 0;
				
		do{
			nextX = rdmTemp.nextInt(3);
			nextY = rdmTemp.nextInt(3);
	
			d = MUMap.aintMToD[nextX][nextY];
		} while (d==0 );
		
			System.out.println("Next move is :" + MUMap.aintDToM[d][0] + " "
					+ MUMap.aintDToM[d][1]);
		return d;

	}
	
	/*
	 * Look at view from each player and determine which are the newly explored squares
	 * If any of those new squares are 0, add them to list 
	 * Remove from list any squares not on the outside of our view radius (incl player square)
	 */
	public void updatePossibleConnects(int[][] lLocalView, int[][] rLocalView){
		int index = -1;
		if(!leftFinished){
			//removing inner cells from list
			for(int i = 1; i < lLocalView.length - 1; i++){
				for(int j = 1; j < lLocalView.length - 1; j++){
					if((index = lALPossiblyConnecting.indexOf(new Coord(j+99+Info.getCurrLX(), i+99+Info.getCurrLY(), 'l'))) != -1){
						lArrPossiblyConnecting[i+99+Info.getCurrLX()][j+99+Info.getCurrLY()] = 0;
						if(lALPossiblyConnecting.get(index).equals(target))
							target = null;
						lALPossiblyConnecting.remove(index);
					}
				}
			}
			//adding appropriate outer cells to list
			for(int i = 0; i < lLocalView.length; i += lLocalView.length - 1){
				for(int j = 0; j < lLocalView.length; j += lLocalView.length - 1){
					if(Info.aintGlobalViewL[i+99+Info.getCurrLX()][j+99+Info.getCurrLY()] == 4 && lLocalView[i][j] == 0){
						lArrPossiblyConnecting[i+99+Info.getCurrLX()][j+99+Info.getCurrLY()] = 1;
						if(rALPossiblyConnecting.get(index).equals(target))
							target = null;
						lALPossiblyConnecting.add(new Coord(i+99+Info.getCurrLX(), j+99+Info.getCurrLY(), 'l'));
					}
				}
			}
			leftFinished = lALPossiblyConnecting.isEmpty();
		}
		if(!rightFinished){
			for(int i = 1; i < rLocalView.length - 1; i++){
				for(int j = 1; j < rLocalView.length - 1; j++){
					if((index = rALPossiblyConnecting.indexOf(new Coord(j+99+Info.getCurrRX(), i+99+Info.getCurrRY(), 'r'))) != -1){
						rArrPossiblyConnecting[i+99+Info.getCurrRX()][j+99+Info.getCurrRY()] = 1;
						rALPossiblyConnecting.remove(index);
					}
				}
			}
			for(int i = 0; i < rLocalView.length; i += rLocalView.length - 1){
				for(int j = 0; j < rLocalView.length; j += rLocalView.length - 1){
					if(Info.aintGlobalViewR[i+99+Info.getCurrRX()][j+99+Info.getCurrRY()] == 4 && rLocalView[i][j] == 0){
						rArrPossiblyConnecting[i+99+Info.getCurrRX()][j+99+Info.getCurrRY()] = 1;
						rALPossiblyConnecting.add(new Coord(i+99+Info.getCurrRX(), j+99+Info.getCurrRY(), 'r'));
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
		//TODO make more intelligent (both able to move, etc)
		//if target is no longer possibly connecting, generate new target
		if(target == null || !(lALPossiblyConnecting.contains(target) || rALPossiblyConnecting.contains(target))){
			Collections.sort(lALPossiblyConnecting);
			Collections.sort(rALPossiblyConnecting);
			if(!leftFinished && !rightFinished){
				if(lALPossiblyConnecting.get(0).compareTo(rALPossiblyConnecting.get(0)) < 0)
					target = lALPossiblyConnecting.get(0);
				else
					target = rALPossiblyConnecting.get(0);
			}
			else if(!leftFinished)
				target = lALPossiblyConnecting.get(0);
			else
				target = rALPossiblyConnecting.get(0);
		}
		
		
		//TODO astar to target
		
		
		return 1;
	}

	public boolean isLeftFinished() {
		return leftFinished;
	}

	public boolean isRightFinished() {
		return rightFinished;
	}
	
	
	
	/*
	 * TODO
	 * Add more functions for a more intelligent exploration strategy. 
	 * They can take any parameters from any of the classes written by us.
	 * HOWEVER, IT IS IMPORTANT THAT THEY RETURN AN INTEGER.
	 * 
	 * The function may or may not be static - its not important.
	 */
}

class Coord implements Comparable<Coord>{
	
	int x,y, globalX, globalY;
	char side;
	
	public Coord(int x, int y, char side){
		this.y = y;
		this.x = x;
		this.side = side;
	}
	
	//TODO should side be included in the equals?
	public boolean equals(Coord c){
		return y == c.getY() && x == c.getX();
	}
	
	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public char getSide(){
		return side;
	}

	@Override
	public int compareTo(Coord c) {
		if(this.equals(c))
			return 0;
		if(side == 'l'){
			globalX = Info.getCurrLX();
			globalY = Info.getCurrLY();
		}
		else{
			globalX = Info.getCurrRX();
			globalY = Info.getCurrRY();
		}
		if(Math.sqrt(Math.pow(x - (99 + globalX), 2) + Math.pow(y - (99 + globalY), 2)) 
				< Math.sqrt(Math.pow(c.getX() - (99 + globalX), 2) + Math.pow(c.getY() - (99 + globalY), 2))){
			return -1;
		}
		else{
			return 1;
		}
	}
	
}
