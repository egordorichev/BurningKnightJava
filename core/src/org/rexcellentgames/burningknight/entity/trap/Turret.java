package org.rexcellentgames.burningknight.entity.trap;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Turret extends SolidProp {
	public static Animation animations = Animation.make("actor-turret", "-fire");
	private AnimationData single = animations.get("turret_1_directions");

	{
		alwaysActive = true;
		collider = new Rectangle(1, 10, 14, 4);
	}

	public float a;
	public float last;
	protected float sp = 1.5f;
	private boolean s;
	protected float sx = 1f;
	protected float sy = 1f;

	@Override
	public void init() {
		super.init();

		float r = Random.newFloat();

		if (r < 0.1f) {
			this.type = 1;
		} else if (r < 0.2f) {
			this.type = 2;
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.last = reader.readFloat();
		this.a = reader.readFloat();
		this.type = reader.readByte();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeFloat(this.last);
		writer.writeFloat(this.a);
		writer.writeByte(this.type);
	}

	private byte type;

	@Override
	public void render() {
		single.render(this.x, this.y, false, false, 8, 0, 0, sx, sy);
	}

	protected boolean rotates;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;

			for (int x = 0; x < this.w / 16; x++) {
				for (int y = 0; y < this.h / 16 + 1; y++) {
					Dungeon.level.setPassable((int) (x + this.x / 16), (int) (y + (this.y + 8) / 16), false);
				}
			}
		}

		this.single.setFrame(7 - Math.floorMod((int) (Math.floor(this.a / (Math.PI / 4))) - 1, 8));

		this.last += dt;

		if (this.last >= sp) {
			this.last = 0;
			Tween.to(new Tween.Task(0.6f, 0.05f) {
				@Override
				public float getValue() {
					return sy;
				}

				@Override
				public void setValue(float value) {
					sy = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(1f, 0.1f) {
						@Override
						public float getValue() {
							return sy;
						}

						@Override
						public void setValue(float value) {
							sy = value;
						}
					});
				}
			});

			Tween.to(new Tween.Task(1.4f, 0.05f) {
				@Override
				public float getValue() {
					return sx;
				}

				@Override
				public void setValue(float value) {
					sx = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(1f, 0.1f) {
						@Override
						public float getValue() {
							return sx;
						}

						@Override
						public void setValue(float value) {
							sx = value;
						}
					});
				}
			});

			this.send();
		}
	}

	protected void send() {
		BulletProjectile bullet = new BulletProjectile();
		bullet.sprite = Graphics.getTexture("bullet-bad");
		bullet.anim = animations.get("projectile");

		float x = this.x + 8;
		float y = this.y + 8;

		bullet.x = x;
		bullet.y = y;
		bullet.damage = 2;
		bullet.w = 4;
		bullet.h = 4;
		bullet.letter = "bad";
		bullet.bad = true;

		this.modify(bullet);

		float s = 1.5f * 30f;

		bullet.vel = new Point(
			(float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s
		);

		bullet.a = a;

		Dungeon.area.add(bullet);
	}

	protected void modify(BulletProjectile entity) {
		switch (this.type) {
			case 1: entity.toApply = PoisonBuff.class; break;
			case 2: entity.toApply = FreezeBuff.class; break;
		}
	}
}