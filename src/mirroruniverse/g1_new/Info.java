package mirroruniverse.g1_new;

import mirroruniverse.g1_new.MapData;
import mirroruniverse.sim.MUMap;

public class Info {

	/*
	 * Local views for both players
	 */
	static int aintLocalViewL [][];
	static int aintLocalViewR [][];
	
	static Astar astar ;
	/*
	 * Global Views for both players
	 */
	static int aintGlobalViewL [][];
	static int aintGlobalViewR [][];
	
	static int currLX, currLY, currRX, currRY;
	
	public static void initInfo ( int visibilityRadiusL, int visibilityRadiusR){
		astar = new Astar();
		aintGlobalViewL = new int [200][200];
		aintGlobalViewR = new int [200][200];
		
		for ( int i=0 ; i<200 ;i++)
			for (int j=0; j<200 ;j++)
				aintGlobalViewL [i][j] = aintGlobalViewR[i][j] = MapData.unknown;
		
	aintLocalViewL = new int [visibilityRadiusL][visibilityRadiusL];
	aintLocalViewR = new int [visibilityRadiusR][visibilityRadiusR];
	}
	
	public static void updateRelativeLocation(char side,int intDirection){
		System.out.println("Currently updating the new Relative Location");
		int[] aintMove = MUMap.aintDToM[ intDirection ];
		/*
		 *  TODO check if X and Y need to be flipped here.
		 *  This thing honestly confuses me.
		 */
		int intDeltaX = aintMove[ 0 ];
		int intDeltaY = aintMove[ 1 ];
		
		System.out.println("Okay, now the Delta Y and X are " + intDeltaY + " " + intDeltaX);
		
		if (side == 'l'){
			System.out.println("Updating Relative Location for Left side");
			currLX += intDeltaX;
			currLY += intDeltaY;
		}
		else if (side == 'r'){
			System.out.println("Updating Relative Location for Right side");
			currRX += intDeltaX;
			currRY += intDeltaY;
		}
		
		System.out.println("Exiting the Relative Location update");
	}
	
	/*
	 * This method update the current Global View of the system.
	 * It takes as parameter the last direction we moved in.
	 * This call is made from G1Player.java 
	 * The direction is taken from the previous round, but the view 
	 * is taken from the current round.
	 */
	public static void updateGlobalLocation(char side, int view[][], int lastDirectionThatPlayerMovedIn){
		updateRelativeLocation(side, lastDirectionThatPlayerMovedIn);
		
		if (side == 'l'){
			System.out.println("Updating Global view for left player");
			for (int i=0; i<view.length; i++)
				for (int j=0; j<view[i].length; j++)
					aintGlobalViewL[i+100+currLY][j+100+currLX] = view [i][j];
		}
		else if (side == 'r'){
			System.out.println("Updating Global view for right player");
			for (int i=0; i<view.length; i++)
				for (int j=0; j<view[i].length; j++)
					aintGlobalViewR[i+100+currRY][j+100+currRX] = view [i][j];
		}
		System.out.println("Global View for side " + side + "has been updated");
	}
	
	/*
	 * This method updates the Local View of each player.
	 * It takes as parameter the side, and the current view 
	 * that needs to be updated.
	 */
	public static  void updateLocalView (char side, int [][] view){
		
		int tempLocalView [][] = new int [view.length][view.length];
		tempLocalView = view;
		
		if (side == 'r'){
			aintLocalViewR = tempLocalView;
		}
		else if (side == 'l'){
			aintLocalViewL = tempLocalView;
		}
	}
}

