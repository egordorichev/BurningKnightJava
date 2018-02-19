package org.rexellentgames.dungeon.util;

import java.util.Collection;
import java.util.LinkedList;

public class Graph {
	public static <T extends GraphNode> void buildDistanceMap(Collection<T> nodes, T from) {
		for (T node : nodes) {
			node.setDistance(Integer.MAX_VALUE);
		}

		LinkedList<GraphNode> queue = new LinkedList<GraphNode>();

		from.setDistance(0);
		queue.add(from);

		while (!queue.isEmpty()) {
			GraphNode node = queue.poll();

			int distance = node.getDistance();
			int price = node.getPrice();

			for (GraphNode edge : node.getEdges()) {
				if (edge.getDistance() > distance + price) {
					queue.add(edge);
					edge.setDistance(distance + price);
				}
			}
		}
	}
}