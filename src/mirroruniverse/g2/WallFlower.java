package mirroruniverse.g2;

import java.util.List;
import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;
import mirroruniverse.g2.astar.State;

;

public class WallFlower implements Player {

	private Explorer explorer;
	private RouteFinder routeFinder;
	Map rightMap;
	Map leftMap;

	public WallFlower() {
		rightMap = new Map("Right");
		leftMap = new Map("Left");
		this.explorer = new Explorer(this.leftMap, this.rightMap);
		this.routeFinder = new RouteFinder(this.leftMap, this.rightMap);
	}

	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		leftMap.updateView(aintViewL);
		rightMap.updateView(aintViewR);

		// if we know the exits and no path found before
		if (leftMap.exitPos != null && rightMap.exitPos != null && !routeFinder.pathFound())
			 routeFinder.searchPath();

		int nextMove = -1;
		
		if (routeFinder.pathFound()) {
			if (Config.DEBUG)
				System.out.println("Follow the path");
			nextMove = routeFinder.getMove();
		} else {
			if (Config.DEBUG)
				System.out.println("Explore");
			nextMove = explorer.getMove();
		}
		
		leftMap.updatePlayer(MUMap.aintDToM[nextMove]);
		rightMap.updatePlayer(MUMap.aintDToM[nextMove]);
		return nextMove;
	}
}
