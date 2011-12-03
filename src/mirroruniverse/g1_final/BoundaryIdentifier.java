package mirroruniverse.g1_final;

public class BoundaryIdentifier {

	static int direction;
	static boolean upperBoundary, lowerBoundary, leftBoundary, rightBoundary;
	
	
	/*
	 * This is used to identify the boundaries
	 */
	public static int identifyBoundaries (){
		
		// If the Left boundary isn't known, look for the left boundary
		if (!leftBoundary)
			direction = locateBoundary("left");
		// If the Right boundary isn't known, look for the right boundary
		if (!rightBoundary)
			direction = locateBoundary("right");
		// If the Upper boundary isn't known, look for the upper boundary
		if (!upperBoundary)
			direction = locateBoundary("upper");
		// If the Lower boundary isn't known, look for the lower boundary
		if (!lowerBoundary)
			direction = locateBoundary("lower");
		return direction;
	}
	
	public static int locateBoundary(String s){
		
		/*
		 *  TODO follow the direction s, till all the squares in that grid
		 *  are 1.
		 */
		
		int returnValue = 0;
		
		if (s.equals("left")){
			if (!checkForBoundary(0, 1, 0, Info.LocalViewL.length, 'l') ||
					!checkForBoundary(0, 1, 0, Info.LocalViewR.length, 'r'))
				
				returnValue = Direction.WEST;
		}
		else if (s.equals("right")){
			if (!checkForBoundary(Info.LocalViewL.length, 1, 0, Info.LocalViewL.length, 'l') ||
					!checkForBoundary(Info.LocalViewR.length, 1, 0, Info.LocalViewR.length, 'r'))
				
				returnValue = Direction.EAST;
			
		}
		else if (s.equals("upper")){
			if (!checkForBoundary(0, Info.LocalViewL.length, 0, 1, 'l') ||
					!checkForBoundary(0, Info.LocalViewR.length, 0, 1, 'r'))
				
				returnValue = Direction.NORTH;
		}
		else if (s.equals("lower")){
			if (!checkForBoundary(0, Info.LocalViewL.length, Info.LocalViewL.length, 1, 'l') ||
					!checkForBoundary(0, Info.LocalViewR.length, Info.LocalViewR.length, 1, 'r'))
				
				returnValue = Direction.SOUTH;
		}
		
		return returnValue;
		
	}
	
	
	public static boolean checkIfAllBoundarySet(){
		
		return 	(
				checkForBoundary(0, 1, 0, Info.LocalViewL.length, 'l') &&
				checkForBoundary(0, 1, 0, Info.LocalViewR.length, 'r') &&
				checkForBoundary(Info.LocalViewL.length, 1, 0, Info.LocalViewL.length, 'l') &&
				checkForBoundary(Info.LocalViewR.length, 1, 0, Info.LocalViewR.length, 'r') &&
				checkForBoundary(0, Info.LocalViewL.length, 0, 1, 'l') &&
				checkForBoundary(0, Info.LocalViewR.length, 0, 1, 'r') &&
				checkForBoundary(0, Info.LocalViewL.length, Info.LocalViewL.length, 1, 'l') &&
				checkForBoundary(0, Info.LocalViewR.length, Info.LocalViewR.length, 1, 'r')
				);
				
	}
	
	public static boolean checkForBoundary( int startI, int endI , int startJ, int endJ, char side){
		int tempView [][] = null ;
		if (side == 'l') tempView = Info.LocalViewL;
		else if (side == 'r') tempView = Info.LocalViewR;
		
		for (int i = startI ; i < startI + endI ; i++){
			for(int j= startJ ; j< startJ + endJ ; j++){
				if (tempView [i][j] != 1){
					return false;
				}
			}
		}
		return true;
	}
}
