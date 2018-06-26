package org.rexcellentgames.burningknight.entity.item.weapon.throwing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class ThrowingDagger extends Weapon {
  float max = 100f;
  private boolean forward;
  private float lastAngle;

  {
    useTime = 1f;
  }

  @Override
  public void use() {
    super.use();

    this.forward = true;
  }

  @Override
  public void onCollision(Entity entity) {
    super.onCollision(entity);

    if (entity == this.owner) {
      return;
    }

    if (this.added > 2f && (entity == null || entity instanceof Creature)) {
      forward = false;
    }
  }

  @Override
  public void render(float x, float y, float w, float h, boolean flipped) {
    float dt = Gdx.graphics.getDeltaTime() * 5;
    float d = this.max - this.added;

    if (this.forward) {
      if (d <= 10f) {
        this.forward = false;
      } else {
        this.added += d * dt;
      }
    } else if (this.added > 0) {
      this.added = Math.max(this.added - d * dt, 0);
    } else {
      endUse();
    }

    Point aim = this.owner.getAim();
    float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI / 2);
    an = Gun.angleLerp(this.lastAngle, an, 0.05f + (this.max - this.added) / 400f);
    this.lastAngle = an;
    float a = (float) Math.toDegrees(an);
    float dst = added;

    TextureRegion sprite = this.getSprite();

    float xx = (float) (x + w / 2 + (flipped ? -w / 4 : w / 4) + Math.cos(an + Math.PI / 2) * dst);
    float yy = (float) (y + h / 4 + Math.sin(an + Math.PI / 2) * dst);

    this.renderAt(xx - (flipped ? sprite.getRegionWidth() : 0), yy,
      a, sprite.getRegionWidth() / 2, 0, flipped, false);

    if (this.body != null) {
      this.body.setTransform(xx, yy, an);
    }
  }
}