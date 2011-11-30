package mirroruniverse.aiplayer;

import java.util.*;

import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class AiPlayer implements Player
{
	boolean blnLOver = false;
	boolean blnROver = false;
	int round=0;
	int call=0;

	int lPlayer_X_Exit;
	int lPlayer_Y_Exit;
	int rPlayer_X_Exit;
	int rPlayer_Y_Exit;

	int lPlayer_X_Explore;
	int lPlayer_Y_Explore;
	int rPlayer_X_Explore;
	int rPlayer_Y_Explore;

	int lPlayer_X_Position;
	int lPlayer_Y_Position;
	int rPlayer_X_Position;
	int rPlayer_Y_Position;

	ArrayList<Integer> movesList;
	ArrayList<Integer> singleMovesList;
	ArrayList<Integer> minSingleMovesList;


	boolean leftExitFound=false;
	boolean rightExitFound=false;
	boolean leftExitPassed=false;
	boolean rightExitPassed=false;

	int[][] leftMap;
	int[][] rightMap;

	int leftSightRadius;
	int rightSightRadius;

	int maxMapSideLength=30;
	// in the problem this is 100 but currently we are working just for max size to be 50
	int maxVirtualMapsize=maxMapSideLength*2+2;

	int xShiftLeftCumulative=0, yShiftLeftCumulative=0;
	int xShiftRightCumulative=0,yShiftRightCumulative=0;

	int lastMove=0;
   boolean leftExited=false;
   boolean rightExited=false;
	String stateBestLeft,stateBestRight,stateBestBoth,bestDiffExitState;

	public static final int[][] movesArray = { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 },  { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
	public int[][][][] state;
	int[][] miniState;

	/*
	public int[][][][] stateDistance;
	public int[][][][] stateLeftUnknown;
	public int[][][][] stateRightUnknown;*/
	/* these states will store an integer which will have the value of the move through which this state was attained
	 * with the value of the move we can retrace the previous state
	 * (non-Javadoc)
	 * @see mirroruniverse.sim.Player#lookAndMove(int[][], int[][])
	 */

	public int lookAndMove( int[][] aintViewL, int[][] aintViewR )
	{
		round++;
		//System.out.println(java.lang.Runtime.getRuntime().maxMemory()); 
		if( round ==1) {
			singleMovesList=new ArrayList<Integer>();
			minSingleMovesList=new ArrayList<Integer>();
			setLeftRightMap(aintViewL,aintViewR);
			state = new int[maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize];
			updateLeftRightMap(aintViewL,aintViewR);
			setMoves(aintViewL,aintViewR);
		}
		updateLeftRightMap(aintViewL,aintViewR);

		if (movesList.isEmpty()) {
			//state = new int[leftMap[0].length][leftMap.length][rightMap[0].length][rightMap.length];
			state = new int[maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize];
			/*stateDistance = new int[maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize];
		stateLeftUnknown = new int[maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize];
		stateRightUnknown = new int[maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize];
			 */
			//stateDistance = new int[leftMap[0].length][leftMap.length][rightMap[0].length][rightMap.length];

			/// do we have to do above for all rounds 
			setMoves(aintViewL,aintViewR);
		}
		//setMovesRandom(aintViewL,aintViewR);
		//if(movesList.isEmpty()) 
			//movesList.add((round%8)+1);

		lastMove=movesList.remove(0);
		System.out.println("round "+round);
		System.out.println("lastMove "+lastMove);
		System.out.println("movesList "+movesList);
		updateCurrentPosition(lastMove);
		return lastMove;
	}


	public void updateCurrentPosition(int moveActuallyMade) {

		int xShiftLeft=movesArray[moveActuallyMade][0];
		int yShiftLeft=movesArray[moveActuallyMade][1];
		int xShiftRight=xShiftLeft;
		int yShiftRight=yShiftLeft;

		int lPlayer_X_Position_New=lPlayer_X_Position+xShiftLeft;
		int lPlayer_Y_Position_New=lPlayer_Y_Position+yShiftLeft;
		int rPlayer_X_Position_New=rPlayer_X_Position+xShiftRight;
		int rPlayer_Y_Position_New=rPlayer_Y_Position+yShiftRight;
		System.out.println("\trightMap "+rPlayer_Y_Position+"-"+rPlayer_X_Position+"="+rightMap[rPlayer_Y_Position][rPlayer_X_Position]);
		if(leftMap[lPlayer_Y_Position_New][lPlayer_X_Position_New]==2)  {
			System.out.println("Exitting in left map in round "+round+" at "+lPlayer_X_Position_New+","+lPlayer_Y_Position_New);
			leftExited=true;
		}
		if(rightMap[rPlayer_Y_Position_New][rPlayer_X_Position_New]==2) { 
			System.out.println("Exitting in right map in round "+round+" at "+rPlayer_X_Position_New+","+rPlayer_Y_Position_New);
			rightExited=true;
		}
		if(leftMap[lPlayer_Y_Position_New][lPlayer_X_Position_New]==1 || leftExited) {
			xShiftLeft=0;
			yShiftLeft=0;
		} else {
			lPlayer_X_Position=lPlayer_X_Position_New;
			lPlayer_Y_Position=lPlayer_Y_Position_New;
		}

		if(rightMap[rPlayer_Y_Position_New][rPlayer_X_Position_New]==1|| rightExited) {
			xShiftRight=0;
			yShiftRight=0;
		} else {
			rPlayer_X_Position=rPlayer_X_Position_New;
			rPlayer_Y_Position=rPlayer_Y_Position_New;
		}

		xShiftRightCumulative+=xShiftRight;
		yShiftLeftCumulative+=yShiftLeft;
		xShiftLeftCumulative+=xShiftLeft;
		yShiftRightCumulative+=yShiftRight;
	}


	public void setLeftRightMap (int[][] aintViewL, int[][] aintViewR) {
		leftSightRadius=(aintViewL.length-1)/2;
		rightSightRadius=(aintViewR.length-1)/2;
		System.out.println("leftSightRadius= "+leftSightRadius);
		System.out.println("rightSightRadius= "+rightSightRadius);

		leftMap = new int[maxVirtualMapsize][maxVirtualMapsize];
		rightMap = new int[maxVirtualMapsize][maxVirtualMapsize];
		for(int i=0;i<maxVirtualMapsize;i++)
			for(int j=0;j<maxVirtualMapsize;j++) {
				leftMap[i][j]=4;
				rightMap[i][j]=4;
			}
		// 4 means unseen
		lPlayer_X_Position=(leftMap[0].length-1)/2;
		lPlayer_Y_Position=(leftMap.length-1)/2;
		rPlayer_X_Position=(rightMap[0].length-1)/2;
		rPlayer_Y_Position=(rightMap.length-1)/2;

	}


	public void updateLeftRightMap(int[][] aintViewL, int[][] aintViewR) {
		int deltaX,deltaY;
		deltaX=(leftMap[0].length-1)/2-(aintViewL[0].length-1)/2;
		deltaY=(leftMap.length-1)/2 -(aintViewL.length-1)/2;
		for(int y=0;y<aintViewL.length;y++) {
			for(int x=0;x<aintViewL[0].length;x++)  {
				int yIndex=y+deltaY+yShiftLeftCumulative;
				int xIndex=x+deltaX+xShiftLeftCumulative;
				if(yIndex<0 ||yIndex>=maxVirtualMapsize || xIndex<0 ||xIndex>=maxVirtualMapsize) 
					continue;
				if (aintViewR[y][x]==2) {
					leftExitPassed=true;
				}
				leftMap[yIndex][xIndex]=aintViewL[y][x];
				//System.out.print(" "+leftMap[y+deltaY][x+deltaX]);
			}
			//System.out.println("");
		}
		System.out.println("\n\n");
		deltaX=(rightMap[0].length-1)/2-(aintViewR[0].length-1)/2;
		deltaY=(rightMap.length-1)/2 -(aintViewR.length-1)/2;
		for(int y=0;y<aintViewR.length;y++) {
			for(int x=0;x<aintViewR[0].length;x++)  {
				int yIndex=y+deltaY+yShiftRightCumulative;
				int xIndex=x+deltaX+xShiftRightCumulative;
				if(yIndex<0 ||yIndex>=maxVirtualMapsize || xIndex<0 ||xIndex>=maxVirtualMapsize) 
					continue;
				if (aintViewR[y][x]==2) {
					rightExitPassed=true;
				}
				rightMap[yIndex][xIndex]=aintViewR[y][x];
				System.out.print(" "+aintViewR[y][x]);
			}
			System.out.println("");
		}
		System.out.println("deltaX="+deltaX);
		System.out.println("deltaY="+deltaY);
		System.out.println("xShiftRightCumulative="+xShiftRightCumulative);
		System.out.println("yShiftRightCumulative="+yShiftRightCumulative);
		/*
		for(int y=0;y<leftMap.length;y++) {
			for(int x=0;x<leftMap[0].length;x++)  {
				System.out.print(" "+leftMap[y][x]);
			}
			System.out.println("");
		}
		 */
		System.out.println("\n\n");
		for(int y=0;y<rightMap.length;y++) {
			for(int x=0;x<rightMap[0].length;x++)  {
				System.out.print(" "+rightMap[y][x]);
			}
			System.out.println("");
		}
		System.out.println("maps printed");


	}

	public void setMovesRandom(int[][] aintViewL, int[][] aintViewR ) {
		movesList=new ArrayList<Integer>();
		movesList.add((round%8)+1);
	}

	public void setMoves(int[][] aintViewL, int[][] aintViewR )
	{
		state[lPlayer_X_Position][lPlayer_Y_Position][rPlayer_X_Position][rPlayer_Y_Position]=900;
		//stateDistance[lPlayer_X_Position][lPlayer_Y_Position][rPlayer_X_Position][rPlayer_Y_Position]=0;
		double maximumTotalUnknown=browse(lPlayer_X_Position,lPlayer_Y_Position,rPlayer_X_Position,rPlayer_Y_Position,false);
		System.out.println("maximumTotalUnknown 1 ="+maximumTotalUnknown);
		movesList=new ArrayList<Integer>();
		boolean movesListSet=false;
		if (exitReached()) {
			updateMovesList(lPlayer_X_Exit,lPlayer_Y_Exit,rPlayer_X_Exit,rPlayer_Y_Exit);
			System.out.println("next state chosen  as exit");
			movesListSet=true;

		} else {
			String nextState;
			if (leftExitFound==true && rightExitFound==true) {
				// exits known
				// go towards max total
				if (maximumTotalUnknown>0.0)
					nextState=stateBestBoth;
				else  {
					state = new int[maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize][maxVirtualMapsize];
					state[lPlayer_X_Position][lPlayer_Y_Position][rPlayer_X_Position][rPlayer_Y_Position]=900;
					maximumTotalUnknown=browse(lPlayer_X_Position,lPlayer_Y_Position,rPlayer_X_Position,rPlayer_Y_Position,true);
					movesList.clear();
					movesList.addAll(minSingleMovesList);
					System.out.println("maximumTotalUnknown 2 ="+maximumTotalUnknown);
					System.out.println("minSingleMovesList  ="+minSingleMovesList);
					System.out.println("bestDiffExitState  ="+bestDiffExitState);
					nextState=bestDiffExitState;
					movesListSet=true;
				}


			} else {
				// exits unknown
				if (leftExitFound==true) {
					nextState=stateBestRight;
					// go towards max right
				} else {
					if (rightExitFound==true) {
						// go towards max left
						nextState=stateBestLeft;
					} else {
						nextState=stateBestBoth;
						// go towards max total
					}
				}
			}
			if(!movesListSet) {
				System.out.println("next state chosen "+nextState);
				String[] arrayA=nextState.split(",");
				int x1=Integer.parseInt(arrayA[0]); int y1=Integer.parseInt(arrayA[1]); 
				int x2=Integer.parseInt(arrayA[2]); int y2=Integer.parseInt(arrayA[3]);
				updateMovesList(x1,y1,x2,y2);
			}

		}


	}


	public double browse(int xx1,int yy1,int xx2,int yy2,boolean exitSeperately) {
		int x1,y1,x2,y2,xm1,ym1,xm2,ym2,deltaX,deltaY,leftSame,rightSame,distance;
		distance=0;
		// here distance is measured in moves
		//stateDistance
		// these define the positions after move

		String stateName=xx1+","+yy1+","+xx2+","+yy2+","+distance+",0,0,0";
		stateBestLeft=stateName;
		stateBestRight=stateName;
		stateBestBoth=stateName;
		double maxLeftScore=0,maxRightscore=0,maxTotalScore=0;
		int leftNext,rightNext,singleStepsRequired=Integer.MAX_VALUE,minSingleStepsRequired=Integer.MAX_VALUE;
		ArrayList<String> queueElements=new ArrayList<String>();
		queueElements.add(xx1+","+yy1+","+xx2+","+yy2+","+distance+",0,0,0");
		int oldDistance =distance;
		while(!queueElements.isEmpty()){
			if (exitReached())
				return maxTotalScore;
			stateName=queueElements.remove(0);
			String[] arrayA=stateName.split(",");
			x1=Integer.parseInt(arrayA[0]); y1=Integer.parseInt(arrayA[1]); 
			x2=Integer.parseInt(arrayA[2]); y2=Integer.parseInt(arrayA[3]);
			distance=Integer.parseInt(arrayA[4]);
			if(distance>oldDistance && 	 leftExitPassed==false && rightExitPassed==false) {
				if(maxTotalScore>0)
					return maxTotalScore; 
				else
					oldDistance++;
			}

			distance++;

			System.out.println("\t\t\tbrowsing "+x1+","+y1+","+x2+","+x2+"="+distance);

			for(int moves=1;moves<=8;moves++) {
				deltaX=movesArray[moves][0];
				deltaY=movesArray[moves][1];
				xm1=x1+deltaX;ym1=y1+deltaY;
				xm2=x2+deltaX;ym2=y2+deltaY;
				leftSame=0;
				rightSame=0;
				if(ym1<0 ||ym1>=maxVirtualMapsize || xm1<0 ||xm1>=maxVirtualMapsize) {
					leftNext=1;
				} else {
					leftNext=leftMap[ym1][xm1];
				}
				if(ym2<0 ||ym2>=maxVirtualMapsize || xm2<0 ||xm2>=maxVirtualMapsize) {
					rightNext=1;
				} else {
					rightNext=rightMap[ym2][xm2];
				}

				// currently assuming that its possible that both exit together
				if(leftNext==2 || rightNext==2) {
					if(leftNext==2 && leftExitFound==false) {
						lPlayer_X_Exit=xm1;lPlayer_Y_Exit=ym1;
						System.out.println("\t\t\t\t\t\tleftExitFound "+xm1+","+ym1);
						leftExitFound=true;
					}
					if(rightNext==2 && rightExitFound==false) {
						rPlayer_X_Exit=xm2;rPlayer_Y_Exit=ym2;
						System.out.println("\t\t\t\t\t\trightExitFound "+xm2+","+ym2);
						rightExitFound=true;
					}
					//if(!(leftMap[ym1][xm1]==2 && rightMap[ym2][xm2]==2)) {
					//continue;
					//}
				}

				if(leftMap[y1][x1]==2) {
					xm1=x1;ym1=y1;leftSame++;
				} else {
					if(leftNext==1) {
						xm1=x1;ym1=y1;leftSame++;
					}
				}

				if(rightMap[y2][x2]==2) {
					xm2=x2;ym2=y2;rightSame++;
				} else {
					if(rightNext==1) {
						xm2=x2;ym2=y2;rightSame++;
					}
				}

				//TODO way to measure distance b/w two states 			

				if(leftSame==1 && rightSame==1)
					continue;
				//if (stateDistance[xm1][ym1][xm2][ym2]>distance || state[xm1][ym1][xm2][ym2]==0 ) { 
				if (state[xm1][ym1][xm2][ym2]==0 ) { 
					if((rightNext==2 &&leftNext!=2) || (rightNext!=2 &&leftNext==2)) { 
						if (!exitSeperately)
							continue;
						// if the control crosses this line then it means that bith the exits ae known
						if(rightNext==2) {
							System.out.println("# right at exit "+xm2+","+ym2);
							System.out.println("# left at  "+xm1+","+ym1);
							singleStepsRequired=countStepsToExit("left",xm1,ym1);
						}
						if(leftNext==2) {
							singleStepsRequired=countStepsToExit("right",xm2,ym2);

							System.out.println("# left at exit "+xm1+","+ym1);
							System.out.println("# right at  "+xm2+","+ym2);
						}
						if(singleStepsRequired<minSingleStepsRequired) {
							minSingleStepsRequired=singleStepsRequired;
							minSingleMovesList.clear();
							minSingleMovesList.addAll(singleMovesList);
							System.out.println("singleStepsRequired"+singleStepsRequired+"singleMovesList"+singleMovesList);
							bestDiffExitState=xm1+","+ym1+","+xm2+","+ym2+","+distance;
						}
						System.out.println("currentState="+xm1+","+ym1+","+xm2+","+ym2+","+distance);
						System.out.println("singleStepsRequired="+singleStepsRequired);
						System.out.println("minSingleStepsRequired="+minSingleStepsRequired);
						System.out.println("bestDiffExitState="+bestDiffExitState);


						// currently assuming that there is a solution with 0 delay
					}
					state[xm1][ym1][xm2][ym2]=moves*100+leftSame*10+rightSame;
					int leftUknown=unknownCount(xm1,ym1,"left");
					int rightUknown=unknownCount(xm2,ym2,"right");
					int totalUnknown=leftUknown+rightUknown;


					stateName=xm1+","+ym1+","+xm2+","+ym2+","+distance+","+leftUknown+","+rightUknown+","+totalUnknown;

					if((1.0*leftUknown/distance )>maxLeftScore) {
						maxLeftScore=1.0*leftUknown/distance;
						stateBestLeft=stateName;
					}

					if((1.0*rightUknown/distance )>maxRightscore) { 
						maxRightscore=1.0*rightUknown/distance;
						stateBestRight=stateName;
					}

					if((1.0*totalUnknown/distance )>maxTotalScore) { 
						maxTotalScore=1.0*totalUnknown/distance;
						stateBestBoth=stateName;
					}

					// make a decision on the basis of max  [leftUknown,rightUknown,(leftUknown+rightUknown)]/distance
					// and leftExitFound ,rightExitFound and exitReached()


					/*
					 * these are probably not needed as long as we make the decision here itself
					stateDistance[xm1][ym1][xm2][ym2]=distance;
					stateLeftUnknown[xm1][ym1][xm2][ym2]=  unknownCount(xm1,ym1,"left");
					stateRightUnknown[xm1][ym1][xm2][ym2]=unknownCount(xm2,ym2,"right");
					 */
				}
				else 
					continue;
				System.out.println("state "+xm1+","+ym1+","+xm2+","+ym2+"="+state[xm1][ym1][xm2][ym2]+" name="+stateName);
				if (exitReached())
					return maxTotalScore;
				// check if exit has been reached =>break loop empty ArrayList
				//(better set a global variable to tell that the exit has been reached

				//litr.add(stateName);
				queueElements.add(stateName);
				//enqueue state[xm1][ym1][xm2][ym2]
			}
		}
		return maxTotalScore;
	}

	public int countStepsToExit(String side, int xPos, int yPos) {
		int distance=0;
		// here distance is measured in moves
		int destX,destY,x1,y1,deltaX,deltaY,xm1,ym1,sideSame,sideNext,finalDistance=Integer.MAX_VALUE;
		boolean destinationReached=false;
		if (side=="left") { 
			destX=lPlayer_X_Exit;
			destY=lPlayer_Y_Exit;
		} else {
			destX=rPlayer_X_Exit;
			destY=rPlayer_Y_Exit;
		}

		String stateName=xPos+","+yPos+","+distance;
		ArrayList<String> miniQueueElements=new ArrayList<String>();
		miniQueueElements.add(stateName);
		miniState = new int[maxVirtualMapsize][maxVirtualMapsize];
		miniState[xPos][yPos]=0;
		// the above is sort of implicit
		while(!miniQueueElements.isEmpty()){
			if (destinationReached)
				break;
			stateName=miniQueueElements.remove(0);
			String[] arrayA=stateName.split(",");
			x1=Integer.parseInt(arrayA[0]); y1=Integer.parseInt(arrayA[1]); 
			distance=Integer.parseInt(arrayA[2]);
			distance++;

			for(int moves=1;moves<=8;moves++) {
				deltaX=movesArray[moves][0];
				deltaY=movesArray[moves][1];
				xm1=x1+deltaX;ym1=y1+deltaY;
				sideSame=0;
				if(ym1<0 ||ym1>=maxVirtualMapsize || xm1<0 ||xm1>=maxVirtualMapsize) {
					sideNext=1;
				} else {
					sideNext=side.equals("left")?leftMap[ym1][xm1]:rightMap[ym1][xm1];
				}
                if (sideNext==1 )
                	continue;
				if(sideNext==2) {
					miniState[xm1][ym1]=moves;
					System.out.println("sideNext  "+sideNext);
					System.out.println("miniState value "+miniState[xm1][ym1]);
					System.out.println("xm1 "+xm1+"   ym1 "+ym1);
					System.out.println("destX "+destX+"   destY "+destY);
					destinationReached=true;
					finalDistance=distance;
					break;
				}

				
				//if (stateDistance[xm1][ym1][xm2][ym2]>distance || state[xm1][ym1][xm2][ym2]==0 ) { 
				if (miniState[xm1][ym1]==0 ) { 
					miniState[xm1][ym1]=moves;
					System.out.println(" 1 miniState value "+miniState[xm1][ym1]);
					System.out.println(" 1 xm1 "+xm1+"   ym1 "+ym1);
					//just putting non zero value
					stateName=xm1+","+ym1+","+distance;
					miniQueueElements.add(stateName);
				}
				else 
					continue;
			}
		}
		singleMovesList=new ArrayList<Integer>();
		updateSingleMovesList(destX,destY);
		return finalDistance;

	}
	public void updateSingleMovesList(int x1,int y1) {
		int moveState = miniState[x1][y1];
		int moves;
		int xm1=x1,ym1=y1,deltaX,deltaY;
		if (moveState!=0) {
			moves=moveState;
			singleMovesList.add(0,moves );
			deltaX=movesArray[moves][0];
			deltaY=movesArray[moves][1];
			xm1=x1-deltaX;
			ym1=y1-deltaY;
			updateSingleMovesList(xm1,ym1);
		} 
	}

	public int unknownCount(int xu, int yu, String side) {
		int unknownGrids=0;
		int sightRadius = side.equals("right")?rightSightRadius:leftSightRadius;
		for(int y=yu-sightRadius;y<=yu+sightRadius;y++) {
			for(int x=xu-sightRadius;x<=xu+sightRadius;x++)  {
				if(y<0 ||y>=maxVirtualMapsize || x<0 ||x>=maxVirtualMapsize)
					continue;
				if (side.equals("right")) {
					if (rightMap[y][x]==4) 
						unknownGrids++;
				} else {
					if (leftMap[y][x]==4) 
						unknownGrids++;
				}
			}
		}
		System.out.println("unknown counts "+xu+","+yu+"-"+side+"="+unknownGrids);
		return unknownGrids;
	}
	public void updateMovesList(int x1,int y1,int x2,int y2 ) {
		int moveState = state[x1][y1][x2][y2];
		int moves;
		int xm1=x1,ym1=y1,xm2=x2,ym2=y2,deltaX,deltaY;
		if (moveState!=900) {
			moves=moveState/100;
			movesList.add(0,moves );
			deltaX=movesArray[moves][0];
			deltaY=movesArray[moves][1];
			int leftBit=(moveState%100)/10;
			int rightBit=moveState%10;
			if (leftBit==0) {
				xm1=x1-deltaX;
				ym1=y1-deltaY;
			}
			if (rightBit==0) {
				xm2=x2-deltaX;
				ym2=y2-deltaY;
			}

			updateMovesList(xm1,ym1,xm2,ym2);
		} 
	}

	public boolean exitReached() {
		if(leftExitFound==true && rightExitFound==true)
			if (state[lPlayer_X_Exit][lPlayer_Y_Exit][rPlayer_X_Exit][rPlayer_Y_Exit]!=0)
				return true;
		return false;
	}


}


