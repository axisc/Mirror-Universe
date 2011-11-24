package mirroruniverse.g1_new;

import java.util.Random;

import mirroruniverse.g2.Config;
import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class G1Player implements Player {
	Random rdmTemp = new Random();
	static boolean initialized = false;
	static Exploration explore = new Exploration();
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		// TODO Auto-generated method stub
		if (!initialized){
			Info.initInfo(aintViewL.length, aintViewR.length);
			initialized = true;
		}
		
		return explore.randomMove();
	}

}
