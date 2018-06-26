package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.MathUtils;

public class InvisThief extends Thief {
  public static Animation animations = Animation.make("actor-thief", "-black");

  {
    hpMax = 4;
  }

  public Animation getAnimation() {
    return animations;
  }

  @Override
  public void render() {
    if (this.target != null) {
      float d = this.getDistanceTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);
      float tar = (128f - d) / 128;
      this.a = MathUtils.clamp(0, 1, tar);
    }

    super.render();
  }
}