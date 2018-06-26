package org.rexcellentgames.burningknight.entity.item.weapon.yoyo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Yoyo extends Weapon {
  protected float max = 128f;
  private float t;
  private Point vel = new Point();
  private Bezier line = new Bezier();

  {
    penetrates = true;
    useTime = 10f;
  }

  @Override
  public void use() {
    super.use();
    TextureRegion region = this.getSprite();

    this.x = this.owner.x + this.owner.w / 2 - region.getRegionWidth() / 2;
    this.y = this.owner.y + this.owner.h / 2 - region.getRegionHeight() / 2;
    this.body.setTransform(this.x, this.y, 0);
  }

  @Override
  public void update(float dt) {
    super.update(dt);

    if (this.delay == 0 && this.body != null) {
      this.endUse();
    }

    this.t += dt;

    if (this.body != null) {
      this.body.setLinearVelocity(this.vel);
      this.x = this.body.getPosition().x;
      this.y = this.body.getPosition().y;
    }

    if (this.delay > 1 && !Input.instance.isDown("mouse0") && !Input.instance.isDown("mouse1")) {
      this.delay = 0.8f;
    }
  }

  @Override
  public void onHit(Creature creature) {
    super.onHit(creature);
    TextureRegion region = this.getSprite();

    double a = Math.atan2(creature.y + creature.h / 2 - this.y - region.getRegionHeight() / 2, creature.x + creature.w / 2 - this.x - region.getRegionWidth() / 2);

    float f = 1000f;
    this.vel.x -= (float) Math.cos(a) * f;
    this.vel.y -= (float) Math.sin(a) * f;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void render(float x, float y, float w, float h, boolean flipped) {
    if (this.delay > 0) {
      float dt = Gdx.graphics.getDeltaTime();
      Point aim = this.owner.getAim();
      TextureRegion region = this.getSprite();

      this.vel.mul(0.5f);

      float ox = ((float) region.getRegionWidth()) / 2f;
      float oy = ((float) region.getRegionHeight()) / 2f;

      Vector2 self = new Vector2(this.owner.x + this.owner.w / 2, this.owner.y + this.owner.h / 6);
      Vector2 cur = new Vector2(this.x + ox, this.y + oy);
      Vector2 dst = new Vector2(aim.x - self.x, aim.y - self.y);
      float d = (float) Math.sqrt(dst.x * dst.x + dst.y * dst.y);

      if (this.delay < 0.8f) {
        if (d < 8f) {
          endUse();
          delay = 0;
        } else {
          this.vel.x += (this.owner.x + this.owner.w / 2 - this.x - region.getRegionWidth() / 2) * dt * 120;
          this.vel.y += (this.owner.y + this.owner.h / 2 - this.y - region.getRegionHeight() / 2) * dt * 120;
        }
      } else if (d < this.max) {
        this.vel.x += (aim.x - this.x - region.getRegionWidth() / 2) * 20;
        this.vel.y += (aim.y - this.y - region.getRegionHeight() / 2) * 20;
      } else {
        double a = Math.atan2(dst.y, dst.x);
        this.vel.x += ((float) Math.cos(a) * (this.max) + self.x - this.x - region.getRegionWidth() / 2) * 20;
        this.vel.y += ((float) Math.sin(a) * (this.max) + self.y - this.y - region.getRegionHeight() / 2) * 20;
      }

      line.set(new Vector2[]{
        self,
        new Vector2((cur.x + self.x) / 2, (cur.y + self.y) / 2 - (max - d) / 8),
        cur
      });

      Graphics.batch.end();
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
      Graphics.shape.setColor(1, 1, 1, 0.6f);

      Vector2 tmpV = new Vector2();
      Vector2 last = self;

      float val = 0f;
      while (val <= 1.02f) {
        line.valueAt(tmpV, val);
        Graphics.shape.line(tmpV.x, tmpV.y, last.x, last.y);
        val += 0.05f;

        last = new Vector2(tmpV);
      }

      Graphics.shape.end();
      Gdx.gl.glDisable(GL20.GL_BLEND);
      Graphics.batch.begin();

      Graphics.render(region, this.x + ox, this.y + oy, this.t * 512,
        ox, oy, false, false);
    }
  }

  @Override
  protected void createHitbox() {
    TextureRegion region = this.getSprite();
    this.body = World.createCircleBody(this, 0, 0, Math.max(region.getRegionWidth(), region.getRegionHeight()) / 2, BodyDef.BodyType.DynamicBody, false);
  }
}