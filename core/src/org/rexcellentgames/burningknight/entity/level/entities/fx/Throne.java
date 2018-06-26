package org.rexcellentgames.burningknight.entity.level.entities.fx;

import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class Throne extends SaveableEntity {
  private static Animation animations = Animation.make("prop-throne", "-throne");
  private AnimationData animation;

  {
    w = 20;
    h = 33;
  }

  @Override
  public void init() {
    super.init();
    this.animation = animations.get("idle");
  }

  @Override
  public void render() {
    super.render();
    this.animation.render(this.x, this.y, false);
  }
}