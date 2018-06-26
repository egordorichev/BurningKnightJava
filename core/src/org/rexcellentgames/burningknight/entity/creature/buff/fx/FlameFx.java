package org.rexcellentgames.burningknight.entity.creature.buff.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

public class FlameFx extends Entity {
  public static Color red = Color.valueOf("#ac3232");
  public static Color orange = Color.valueOf("#df7126");
  private Color color;
  private float t;
  private float tt;
  private float size = 1f;
  private float range = 1;
  private float angle;
  private float s;
  private Entity owner;

  {
    color = Random.newFloat() < 0.7 ? orange : red;
    t = Random.newFloat(1024);
    tt = 0;
    s = Random.newFloat(3, 6);
    depth = 6;
    alwaysActive = true;
    alwaysRender = true;
  }

  public FlameFx(Entity owner) {
    this.owner = owner;
    x = owner.x;
    y = owner.y;

    Tween.to(new Tween.Task(4, 0.05f) {
      @Override
      public float getValue() {
        return size;
      }

      @Override
      public void setValue(float value) {
        size = value;
      }

      @Override
      public void onEnd() {
        Tween.to(new Tween.Task(0, 1f) {
          @Override
          public float getValue() {
            return size;
          }

          @Override
          public void setValue(float value) {
            size = value;
          }
        });
      }
    });
  }

  @Override
  public void update(float dt) {
    this.t += dt;
    this.tt += dt;
    this.y = this.owner.y + this.tt * 15;

    if (this.range < 6) {
      this.range += dt * 15;
    }

    this.x = this.owner.x + (float) (Math.cos(this.t) * this.range);
    this.angle = (float) Math.cos(this.t * this.s) * 20;

    if (this.y - this.owner.y > 16) {
      this.done = true;
    }
  }

  @Override
  public void render() {
    Graphics.batch.end();

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Graphics.shape.setProjectionMatrix(Camera.game.combined);
    Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

    float s = this.size / 2;

    Graphics.shape.setColor(this.color.r, this.color.g, this.color.b, 0.8f);
    Graphics.shape.rect(this.x + this.owner.w / 2, this.y, s, s, this.size,
      this.size, 1, 1, this.angle);

    Graphics.shape.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
    Graphics.batch.begin();
  }
}