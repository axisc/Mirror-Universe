package mirroruniverse.g1;

public class Direction {

	final static int EAST = 1;
	final static int NORTHEAST = 2;
	final static int NORTH = 3;
	final static int NORTHWEST = 4;
	final static int WEST = 5;
	final static int SOUTHWEST = 6;
	final static int SOUTH = 7;
	final static int SOUTHEAST = 8;
	
	public int returnDirection (Node source , Node destination){
		
		int diffX = destination.getX() - source.getX();
		int diffY = destination.getY()- source.getY();
		
		if (diffX >0  && diffY>0 )	return Direction.NORTHEAST;
		else if (diffX>0 && diffY <0 )	return Direction.SOUTHEAST;
		else if (diffX >0 && diffY ==0)	return Direction.EAST;
		else if (diffX<0 && diffY>0)	return Direction.NORTHWEST;
		else if (diffX<0 && diffY<0)	return Direction.SOUTHWEST;
		else if (diffX<0 && diffY ==0)	return Direction.WEST;
		return 0;
	}
	
	public static int returnDirecitonFromDeltas (int diffX, int diffY){
		if (diffX >0  && diffY>0 )	return Direction.NORTHEAST;
		else if (diffX>0 && diffY <0 )	return Direction.SOUTHEAST;
		else if (diffX >0 && diffY ==0)	return Direction.EAST;
		else if (diffX<0 && diffY>0)	return Direction.NORTHWEST;
		else if (diffX<0 && diffY<0)	return Direction.SOUTHWEST;
		else if (diffX<0 && diffY ==0)	return Direction.WEST;
		return 0;
	}
}
