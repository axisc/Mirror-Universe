package mirroruniverse.g2;

import mirroruniverse.g2.astar.State;

public class Position {
	public int x;
	public int y;

	public Position() {
		this.x = 0;
		this.y = 0;
	}

	public Position(int y, int x) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Position rhs = (Position) obj;
		return x == rhs.x && y == rhs.y;
	}
}