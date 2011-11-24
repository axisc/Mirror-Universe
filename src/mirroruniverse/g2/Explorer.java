package mirroruniverse.g2;

import java.util.Random;

import mirroruniverse.sim.MUMap;

public class Explorer {
	Map leftMap;
	Map rightMap;

	public Explorer(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
	}
	
	// random move
	public int getMove() {
		Random rdmTemp = new Random();
		int nextX = rdmTemp.nextInt(3);
		int nextY = rdmTemp.nextInt(3);

		int d = MUMap.aintMToD[nextX][nextY];
		if (Config.DEBUG) {
			System.out.println("Next move is :" + MUMap.aintDToM[d][0] + " "
					+ MUMap.aintDToM[d][1]);
		}
		
		return d;
	}
}
