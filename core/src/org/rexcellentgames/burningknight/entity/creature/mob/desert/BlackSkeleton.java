package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BlackSkeleton extends Skeleton {
  public static Animation animations = Animation.make("actor-skeleton", "-black");

  {
    distance = 72;
    boneSpeed = 300;
    hpMax = 8;
  }

  public Animation getAnimation() {
    return animations;
  }

  @Override
  public void mod(Point vel, Point ivel, float a, float d, float time) {
    a += time / 4f;
    float v = (float) Math.cos(time * 2f);

    vel.x = (float) Math.cos(a) * d * v;
    vel.y = (float) Math.sin(a) * d * v;
  }
}