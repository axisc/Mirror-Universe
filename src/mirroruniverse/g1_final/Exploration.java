package mirroruniverse.g1_final;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.g1_final.Info;


public class Exploration {

	private int[][] lArrPossiblyConnecting;
	private int[][] rArrPossiblyConnecting;
	private int globalX, globalY;
	private ArrayList<Integer> actionPath;
	private int nodeNumber;
	int[][] mymap;
	char chosenSide, badPlayer;
	Node_NonExit nodeNonExit;
	Node_Single nodeSingle;

	boolean leftExitTotallyExplored = false;
	boolean rightExitTotallyExplored = false;

	boolean backtracking = false;

	int next = 0; //for backtracking keeping path state


	int turns = 0; //for debugging - jumping to a specific turn


	private ArrayList<Coord> lALPossiblyConnecting, rALPossiblyConnecting;

	boolean leftFinished, rightFinished;

	Coord target;

	public Exploration(){
		lArrPossiblyConnecting = new int[Info.GlobalViewL.length][Info.GlobalViewL.length];
		rArrPossiblyConnecting = new int[Info.GlobalViewL.length][Info.GlobalViewL.length];

		lALPossiblyConnecting = new ArrayList<Coord>();
		rALPossiblyConnecting = new ArrayList<Coord>();

		leftFinished = false;
		rightFinished = false;
		badPlayer = 'l';

		target = null;
		nodeNumber = 0;
		actionPath = null;
		nodeNonExit = null;
		nodeSingle = null;

	}

	

	public boolean isMoveLegal(int direction){
		boolean retValue =  true;
		int lastXMove = MUMap.aintDToM [direction][0];
		int lastYMove = MUMap.aintDToM [direction][1];

		if (Info.LocalViewR [Info.LocalViewR.length / 2 + lastYMove][Info.LocalViewR.length / 2 + lastXMove] == MapData.EXIT){
			retValue = false;
			badPlayer = 'r';
		}
		if (Info.LocalViewL[Info.LocalViewL.length / 2 + lastYMove][Info.LocalViewL.length / 2 + lastXMove]== MapData.EXIT){
			retValue = false;
			badPlayer = 'l';
		}

		if (Config.DEBUG) System.out.println("Returning value " + retValue);
		return retValue;
	}



	public static boolean notWall( int intDeltaX,int intDeltaY){
		if (Info.LocalViewL[ Info.LocalViewL.length/ 2 + intDeltaY ][ Info.LocalViewL.length / 2 + intDeltaX ] == 1 
				|| (intDeltaY == 0 && intDeltaX == 0) || Info.LocalViewR[Info.LocalViewL.length / 2 + intDeltaY][Info.LocalViewL.length / 2 + intDeltaX] ==1)
			return false;

		else return true;
	}

	
//	/*
//	 * Implements a random Move.Making sure 
//	 * that a 0 move is not returned. 
//	 * Also ensures that a legal move is 
//	 * returned.
//	 */
//	public static int randomMove (){
//
//		Random rdmTemp = new Random();
//		int d=0;
//		int nextX =0 ,nextY = 0;
//
//		do{
//			nextX = rdmTemp.nextInt(3);
//			nextY = rdmTemp.nextInt(3);
//
//			d = MUMap.aintMToD[nextX][nextY];
//		} while (d==0 && isMoveLegal(d) );
//
//		System.out.println("Next move is :" + MUMap.aintDToM[d][0] + " "
//				+ MUMap.aintDToM[d][1]);
//		return d;
//
//	}

