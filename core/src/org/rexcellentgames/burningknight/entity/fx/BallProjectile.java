package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.physics.World;

public class BallProjectile extends Projectile {
  private static TextureRegion sprite = Graphics.getTexture("item-bullet_e");

  {
    w = sprite.getRegionWidth();
    h = sprite.getRegionHeight();
    ignoreVel = true;
    damage = 4;
  }

  @Override
  protected boolean hit(Entity entity) {
    if (entity instanceof Mob) {
      return true;
    }

    return super.hit(entity);
  }

  @Override
  public void init() {
    super.init();
    this.body = World.createCircleBody(this, 0, 0, this.w / 2, BodyDef.BodyType.DynamicBody, false, 0.8f);
    this.body.setTransform(this.x, this.y, 0);

    MassData data = new MassData();
    data.mass = 0.01f;
    this.body.setMassData(data);
  }

  @Override
  public void render() {
    Graphics.render(sprite, this.x, this.y);
  }
}