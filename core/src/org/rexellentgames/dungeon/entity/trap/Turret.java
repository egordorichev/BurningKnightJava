package org.rexellentgames.dungeon.entity.trap;

import com.badlogic.gdx.math.Rectangle;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.buff.FreezeBuff;
import org.rexellentgames.dungeon.entity.creature.buff.PoisonBuff;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.entity.level.entities.SolidProp;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class Turret extends SolidProp {
	{
		sprite = "item (missing)";
		alwaysActive = true;
		collider = new Rectangle(1, 10, 14, 4);
	}

	public float a;
	public float last;
	protected float sp = 1.5f;
	private boolean s;
	private float sx = 1;
	private float sy = 1;

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
		Graphics.render(region, this.x + region.getRegionWidth() / 2, this.y, 0, region.getRegionWidth() / 2, 0, false, false, sx, sy);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;

			for (int x = 0; x < this.w / 16; x++) {
				for (int y = 0; y < this.h / 16; y++) {
					Dungeon.level.setPassable((int) (x + this.x / 16), (int) (y + (this.y + 8) / 16), false);
				}
			}
		}

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
		BulletEntity bullet = new BulletEntity();
		bullet.sprite = Graphics.getTexture("bullet (bullet bad)");

		float x = this.x + region.getRegionWidth() / 2;
		float y = this.y + region.getRegionHeight() / 2;

		bullet.x = x;
		bullet.y = y;
		bullet.damage = 2;
		bullet.letter = "bullet bad";

		this.modify(bullet);

		float s = 1.5f;

		bullet.vel = new Point(
			(float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s
		);

		bullet.a = a;

		Dungeon.area.add(bullet);
	}

	protected void modify(BulletEntity entity) {
		switch (this.type) {
			case 1: entity.toApply = PoisonBuff.class; break;
			case 2: entity.toApply = FreezeBuff.class; break;
		}
	}
}