	/*
	 * Look at view from each player and determine which are the newly explored squares
	 * If any of those new squares are 0, add them to list 
	 * Remove from list any squares not on the outside of our view radius (incl player square)
	 */
	public void updatePossibleConnects(int[][] lLocalView, int[][] rLocalView){
		turns++;
		int index = -1;



		//		if(Mirrim2.seeLeftExit) 
		//			leftFinished = true;
		//		if(Mirrim2.seeRightExit) 
		//			rightFinished = true;





		if(!leftFinished){
			//removing inner cells from list
			for(int i = 1; i < lLocalView.length - 1; i++){
				for(int j = 1; j < lLocalView.length - 1; j++){
					if((index = lALPossiblyConnecting.indexOf(new Coord(j+Info.getCurrLX() - lLocalView.length/2, i+Info.getCurrLY() - lLocalView.length/2, 'l'))) != -1){
						lArrPossiblyConnecting[j+Info.getCurrLY() - lLocalView.length/2][i+Info.getCurrLX() - lLocalView.length/2] = 0;
						if(lALPossiblyConnecting.get(index).equals(target))
							target = null;
						lALPossiblyConnecting.remove(index);
					}
				}
			}
			//adding appropriate outer cells to list
			for(int i = 0; i < lLocalView.length; i += lLocalView.length - 1){
				for(int j = 0; j < lLocalView.length; j++){
					if(Info.GlobalViewL[j+Info.getCurrLY() - lLocalView.length/2][i+Info.getCurrLX() - lLocalView.length/2] == 4 && lLocalView[j][i] == 0){
						lArrPossiblyConnecting[j+Info.getCurrLY() - lLocalView.length/2][i+Info.getCurrLX() - lLocalView.length/2] = 1;
						lALPossiblyConnecting.add(new Coord(i+Info.getCurrLX() - lLocalView.length/2, j+Info.getCurrLY() - lLocalView.length/2, 'l'));
					}
				}
			}
			for(int j = 0; j < lLocalView.length; j += lLocalView.length - 1){
				for(int i = 1; i < lLocalView.length - 1; i++){
					if(Info.GlobalViewL[j+Info.getCurrLY() - lLocalView.length/2][i+Info.getCurrLX() - lLocalView.length/2] == 4 && lLocalView[j][i] == 0){
						lArrPossiblyConnecting[j+Info.getCurrLY() - lLocalView.length/2][i+Info.getCurrLX() - lLocalView.length/2] = 1;
						lALPossiblyConnecting.add(new Coord(i+Info.getCurrLX() - lLocalView.length/2, j+Info.getCurrLY() - lLocalView.length/2, 'l'));
					}
				}
			}
			leftFinished = lALPossiblyConnecting.isEmpty();
		}
		if(!rightFinished){
			for(int i = 1; i < rLocalView.length - 1; i++){
				for(int j = 1; j < rLocalView.length - 1; j++){
					if((index = rALPossiblyConnecting.indexOf(new Coord(j+Info.getCurrRX() - rLocalView.length/2, i+Info.getCurrRY() - rLocalView.length/2, 'r'))) != -1){
						rArrPossiblyConnecting[j+Info.getCurrRY() - rLocalView.length/2][i+Info.getCurrRX() - rLocalView.length/2] = 1;
						if(rALPossiblyConnecting.get(index).equals(target))
							target = null;
						rALPossiblyConnecting.remove(index);
					}
				}
			}
			for(int i = 0; i < rLocalView.length; i += rLocalView.length - 1){
				for(int j = 0; j < rLocalView.length; j++){
					if(Info.GlobalViewR[j+Info.getCurrRY() - rLocalView.length/2][i+Info.getCurrRX() - rLocalView.length/2] == 4 && rLocalView[j][i] == 0){
						rArrPossiblyConnecting[j+Info.getCurrRY() - rLocalView.length/2][i+Info.getCurrRX() - rLocalView.length/2] = 1;
						rALPossiblyConnecting.add(new Coord(i+Info.getCurrRX() - rLocalView.length/2, j+Info.getCurrRY() - rLocalView.length/2, 'r'));
					}
				}
			}
			for(int j = 0; j < rLocalView.length; j += rLocalView.length - 1){
				for(int i = 1; i < rLocalView.length - 1; i++){
					if(Info.GlobalViewR[j+Info.getCurrRY() - rLocalView.length/2][i+Info.getCurrRX() - rLocalView.length/2] == 4 && rLocalView[j][i] == 0){
						rArrPossiblyConnecting[j+Info.getCurrRY() - rLocalView.length/2][i+Info.getCurrRX() - rLocalView.length/2] = 1;
						rALPossiblyConnecting.add(new Coord(i+Info.getCurrRX() - rLocalView.length/2, j+Info.getCurrRY() - rLocalView.length/2, 'r'));
					}
				}
			}
			rightFinished = rALPossiblyConnecting.isEmpty();
		}

	}

