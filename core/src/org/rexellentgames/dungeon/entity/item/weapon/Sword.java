package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Tween;

public class Sword extends Weapon {
	private boolean blocking;
	private float oy;
	private float ox;
	private float blockT;

	@Override
	public boolean isBlocking() {
		return this.blocking;
	}

	{
		name = "Sword";
		sprite = "item (iron sword)";
		damage = 3;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.blockT = Math.max(0, this.blockT - dt);

		if (this.blocking) {
			this.owner.vel.mul(0.1f);
		}

		if (this.blocking && (Input.instance.wasReleased("mouse1") || Input.instance.wasReleased("scroll") || this.blockT == 0)) {
			this.blocking = false;
			this.body.getWorld().destroyBody(this.body);
			this.body = null;
			this.blockT = 3f;

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
		float angle = this.added;

		if (this.owner != null) {
			if (this.owner instanceof Player) {
				float dx = this.owner.x + this.owner.w / 2 - Input.instance.worldMouse.x - 8;
				float dy = this.owner.y + this.owner.h / 2 - Input.instance.worldMouse.y - 8;
				float a = (float) Math.toDegrees(Math.atan2(dy, dx));

				angle += (flipped ? a : -a);
			} else {
				angle += (flipped ? 0 : 180);
			}
		}

		angle = flipped ? angle : 180 - angle;

		TextureRegion sprite = this.getSprite();

		float xx = x + w / 2 + (flipped ? -w / 4 : w / 4);
		float yy = y + (this.ox == 0 ? h / 4 : h / 2);

		Graphics.render(sprite, xx, yy,
			angle, sprite.getRegionWidth() / 2 + (flipped ? this.ox : -this.ox), this.oy, false, false);

		if (this.body != null) {
			float a = (float) Math.toRadians(angle);

			if (this.blocking) {
				this.body.setTransform(xx + (float) Math.cos(a) * (flipped ? 0 : ox * 2), yy + (float) Math.sin(a) * (flipped ? 0 : ox * 2), a);
			} else {
				this.body.setTransform(xx, yy, a);
			}
		}
	}

	protected void createBlockbox() {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;

		body = Dungeon.world.createBody(def);
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

		body.createFixture(fixture);
		body.setUserData(this);
		poly.dispose();
	}


	@Override
	public void use() {
		if (this.blocking) {
			return;
		}

		super.use();

		Tween.to(new Tween.Task(180, this.useTime / 3) {
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
				Tween.to(new Tween.Task(0, useTime / 2) {
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
		if (this.body != null || this.blockT > 0) {
			return;
		}

		this.blockT = 1f;

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