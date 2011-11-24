package mirroruniverse.g1_new;

import java.util.Random;

import mirroruniverse.sim.MUMap;

public class Exploration {

	/*
	 * Implements a random Move.Making sure 
	 * that a 0 move is not returned. 
	 */
	public int randomMove (){
		
		Random rdmTemp = new Random();
		int d=0;
		int nextX =0 ,nextY = 0;
				
		do{
			nextX = rdmTemp.nextInt(3);
			nextY = rdmTemp.nextInt(3);
	
			d = MUMap.aintMToD[nextX][nextY];
		} while (d==0);
		
			System.out.println("Next move is :" + MUMap.aintDToM[d][0] + " "
					+ MUMap.aintDToM[d][1]);
		return d;

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
