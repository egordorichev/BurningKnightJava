package org.rexcellentgames.burningknight.entity.creature.player.fx;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class FootFx extends Entity {
  private float t;
  private boolean fade;
  private float a;
  private float angle;

  public FootFx(float x, float y, float angle, float a) {
    this.angle = angle;
    this.a = a;
    this.x = x;
    this.y = y;
    this.t = a * 3;
    this.depth = -1;
  }

  @Override
  public void update(float dt) {
    this.t += dt;

    if (this.t > 3f && !this.fade) {
      this.fade = true;

      Tween.to(new Tween.Task(0, 3f) {
        @Override
        public float getValue() {
          return a;
        }

        @Override
        public void setValue(float value) {
          a = value;
        }

        @Override
        public void onEnd() {
          done = true;
        }
      });
    }
  }

  @Override
  public void render() {
    Graphics.batch.setColor(1, 1, 1, this.a);
    // Graphics.render(Graphics.effects, 8, this.x, this.y, 1, 1, (this.angle * 360 + 90), 3, 12, false, false);
    Graphics.batch.setColor(1, 1, 1, 1);
  }
}