package mirroruniverse.g2.astar;

/*    
 * A* algorithm implementation.
 * Copyright (C) 2007, 2009 Giuseppe Scrivano <gscrivano@gnu.org>

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/>.
 */

import java.util.*;

/**
 * A* algorithm implementation using the method design pattern.
 * 
 * @author Giuseppe Scrivano
 */
public abstract class AStar<T> {
	protected class Node implements Comparable {
		public T state;
		public Double f;
		public Double g;
		public Node parent;

		/**
		 * Default c'tor.
		 */
		public Node() {
			parent = null;
			state = null;
			g = f = 0.0;
		}

		/**
		 * C'tor by copy another object.
		 * 
		 * @param p
		 *            The path object to clone.
		 */
		public Node(Node p) {
			this();
			parent = p;
			g = p.g;
			f = p.f;
		}

		/**
		 * Compare to another object using the total cost f.
		 * 
		 * @param o
		 *            The object to compare to.
		 * @see Comparable#compareTo()
		 * @return <code>less than 0</code> This object is smaller than
		 *         <code>0</code>; <code>0</code> Object are the same.
		 *         <code>bigger than 0</code> This object is bigger than o.
		 */
		@Override
		public int compareTo(Object o) {
			Node p = (Node) o;
			return (int) (f - p.f);
		}

		/**
		 * Get the last point on the path.
		 * 
		 * @return The last point visited by the path.
		 */
		public T getState() {
			return state;
		}

		/**
		 * Set the
		 */
		public void setState(T p) {
			state = p;
		}
	}

	/**
	 * Check if the current node is a goal for the problem.
	 * 
	 * @param node
	 *            The node to check.
	 * @return <code>true</code> if it is a goal, <code>false</else> otherwise.
	 */
	protected abstract boolean isGoal(T node);

	/**
	 * Cost for the operation to go to <code>to</code> from <code>from</from>.
	 * 
	 * @param from
	 *            The node we are leaving.
	 * @param to
	 *            The node we are reaching.
	 * @return The cost of the operation.
	 */
	protected abstract Double g(T from, T to);

	/**
	 * Estimated cost to reach a goal node. An admissible heuristic never gives
	 * a cost bigger than the real one. <code>from</from>.
	 * 
	 * @param from
	 *            The node we are leaving.
	 * @param to
	 *            The node we are reaching.
	 * @return The estimated cost to reach an object.
	 */
	protected abstract Double h(T from, T to);

	/**
	 * Generate the successors for a given node.
	 * 
	 * @param node
	 *            The node we want to expand.
	 * @return A list of possible next steps.
	 */
	protected abstract List<T> generateSuccessors(Node node);

	protected PriorityQueue<Node> fringe;
	protected HashSet<T> closedStates;
	protected int expandedCounter;

	/**
	 * Check how many times a node was expanded.
	 * 
	 * @return A counter of how many times a node was expanded.
	 */
	public int getExpandedCounter() {
		return expandedCounter;
	}

	/**
	 * Default c'tor.
	 */
	public AStar() {
		fringe = new PriorityQueue<Node>();
		closedStates = new HashSet<T>();
		expandedCounter = 0;
	}

	/**
	 * Total cost function to reach the node <code>to</code> from
	 * <code>from</code>.
	 * 
	 * The total cost is defined as: f(x) = g(x) + h(x).
	 * 
	 * @param from
	 *            The node we are leaving.
	 * @param to
	 *            The node we are reaching.
	 * @return The total cost.
	 */
	protected Double f(Node p, T from, T to) {
		Double g = g(from, to) + ((p.parent != null) ? p.parent.g : 0.0);
		Double h = h(from, to);

		p.g = g;
		p.f = g + h;

		return p.f;
	}

	/**
	 * Expand a node.
	 * 
	 * @param node
	 *            The node to expand.
	 */
	protected void expand(Node node) {
		List<T> successors = generateSuccessors(node);

		for (T t : successors) {
			Node newNode = new Node(node);
			newNode.setState(t);
			f(newNode, node.getState(), t);
			fringe.offer(newNode);
		}

		expandedCounter++;
	}

	/**
	 * Find the shortest path to a goal starting from <code>start</code>.
	 * 
	 * @param start
	 *            The initial node.
	 * @return A list of nodes from the initial point to a goal,
	 *         <code>null</code> if a path doesn't exist.
	 */
	public List<T> compute(T start) {
		try {
			Node root = new Node();
			root.setState(start);

			/* Needed if the initial point has a cost. */
			f(root, start, start);

			expand(root);

			for (;;) {
				Node p = fringe.poll();

				if (p == null) {
					return null;
				}

				T last = p.getState();

				if (isGoal(last)) {
					return constructSolution(p);
				}
				expand(p);
				closedStates.add(p.state);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	protected List<T> constructSolution (Node last) {
		LinkedList<T> retPath = new LinkedList<T>();

		for (Node i = last; i != null; i = i.parent) {
			retPath.addFirst(i.getState());
		}

		return retPath;
	}
}
