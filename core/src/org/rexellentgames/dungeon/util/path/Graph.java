package org.rexellentgames.dungeon.util.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Graph {
	public static <T extends org.rexellentgames.dungeon.util.path.GraphNode> void buildDistanceMap(Collection<T> nodes, T from) {
		for (T node : nodes) {
			node.setDistance(Integer.MAX_VALUE);
		}

		LinkedList<org.rexellentgames.dungeon.util.path.GraphNode> queue = new LinkedList<>();

		from.setDistance(0);
		queue.add(from);

		while (!queue.isEmpty()) {
			org.rexellentgames.dungeon.util.path.GraphNode node = queue.poll();

			int distance = node.getDistance();
			int price = node.getPrice();

			for (org.rexellentgames.dungeon.util.path.GraphNode edge : node.getEdges()) {
				if (edge.getDistance() > distance + price) {
					queue.add(edge);
					edge.setDistance(distance + price);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends org.rexellentgames.dungeon.util.path.GraphNode> ArrayList<T> buildPath(Collection<T> nodes, T from, T to) {
		ArrayList<T> path = new ArrayList<>();
		T room = from;

		while (room != to) {
			int min = room.getDistance();
			T next = null;

			Collection<? extends org.rexellentgames.dungeon.util.path.GraphNode> edges = room.getEdges();

			for (GraphNode edge : edges) {
				int distance = edge.getDistance();

				if (distance < min) {
					min = distance;
					next = (T) edge;
				}
			}

			if (next == null) {
				return null;
			}

			path.add(next);
			room = next;
		}

		return path;
	}
}