	private Coord seenAllSquaresAround(int x, int y, int[][] global, char side){
		int tolerance = 5;
		Coord c;
		for(int i = -1*tolerance ; i <= tolerance; i++){
			for(int j = -1*tolerance; j <= tolerance; j++){
				if(!(x + i < 0 || y + j < 0 || x + i >= global[0].length || j >= global.length)){
					if(side == 'l' && lALPossiblyConnecting.contains((c = new Coord(i + x, j + y, 'l')))){
						return c;
					}
					else if(side == 'r' && rALPossiblyConnecting.contains((c = new Coord(i + x, j + y, 'r'))))
						return c;
				}
			}
		}
		return null;
	}



	/*
	 * If any new squares were added, pick one of them and make it the target
	 * Otherwise, pick one square from possiblyConnecting list and set it as target
	 * While target remains in list and not at target, use astar to move towards target
	 */
	public int explore(int[][] lLocalView, int[][] rLocalView, int lastDirection){
		//TODO make more intelligent (both able to move, etc)
		//if target is no longer possibly connecting, generate new target

		//for debugging to a specific turn
		if(turns > 680){
			int asdf = 5;
			asdf++;
		}
		

		//checking if somehow we are standing on the target
		if(target != null){
			if(!leftFinished){
				if(target.getX() == Info.currLX && target.getY() == Info.currLY){
					lALPossiblyConnecting.remove(target);
					target = null;
				}
			}
			if(!rightFinished){
				if(target.getX() == Info.currRX && target.getY() == Info.currRY){
					rALPossiblyConnecting.remove(target);
					target = null;
				}
			}
		}


		int i = 0;
		int j = 0;
		int mynext = 0;
		
		
		if(target == null || !(lALPossiblyConnecting.contains(target) || rALPossiblyConnecting.contains(target))
				|| nodeNumber >= actionPath.size() ){
			Collections.sort(lALPossiblyConnecting);
			Collections.sort(rALPossiblyConnecting);

			//finds new target by shortest euclidean distance 
			do{
				if((i >= lALPossiblyConnecting.size() && j >= rALPossiblyConnecting.size()) ){
					System.out.println("BACKTRACKING");
					backtracking = true;
					i = 0;
					j = 0;
				}

				if(!(Mirrim2.leftExitPath && Mirrim2.rightExitPath)){
					if(Mirrim2.leftExitPath){
						chooseRightSide(j);
						i = lALPossiblyConnecting.size();
					}
					else if(Mirrim2.rightExitPath){
						chooseLeftSide(i);
						j = rALPossiblyConnecting.size();
					}
					else{
						compareSides(i, j);
					}
					if(!backtracking){
						nodeNonExit = null;
						AStar_Single s = new AStar_Single(globalX, globalY, target.x, target.y, mymap);
						nodeSingle = s.findNonExitPath();
						if(nodeSingle == null){
							if(chosenSide == 'l')
								i++;
							else
								j++;
							continue;
						}
						actionPath = nodeSingle.getActionPath();
					}
					else{
						nodeSingle = null;
						AStar_NonExit s;
						//set new path
						if(chosenSide == 'l'){
							s = new AStar_NonExit(Info.currLX, Info.currLY, target.x, target.y, 
									Info.currRX, Info.currRY, Info.GlobalViewL, Info.GlobalViewR);
						}
						else{
							s = new AStar_NonExit(Info.currRX, Info.currRY, target.x, target.y, 
									Info.currLX, Info.currLY, Info.GlobalViewR, Info.GlobalViewL);
						}
						nodeNonExit = s.findNonExitPath();

						if(nodeNonExit == null){
							if(chosenSide == 'l')
								i++;
							else
								j++;
							continue;
						}
						actionPath = nodeNonExit.getActionPath();
					}
				}
				else{
					nodeSingle = null;
					if(leftFinished){
						chooseRightSide(j);
						i = lALPossiblyConnecting.size();
					}
					else if(rightFinished){
						chooseLeftSide(i);
						j = rALPossiblyConnecting.size();
					}
					else{
						compareSides(i, j);
					}
					if(!backtracking){
						nodeNonExit = null;
						AStar_Single s = new AStar_Single(globalX, globalY, target.x, target.y, mymap);
						nodeSingle = s.findNonExitPath();
						if(nodeSingle == null){
							if(chosenSide == 'l')
								i++;
							else
								j++;
							continue;
						}
						actionPath = nodeSingle.getActionPath();
					}
					else{
						if(Mirrim2.seeLeftExit && !leftExitTotallyExplored){
							target = seenAllSquaresAround(Info.currLX, Info.currLY, Info.GlobalViewL, 'l');
							if((leftExitTotallyExplored = target == null)) continue;
						}
						else if(Mirrim2.seeRightExit && !rightExitTotallyExplored){
							target = seenAllSquaresAround(Info.currLX, Info.currLY, Info.GlobalViewR, 'r');
							if((rightExitTotallyExplored = target == null)) continue;
						}
						nodeSingle = null;
						AStar_NonExit s;
						//set new path
						if(chosenSide == 'l'){
							s = new AStar_NonExit(Info.currLX, Info.currLY, target.x, target.y, 
									Info.currRX, Info.currRY, Info.GlobalViewL, Info.GlobalViewR);
						}
						else{
							s = new AStar_NonExit(Info.currRX, Info.currRY, target.x, target.y, 
									Info.currLX, Info.currLY, Info.GlobalViewR, Info.GlobalViewL);
						}
						nodeNonExit = s.findNonExitPath();

						if(nodeNonExit == null){
							if(chosenSide == 'l')
								i++;
							else
								j++;
							continue;
						}
						actionPath = nodeNonExit.getActionPath();
					}
				}


				nodeNumber = 0;
				mynext = actionPath.get(nodeNumber);
				nodeNumber++;

				//dont step on exit checks
				if(!isMoveLegal(mynext)) {
					if(chosenSide == 'l')
						i++;
					else
						j++;
					continue;
				}
			}
			//dont step on exit looping
			while((nodeNonExit == null && nodeSingle == null) || !isMoveLegal(mynext));
		}
		else{
			//check for accidental exit
			mynext = actionPath.get(nodeNumber);
			nodeNumber++;
			if(!isMoveLegal(mynext)){
				target = null;
				return explore(lLocalView, rLocalView, lastDirection);
			}
		}
		return mynext;
	}

