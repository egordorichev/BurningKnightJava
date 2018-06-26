package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.util.Random;

public class SlowBook extends Book {
  public static TextureRegion particle = Graphics.getTexture("particle-giant");

  {    mana = 6;
    damage = 10; // GG WP EZ
  }

  @Override
  public void spawnProjectile(float x, float y, float a) {
    BulletProjectile missile = new BulletProjectile() {
      @Override
      public void render() {
        Graphics.batch.end();
        RectFx.shader.begin();
        RectFx.shader.setUniformf("r", (float) Math.abs(Math.cos(this.t * 1.5f)));
        RectFx.shader.setUniformf("g", (float) Math.abs(Math.cos(this.t * 2f - 0.1f)));
        RectFx.shader.setUniformf("b", (float) Math.abs(Math.sin(this.t * 2.7f)));
        RectFx.shader.setUniformf("a", 0.8f);
        RectFx.shader.end();
        Graphics.batch.setShader(RectFx.shader);
        Graphics.batch.begin();
        Graphics.render(particle, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false);
        Graphics.batch.end();
        Graphics.batch.setShader(null);
        Graphics.batch.begin();
      }

      @Override
      public void logic(float dt) {
        super.logic(dt);

        if (this.last > 0.03f) {
          this.last = 0;
          RectFx fx = new RectFx();

          fx.depth = this.depth;
          fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
          fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
          fx.w = 4;
          fx.h = 4;
          fx.r = (float) Math.abs(Math.cos(this.t * 1.5f));
          fx.g = (float) Math.abs(Math.cos(this.t * 2f - 0.1f));
          fx.b = (float) Math.abs(Math.sin(this.t * 2.7f));

          Dungeon.area.add(fx);
        }

        this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
      }
    };

    missile.depth = 1;
    missile.damage = this.rollDamage();

    missile.crit = this.lastCrit;
    missile.owner = this.owner;
    missile.x = x;
    missile.y = y - 8;
    missile.rectShape = true;
    missile.w = 16;
    missile.h = 16;
    missile.rotates = true;
    missile.rotationSpeed = 0.1f;

    double ra = Math.toRadians(a);
    float s = 10f;

    missile.vel.x = (float) Math.cos(ra) * s;
    missile.vel.y = (float) Math.sin(ra) * s;

    Dungeon.area.add(missile);
  }
}