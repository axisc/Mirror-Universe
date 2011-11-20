package mirroruniverse.g1;

import mirroruniverse.g1.Direction;

public class Info {

	//Local Views for both the boards
	static int aintLocalViewL [][] ;
	static int aintLocalViewR [][] ;

	
	//Global Views for both the boards
		static int aintGlobalViewL [][];
		static int aintGlobalViewR [][];
	
	/*
	 * Default Constructor
	 */
		public Info (){
			aintGlobalViewL = new int [200][200];
			aintGlobalViewR = new int [200][200];
			
			aintLocalViewL = new int [3][3];
			aintLocalViewR = new int [3][3];
		}
		
	/*
	 * This method checks if the board has been explored
	 * completely. 
	 * This can be checked by hunting for an array of size 100x100
	 * that doesn't contain a value '4' for any of the squares.
	 */
	public boolean isExplorationComplete(char side){
		
		return false;
	}
	
	
	/*
	 * This method checks if the exit has been spotted
	 * in the local view of the player specified.
	 * It takes a character 'side' as the parameter.
	 */
	public boolean isExitSpotted (char side){
		int tempLocalView[][] = new int [3][3];
		if (side =='l'){
			tempLocalView = aintLocalViewL;
		}else
			if (side == 'r'){
				tempLocalView = aintLocalViewR;
			}
		for (int i =0; i<tempLocalView.length ;i++)
			for(int j=0;j<tempLocalView[i].length ;j++)
				if (tempLocalView[i][j] == 2)
					return true;
		return false;
	}
	
	/*
	 * This method updates the Global View of each player.
	 * It takes as parameter the side, and the current view
	 * that needs to be added to the Global View
	 */
	public void updateGlobalView (char side, int [][]view){
		int tempGlobalView [][]= new int [3][3];
		
		if (side == 'r'){
			this.aintGlobalViewR= tempGlobalView;
		}
		else if (side == 'l'){
			this.aintGlobalViewL = tempGlobalView;
		}
		
	}
	
	/*
	 * This method updates the Local View of each player.
	 * It takes as parameter the side, and the current view 
	 * that needs to be updated.
	 */
	public void updateLocalView (char side, int [][] view){
		
		int tempLocalView [][] = new int [3][3];
		
		if (side == 'r'){
			this.aintLocalViewR = tempLocalView;
		}
		else if (side == 'l'){
			this.aintLocalViewL = tempLocalView;
		}
	}
	
	/*
	 * We call this method once the exits on both the maps 
	 * have been spotted.
	 */
	public void aStar(int [][] localView){
		
	}
}
