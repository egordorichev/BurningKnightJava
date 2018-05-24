package org.rexellentgames.dungeon.entity.item.weapon.sword;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class Sword extends Weapon {
	protected boolean blocking;
	protected float oy;
	protected float ox;
	protected float blockT;
	protected static Animation animations = Animation.make("sword-fx");
	protected AnimationData animation;

	protected Body blockbox;
	protected int maxAngle = 200;
	protected float timeA = 0.1f;
	protected float timeB = 0.1f;

	@Override
	public boolean isBlocking() {
		return this.blocking;
	}

	{
		name = "Sword";
		sprite = "item (iron sword)";
		damage = 3;
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
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.animation == null) {
			this.animation = animations.get("idle");
			this.animation.setPaused(true);
		}

		float angle = this.added;
		float pure = 0;

		if (this.owner != null) {
			if (this.owner instanceof Player) {
				float dx = this.owner.x + this.owner.w / 2 - Input.instance.worldMouse.x - 8;
				float dy = this.owner.y + this.owner.h / 2 - Input.instance.worldMouse.y - 8;
				float a = (float) Math.toDegrees(Math.atan2(dy, dx));

				angle += (flipped ? a : -a);
				pure = a - 180;

				angle = flipped ? angle : 180 - angle;
			} else if (this.owner instanceof Mob && this.added == 0) {
				Mob mob = (Mob) this.owner;

				if (mob.target != null && mob.saw && !mob.isDead()) {
					float dx = this.owner.x + this.owner.w / 2 - mob.target .x - mob.target.w / 2;
					float dy = this.owner.y + this.owner.h / 2 - mob.target .y - mob.target.h / 2;
					float a = (float) Math.toDegrees(Math.atan2(dy, dx));
					pure = a - 180;

					angle += (flipped ? a : -a);
				} else {
					angle += (flipped ? 0 : 180);
				}

				angle = flipped ? angle : 180 - angle;
			} else {
				angle += (flipped ? 0 : 180);
				angle = flipped ? angle : 180 - angle;
			}
		}

		TextureRegion sprite = this.getSprite();

		float xx = x + w / 2 + (flipped ? -w / 4 : w / 4);
		float yy = y + (this.ox == 0 ? h / 4 : h / 2);

		if (!this.animation.isPaused() && !this.owner.isDead()) {
			this.animation.render(x + w / 2, y - this.owner.hh / 2, false, false, 0, 11, pure, false);
		}

		this.applyColor();

		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.render(sprite, xx - (flipped ? sprite.getRegionWidth() : 0), yy,
			angle, sprite.getRegionWidth() / 2 + (flipped ? this.ox : -this.ox), this.oy, flipped, false);

		if (this.blockbox != null) {
			float a = (float) Math.toRadians(angle);
			this.blockbox.setTransform(xx + (float) Math.cos(a) * (flipped ? 0 : ox * 2), yy + (float) Math.sin(a) * (flipped ? 0 : ox * 2), a);
		} else if (this.body != null) {
			float a = (float) Math.toRadians(angle);
			this.body.setTransform(xx, yy, a);
		}
	}

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		// Camera.instance.shake(4);
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

		this.animation.setPaused(false);
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
		});
	}

	@Override
	public void secondUse() {
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
		});
	}
}