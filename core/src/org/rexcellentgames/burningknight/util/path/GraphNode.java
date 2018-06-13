package org.rexcellentgames.burningknight.util.path;

import java.util.Collection;

public interface GraphNode {
	void setPrice(int price);
	int getPrice();
	void setDistance(int distance);
	int getDistance();

	Collection<? extends GraphNode> getEdges();
}