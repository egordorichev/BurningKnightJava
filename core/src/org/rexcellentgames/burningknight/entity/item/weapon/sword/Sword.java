package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Sword extends Weapon {
  protected static Animation animations = Animation.make("sword-fx");
  protected boolean blocking;
  protected float oy;
  protected float ox;
  protected float blockT;
  protected AnimationData animation;

  protected Body blockbox;
  protected int maxAngle = 200;
  protected float tr = 1f;
  protected float tg = 1f;
  protected float tb = 1f;
  protected float lastAngle;
  protected boolean tail;
  private float lastFrame;
  private ArrayList<Frame> frames = new ArrayList<>();
  private float pure;

  {
    name = "Sword";

    damage = 3;
  }

  @Override
  public boolean isBlocking() {
    return this.blocking;
  }

  protected String getSfx() {
    return "sword_1";
  }

  @Override
  public void update(float dt) {
    super.update(dt);

    if (this.animation != null && this.animation.update(dt)) {
      this.animation.setPaused(true);
    }

    this.blockT = Math.max(0, this.blockT - dt);

    if (this.blocking && (Input.instance.wasReleased("mouse1") || Input.instance.wasReleased("scroll") || this.blockT <= 0)) {
      this.blocking = false;
      this.blockbox = World.removeBody(this.blockbox);
      this.blockT = 3f;

      this.blockbox = null;

      Tween.to(new Tween.Task(0, 0.1f) {
        @Override
        public float getValue() {
          return oy;
        }

        @Override
        public void setValue(float value) {
          oy = value;
          ox = value;
        }
      });
    }

    this.lastFrame += dt;

    if (this.lastFrame >= 0.005f) {
      this.lastFrame = 0;

      if (this.added > 0) {
        Frame frame = new Frame();
        frame.added = (float) Math.toRadians(this.added);

        this.frames.add(frame);

        if (this.frames.size() > 10) {
          this.frames.remove(0);
        }
      } else if (this.frames.size() > 0) {
        this.frames.remove(0);

        if (this.frames.size() > 0) {
          this.frames.remove(0);
        }
      }
    }
  }

  public void render(float x, float y, float w, float h, boolean flipped) {
    if (this.animation == null) {
      this.animation = animations.get("idle");
      this.animation.setPaused(true);
    }

    float angle = added;
    this.pure = 0;

    if (this.owner != null) {
      Point aim = this.owner.getAim();

      float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
      an = Gun.angleLerp(this.lastAngle, an, 0.15f);
      this.lastAngle = an;
      float a = (float) Math.toDegrees(this.lastAngle);

      angle += (flipped ? a : -a);
      pure = a - 180;

      angle = flipped ? angle : 180 - angle;
    }

    TextureRegion sprite = this.getSprite();

    float xx = x + w / 2 + (flipped ? 0 : w / 4);
    float yy = y + h / 4;//(this.ox == 0 ? h / 4 : h / 2);

    if (!this.animation.isPaused() && !this.owner.isDead()) {
      this.animation.render(x + w / 2, y - this.owner.hh / 2, false, false, 0, 11, pure, false);
    }

    if (this.tail && this.frames.size() > 0) {
      double radAngle = Math.toRadians(pure);

      float rx = xx - this.ox - sprite.getRegionWidth() / 2;
      float ry = yy - this.oy;
      float d = this.region.getRegionHeight();

      Frame self = new Frame();
      self.added = (float) Math.toRadians(added);

      Graphics.batch.end();

      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      Graphics.shape.setProjectionMatrix(Camera.game.combined);
      Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

      for (int i = 0; i < this.frames.size(); i++) {
        Frame frame = this.frames.get(i);

        double ad = radAngle + frame.added - Math.PI / 2;
        double sad = radAngle + self.added - Math.PI / 2;

        Graphics.shape.setColor(this.tr, this.tg, this.tb, 0.7f / (this.frames.size() - i));

        Graphics.shape.triangle(rx, ry, rx + (float) Math.cos(ad) * d, ry + (float) Math.sin(ad) * d,
          rx + (float) Math.cos(sad) * d, ry + (float) Math.sin(sad) * d);

        self = frame;
      }

      Graphics.shape.end();
      Gdx.gl.glDisable(GL20.GL_BLEND);
      Graphics.batch.begin();
    }

    this.renderAt(xx - (flipped ? sprite.getRegionWidth() / 2 : 0), yy,
      angle, sprite.getRegionWidth() / 2 + this.ox, this.oy, false, false, flipped ? -1 : 1, 1);

    if (this.blockbox != null) {
      float a = (float) Math.toRadians(angle);
      this.blockbox.setTransform(xx + (float) Math.cos(a) * (flipped ? 0 : ox * 2), yy + (float) Math.sin(a) * (flipped ? 0 : ox * 2), a);
    } else if (this.body != null) {
      float a = (float) Math.toRadians(angle);
      this.body.setTransform(xx + (flipped ? -w / 4 : 0), yy, a);
    }
  }

  @Override
  public void onHit(Creature creature) {
    super.onHit(creature);

    // Camera.shake(4);
    BloodFx.add(creature, 10);

    float a = this.owner.getAngleTo(creature.x + creature.w / 2, creature.y + creature.h / 2);
    this.owner.vel.x += -Math.cos(a) * 120f;
    this.owner.vel.y += -Math.sin(a) * 120f;
  }

  protected void createBlockbox() {
    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;

    Log.physics("Creating centred body for " + this.getClass().getSimpleName());

    this.blockbox = World.world.createBody(def);
    PolygonShape poly = new PolygonShape();

    int w = this.region.getRegionWidth();
    int h = this.region.getRegionHeight();

    float o = this.getSprite().getRegionHeight() / 2;

    poly.set(new Vector2[]{
      new Vector2((float) Math.floor((double) -w / 2) - o, -h / 2), new Vector2((float) Math.ceil((double) w / 2) - o, -h / 2),
      new Vector2((float) Math.floor((double) -w / 2) - o, h / 2), new Vector2((float) Math.ceil((double) w / 2) - o, h / 2)
    });

    FixtureDef fixture = new FixtureDef();

    fixture.shape = poly;
    fixture.friction = 0;

    this.blockbox.createFixture(fixture);
    this.blockbox.setUserData(this);
    poly.dispose();
  }

  @Override
  public void destroy() {
    super.destroy();
    this.blockbox = World.removeBody(this.blockbox);
  }

  @Override
  public void use() {
    if (this.blocking || this.delay > 0) {
      return;
    }

    this.owner.playSfx(this.getSfx());

    if (this.animation != null) {
      this.animation.setPaused(false);
    }

    super.use();

    float a = this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);

    this.owner.vel.x += -Math.cos(a) * 30f;
    this.owner.vel.y += -Math.sin(a) * 30f;

    Tween.to(new Tween.Task(this.maxAngle, this.timeA) {
      @Override
      public float getValue() {
        return added;
      }

      @Override
      public void setValue(float value) {
        added = value;
      }

      @Override
      public void onEnd() {
        if (timeB == 0) {
          added = 0;
        } else {
          Tween.to(new Tween.Task(0, timeB) {
            @Override
            public float getValue() {
              return added;
            }

            @Override
            public void setValue(float value) {
              added = value;
            }

            @Override
            public void onEnd() {
              endUse();
            }
          });
        }
      }
    });
  }

  @Override
  public void secondUse() {
		/*
		if (this.body != null || this.blockT > 0 || this.delay > 0) {
			return;
		}

		this.blockT = 2f;

		super.secondUse();

		this.createBlockbox();
		this.blocking = true;

		Tween.to(new Tween.Task(this.getSprite().getRegionHeight() / 2, 0.1f) {
			@Override
			public float getValue() {
				return oy;
			}

			@Override
			public void setValue(float value) {
				oy = value;
				ox = value;
			}
		});*/
  }

  private static class Frame {
    float added;
  }
}