package org.rexcellentgames.burningknight.entity.item.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class PickupFx extends Entity {
  public TextureRegion region;
  public Point target;
  private float scale = 1f;
  private float t;

  @Override
  public void init() {
    this.alwaysActive = true;
    this.alwaysRender = true;
    this.depth = 30;

    Tween.to(new Tween.Task(0, 1f, Tween.Type.LINEAR) {
      @Override
      public float getValue() {
        return scale;
      }

      @Override
      public void setValue(float value) {
        scale = value;
      }

      @Override
      public void onEnd() {
        setDone(true);
      }
    });
  }

  @Override
  public void update(float dt) {
    this.t += dt;

    float dx = target.x - this.x;
    float dy = target.y - this.y;
    float d = (float) Math.sqrt(dx * dx + dy * dy);

    if (d > 1) {
      this.x += dx / (d / 2) * dt * 50;
      this.y += dy / (d / 2) * dt * 50;
    }
  }

  @Override
  public void render() {
    Graphics.render(region, this.x + region.getRegionWidth() / 2, this.y + region.getRegionHeight() / 2, this.t * 360f, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, scale, scale);
  }
}