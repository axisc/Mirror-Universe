package mirroruniverse.g2;

import mirroruniverse.sim.MUMap;

public class Map {

	public String name;
	public Position playerPos;
	public Position exitPos;
	public int[][] map;

	public enum Tile {
		UNKNOWN(8), BARRIER(1), EMPTY(0), EXIT(2);
		private int value;

		private Tile(int value) {
			this.value = value;
		}
	};

	public boolean isExit(Position pos) {
		if (exitPos != null)
			if (exitPos.x == pos.x && exitPos.y == pos.y)
				return true;
		return false;
	}

	public boolean isUnknown(Position pos) {
		if (pos.x < 0 || pos.x >= Config.MAX_SIZE || pos.y < 0
				|| pos.y >= Config.MAX_SIZE)
			return false;
		if (map[pos.y][pos.x] == Tile.UNKNOWN.value)
			return true;
		return false;
	}

	public boolean isValid(Position pos) {
		if (pos.x < 0 || pos.x >= Config.MAX_SIZE || pos.y < 0
				|| pos.y >= Config.MAX_SIZE)
			return false;
		if (map[pos.y][pos.x] == Tile.EMPTY.value
				|| map[pos.y][pos.x] == Tile.EXIT.value)
			return true;
		return false;
	}

	public Map(String name) {
		this.name = name;
		playerPos = new Position(Config.MAX_SIZE / 2, Config.MAX_SIZE / 2);
		exitPos = null;

		map = new int[Config.MAX_SIZE][Config.MAX_SIZE];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				map[i][j] = Tile.UNKNOWN.value;
			}
		}
	}

	public void updatePlayer(int[] newPos) {

		if (map[(playerPos.y + newPos[1])][(playerPos.x + newPos[0])] == Tile.EMPTY.value
				|| map[(playerPos.y + newPos[1])][(playerPos.x + newPos[0])] == Tile.EXIT.value) {

			if (exitPos != null) {
				if (playerPos.x == exitPos.x && playerPos.y == exitPos.y) {
					return;
				}
			}

			playerPos.x += newPos[0];
			playerPos.y += newPos[1];
		}
	}

	public void updateView(int[][] view) {
		int center = view.length / 2;

		for (int i = -view.length / 2; i <= view.length / 2; i++) {
			for (int j = -view.length / 2; j <= view.length / 2; j++) {
				if (!isLegalPosition(playerPos.y, i)
						|| !isLegalPosition(playerPos.x, j)) {
					continue;
				}
				if (map[playerPos.y + i][playerPos.x + j] == Tile.UNKNOWN.value) {
					map[playerPos.y + i][playerPos.x + j] = view[center + i][center
							+ j];
				}
				if (view[center + i][center + j] == 2) {
					if (exitPos == null) {
						exitPos = new Position(playerPos.y + i, playerPos.x + j);
					}
				}
			}
		}
		if (Config.DEBUG) {
			//System.out.println(name + " has view\n" + whatIsee(view));
			//System.out.println(name + " has map\n" + printMap());
		}

	}

	// the following two function is extremely inefficient in large map
	private String whatIsee(int[][] view) {
		String ret = "";
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view.length; j++) {
				ret += view[i][j] + " ";
			}
			ret += "\n";
		}
		return ret;
	}

	private String printMap() {
		String ret = "   ";
		for (int i = 0; i < map.length; i++) {
			if (i < 10)
				ret += i + "  ";
			else
				ret += i + " ";
		}
		ret += "\n";
		for (int i = 0; i < map.length; i++) {
			if (i < 10)
				ret += i + "  ";
			else
				ret += i + " ";
			for (int j = 0; j < map.length; j++) {

				if (playerPos.x == j && playerPos.y == i) {
					ret += "X" + "  ";
				} else {
					ret += map[i][j] + "  ";
				}
			}
			ret += "\n";
		}
		return ret;
	}

	public boolean isLegalPosition(int v, int add) {
		if (v + add < 0 || v + add >= Config.MAX_SIZE) {
			return false;
		}
		return true;
	}

}
