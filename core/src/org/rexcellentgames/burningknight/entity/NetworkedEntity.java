package org.rexcellentgames.burningknight.entity;

import org.rexcellentgames.burningknight.util.geometry.Point;

public class NetworkedEntity extends Entity {
  public Point vel = new Point();
  public float t;
  protected String state = "idle";

  public String getState() {
    return state;
  }

  @Override
  public void update(float dt) {
    super.update(dt);
    this.t += dt;
  }

  public void become(String state) {
    if (!this.state.equals(state)) {
      this.state = state;
      this.t = 0;
    }
  }
}