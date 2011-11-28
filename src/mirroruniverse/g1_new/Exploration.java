package mirroruniverse.g1_new;

import java.util.ArrayList;
import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.g1_new.Info;


public class Exploration {
	
	private int[][] lArrPossiblyConnecting;
	private int[][] rArrPossiblyConnecting;
		
	private ArrayList<Coord> lALPossiblyConnecting, rALPossiblyConnecting;
	
	boolean leftFinished, rightFinished;
	
	public void init(){
		lArrPossiblyConnecting = new int[198][198];
		rArrPossiblyConnecting = new int[198][198];

		lALPossiblyConnecting = new ArrayList<Coord>();
		rALPossiblyConnecting = new ArrayList<Coord>();
		
		leftFinished = false;
		rightFinished = false;
	}
	
	/*
	 * Implements a random Move.Making sure 
	 * that a 0 move is not returned. 
	 */
	
	public boolean isMoveLegal(int direction){
		boolean retValue =  true;
		int lastYMove = MUMap.aintDToM [direction][0];
		int lastXMove = MUMap.aintDToM [direction][1];
		
		if (!G1Player.seeLeftExit && Info.aintLocalViewR [Info.aintLocalViewR.length / 2 + lastYMove][Info.aintLocalViewR.length / 2 + lastXMove] == MapData.exit)
			retValue = false;
		if (!G1Player.seeRightExit && Info.aintLocalViewL[Info.aintLocalViewL.length / 2 + lastYMove][Info.aintLocalViewL.length / 2 + lastXMove]== MapData.exit)
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
	
	public void updatePossibleConnects(int[][] lLocalView, int[][] rLocalView){
		//removing inner cells from list
		int index;
		if(!leftFinished){
			for(int i = 1; i < lLocalView.length - 1; i++){
				for(int j = 1; j < lLocalView.length - 1; j++){
					if((index = lALPossiblyConnecting.indexOf(new Coord(i+99+Info.getCurrLY(), j+99+Info.getCurrLX()))) != -1){
						lArrPossiblyConnecting[j+99+Info.getCurrLY()][i+99+Info.getCurrLX()] = 0;
						lALPossiblyConnecting.remove(index);
					}
				}
			}
			for(int i = 0; i < lLocalView.length; i += lLocalView.length - 1){
				for(int j = 0; j < lLocalView.length; j += lLocalView.length - 1){
					if(Info.aintGlobalViewL[j+99+Info.getCurrLY()][i+99+Info.getCurrLX()] == 4 && lLocalView[j][i] == 0){
						lArrPossiblyConnecting[j+99+Info.getCurrLY()][i+99+Info.getCurrLX()] = 1;
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
					if(Info.aintGlobalViewR[j+99+Info.getCurrRY()][i+99+Info.getCurrRX()] == 4 && rLocalView[j][i] == 0){
						rArrPossiblyConnecting[j+99+Info.getCurrRY()][i+99+Info.getCurrRX()] = 1;
						rALPossiblyConnecting.add(new Coord(j+99+Info.getCurrRY(), i+99+Info.getCurrRX()));
					}
				}
			}
			rightFinished = rALPossiblyConnecting.isEmpty();
		}
	}
	
	/*
	 * Look at view from each player and determine which are the newly explored squares
	 * If any of those new squares are 0, add them to list 
	 * Remove from list any squares not on the outside of our view radius (incl player square)
	 * If any new squares were added, pick one of them and make it the target
	 * Otherwise, pick one square from possiblyConnecting list and set it as target
	 * While target remains in list and not at target, use astar to move towards target
	 */
	public int explore(int[][] lLocalView, int[][] rLocalView, int lastDirection){
		
		
		
		return 0;
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
