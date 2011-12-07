package mirroruniverse.g4;

import java.util.ArrayList;

import mirroruniverse.sim.Player;

public class G4PlayerAstarTest implements Player{
	
	public int sightRadius1;
	public int sightRadius2;
	public boolean started = false;
	public static final int MAX_SIZE = 100;
	public int[][] kb_p1;
	public int[][] kb_p2;
	private int[] p1Pos;
	private int[] p2Pos;
	
	private int numPath;
	private int initialDir;
	//private int turn;
	private int stepCounter;
	private int currentDir;
	
	private boolean debugging = false;
	
	private ArrayList<Integer> path = new ArrayList<Integer>();

	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		sightRadius1 = (aintViewL.length-1)/2;
		sightRadius2 = (aintViewR.length-1)/2;
		if (!started){
			initialize(aintViewL);
		}
		if(path.isEmpty()){
			int[] exit1 = new int[2];
			int[] exit2 = new int[2];
			for(int y = 0; y < aintViewL.length; ++y){
				for(int x = 0; x < aintViewL[0].length; ++x){
					if(aintViewL[y][x] == 2){
						exit1[0] = x;
						exit1[1] = y;
					}
				}
			}
			for(int y = 0; y < aintViewR.length; ++y){
				for(int x = 0; x < aintViewR[0].length; ++x){
					if(aintViewR[y][x] == 2){
						exit2[0] = x;
						exit2[1] = y;
					}
				}
			}
			AStar_2 a = new AStar_2(sightRadius1,sightRadius1,sightRadius2,sightRadius2,aintViewL,aintViewR);
			a.setExit1(exit1[0], exit1[1]);
			a.setExit2(exit2[0], exit2[1]);
			path = a.findPath();
			
		}
		int goTo = path.remove(0);
		if (debugging) {
			//if(path.isEmpty()){
			for (int i = 0; i < aintViewL.length; ++i) {
				for (int j = 0; j < aintViewL[0].length; ++j) {
					if (i == sightRadius1 && j == sightRadius1) {
						System.out.print(3);
					} else {
						System.out.print(aintViewL[i][j]);
					}
				}
				System.out.println();
			}
			System.out.println("---------------");
			for (int i = 0; i < aintViewR.length; i++) {
				for (int j = 0; j < aintViewR[0].length; ++j) {
					if (i == sightRadius2 && j == sightRadius2) {
						System.out.print(3);
					} else {
						System.out.print(aintViewR[i][j]);
					}
				}
				System.out.println();
			}
			System.out.println("\n");
			//}
			System.out.println(goTo);
		}
		return goTo;
		
		//return 0;
	}
	
	public void initialize(int[][] aintViewL){
		started = true;
		sightRadius1 = (aintViewL[0].length - 1)/2;
		kb_p1 = new int[2*MAX_SIZE-1][2*MAX_SIZE-1];
		kb_p2 = new int[2*MAX_SIZE-1][2*MAX_SIZE-1];
		p1Pos = new int[2];
		p2Pos = new int[2];
		p1Pos[0] = p2Pos[0] = p1Pos[1] = p2Pos[1] = 99;
		
		for(int i = 0; i < kb_p1.length; ++i){
			for(int j = 0; j < kb_p1.length; ++j){
				kb_p1[i][j] = 1;
				kb_p2[i][j] = 1;
			}
		}
		
		
		numPath = 0;
		initialDir = 2;
		currentDir = initialDir;
		stepCounter = 0;
	}
	
	//not currently taking into account obstacles
	public int move(){
		if (stepCounter == calcPathSteps()){
			currentDir -= 2;
			if (currentDir == 0){
				currentDir = 8;
			}
			numPath++;
			stepCounter = 0;
		}
		
		stepCounter++;
		//turn++;
		return currentDir;
	}
	
	private int calcPathSteps(){
		return (numPath/2 + 1)*(2*sightRadius1 - 1);
	}

}
