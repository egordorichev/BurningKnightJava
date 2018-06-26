package org.rexcellentgames.burningknight.util.path;

import java.util.Collection;

public interface GraphNode {
  int getPrice();

  void setPrice(int price);

  int getDistance();

  void setDistance(int distance);

  Collection<? extends GraphNode> getEdges();
}