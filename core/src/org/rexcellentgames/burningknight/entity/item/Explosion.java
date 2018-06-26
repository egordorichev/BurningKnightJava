package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Explosion extends Entity {
  public static Animation animations = Animation.make("explosion");
  private AnimationData animation = animations.get("idle");
  private boolean flip = Random.chance(50);

  {
    depth = 30;
  }

  public Explosion(float x, float y) {
    this.x = x;
    this.y = y;
    this.alwaysActive = true;
  }

  @Override
  public void init() {
    super.init();
    Camera.shake(10f);
  }

  @Override
  public void update(float dt) {
    Dungeon.level.addLightInRadius(this.x, this.y, 1f, 0.7f, 0f, 0.8f, 5f, true);

    if (this.animation.update(dt)) {
      this.done = true;
    }
  }

  @Override
  public void render() {
    this.animation.render(this.x - 24, this.y - 24, this.flip);
  }
}