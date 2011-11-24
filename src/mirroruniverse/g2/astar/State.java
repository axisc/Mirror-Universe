package mirroruniverse.g2.astar;

import mirroruniverse.g2.Config;
import mirroruniverse.g2.Position;

public class State {
	public Position posLeft;
	public Position posRight;

	public State(Position posLeft, Position posRight) {
		this.posLeft = posLeft;
		this.posRight = posRight;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += posLeft.x * Config.MAX_SIZE * Config.MAX_SIZE * Config.MAX_SIZE;
		hash += posLeft.y * Config.MAX_SIZE * Config.MAX_SIZE;
		hash += posRight.x * Config.MAX_SIZE;
		hash += posRight.y;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final State rhs = (State) obj;

		if (this.posLeft.equals(rhs.posLeft)
				&& this.posRight.equals(rhs.posRight))
			return true;
		return false;
	}

	public String toString() {
		return String.format("Left: (%d,%d)\nRight: (%d,%d)\n", posLeft.x,
				posLeft.y, posRight.x, posRight.y);
	}
}