	public boolean isLeftFinished() {
		return leftFinished;
	}

	public boolean isRightFinished() {
		return rightFinished;
	}


	private void chooseRightSide(int j){
		target = rALPossiblyConnecting.get(j);
		mymap = Info.GlobalViewR;
		globalX = Info.getCurrRX();
		globalY = Info.getCurrRY();
		chosenSide = 'r';
	}

	private void chooseLeftSide(int i){
		target = lALPossiblyConnecting.get(i);
		mymap = Info.GlobalViewL;
		globalX = Info.getCurrLX();
		globalY = Info.getCurrLY();
		chosenSide = 'l';
	}

	private void compareSides(int i, int j){
		if(i >= lALPossiblyConnecting.size()){
			chooseLeftSide(i);
		}
		else if(j >= rALPossiblyConnecting.size()){
			chooseRightSide(j);
		}
		else{
			if(lALPossiblyConnecting.get(i).compareTo(rALPossiblyConnecting.get(j)) < 0){
				chooseLeftSide(i);
			}
			else{
				chooseRightSide(j);
			}
		}
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
		if(side == 'l'){
			globalX = Info.getCurrLX();
			globalY = Info.getCurrLY();
		}
		else{
			globalX = Info.getCurrRX();
			globalY = Info.getCurrRY();
		}
	}

	//TODO should side be included in the equals?
	@Override
	public boolean equals(Object c){
		if(c == null)
			return false;
		return y == ((Coord) c).getY() && x == ((Coord) c).getX();
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


	public int getGlobalX() {
		return globalX;
	}

	public int getGlobalY() {
		return globalY;
	}

	//by euclidean distance
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
		if(Math.sqrt(Math.pow(x - (globalX), 2) + Math.pow(y - (globalY), 2)) 
				< Math.sqrt(Math.pow(c.getX() - (c.getGlobalX()), 2) + Math.pow(c.getY() - (c.getGlobalY()), 2))){
			return -1;
		}
		else{
			return 1;
		}
	}

}