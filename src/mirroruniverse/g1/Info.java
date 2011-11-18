package mirroruniverse.g1;

public class Info {

	//Local Views for both the boards
	int aintLocalViewL [][] = new int [3][3];
	int aintLocalViewR [][] = new int [3][3];
	
	//Global Views for both the boards
		static int aintGlobalViewL [][]= new int [200][200];
		static int aintGlobalViewR [][]= new int [200][200];
	
	
	/*
	 * This method checks if the board has been explored
	 * completely.
	 */
	public boolean isExplorationComplete(char side){
		
		return false;
	}
	
	
	/*
	 * This method checks if the exit has been spotted.
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
				if (aintLocalViewL[i][j] == 2)
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
}
