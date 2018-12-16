package org.rexcellentgames.burningknight.entity.trap;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.FireFx;
import org.rexcellentgames.burningknight.entity.fx.FireFxPhysic;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Turret extends SolidProp {
	protected AnimationData single;

	{
		alwaysActive = true;
		collider = new Rectangle(1, 10, 14, 4);
	}

	public float a;
	public float last;
	protected float sp = 4f;
	private boolean s;
	protected float sx = 1f;
	protected float sy = 1f;

	protected Room room;

	@Override
	public void init() {
		super.init();

		float r = Random.newFloat();

		/*if (r < 0.2f) {
			this.type = 1;
		} else if (r < 0.5f) {
			this.type = 2;
		} else if (r < 0.1f) {
			this.type = 3;
			this.sp = 10;
		}*/

		readRoom();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.last = reader.readFloat();
		this.a = reader.readFloat();
		this.type = reader.readByte();
		this.frame = reader.readByte();

		readRoom();
	}

	private void readRoom() {
		this.room = Dungeon.level.findRoomFor(this.x, this.y);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeFloat(this.last);
		writer.writeFloat(this.a);
		writer.writeByte(this.type);
		writer.writeByte((byte) Math.floorMod(this.frame, 8));
	}

	protected byte type;

	protected Animation getAnimation() {
		switch (this.type) {
			case 1: return Animation.make("actor-turret", "-poison");
			case 2: return Animation.make("actor-turret", "-ice");
			case 3: return Animation.make("actor-turret", "-fire");
		}

		return Animation.make("actor-turret", "-normal");
	}

	@Override
	public void render() {
		if (single == null) {
			single = getAnimation().get("turret_1_directions");
		}

		single.render(this.x, this.y, false, false, 8, 0, 0, sx, sy);
	}

	private float lastFlame;
	private boolean was;

	protected void sendFlames() {
		FireFx fx = (Random.chance(50) && this.t >= 1.5f) ? new FireFxPhysic() : new FireFx();

		float d = 5;
		fx.x = x + Random.newFloat(-4, 4) + 8 + (float) (Math.cos(this.a) * d);
		fx.y = y + Random.newFloat(-4, 4) + 8 + (float) (Math.sin(this.a) * d);

		float f = this.t >= 1.5f ? 120f : 40f;

		fx.vel.x = (float) (Math.cos(this.a) * f);
		fx.vel.y = (float) (Math.sin(this.a) * f);

		Dungeon.area.add(fx);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.on) {
			if (this.t >= 0.5f && this.t < 1.5f) {
				was = false;
				return;
			}

			if (!was) {
				tween();
				was = true;
				this.playSfx("fire");
			}

			this.lastFlame += dt;

			if (this.lastFlame >= 0.04f) {
				if (this.room == Player.instance.room && this.room.lastNumEnemies > 0) {
					this.sendFlames();
				}

				if (this.t >= 4.5f) {
					this.on = false;
				}

				this.lastFlame = 0;
			}
		}

		if (this.type == 3) {
			if (this.t >= 9f) {
				if (!this.rotated) {
					if (this.room == Player.instance.room && this.room.lastNumEnemies > 0) {
						this.rotate();
					}

					this.rotated = true;
				}
			} else {
				this.rotated = false;
			}
		} else {
			if (this.t >= this.sp / 2) {
				if (!this.rotated) {
					if (this.room == Player.instance.room && this.room.lastNumEnemies > 0) {
						this.rotate();
					}

					this.rotated = true;
				}
			} else {
				this.rotated = false;
			}
		}

		if (!s) {
			s = true;

			for (int x = 0; x < this.w / 16; x++) {
				for (int y = 0; y < this.h / 16 + 1; y++) {
					Dungeon.level.setPassable((int) (x + this.x / 16), (int) (y + (this.y + 8) / 16), false);
				}
			}
		}

		if (this.single != null) {
			setFrame();
		}

		this.last += dt;

		if (this.last >= sp / 2) {
			this.last = 0;

			if (this.room == Player.instance.room && this.room.lastNumEnemies > 0) {
				this.send();
			}
		}
	}

	protected void setFrame() {
		this.single.setFrame(7 - Math.floorMod((int) (Math.floor(this.a / (Math.PI / 4))) - 1, 8));
	}

	protected int frame;

	protected void tween() {
		Tween.to(new Tween.Task(0.6f, 0.1f) {
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
				Tween.to(new Tween.Task(1f, 0.15f) {
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

		Tween.to(new Tween.Task(1.4f, 0.1f) {
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
				Tween.to(new Tween.Task(1f, 0.15f) {
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
	}

	@Override
	public void renderShadow() {
		Graphics.shadowSized(this.x, this.y + 2, this.w, this.h, 6);
	}

	protected boolean on;

	protected void send() {
		this.tween();
		this.t = 0;
		this.playSfx(this.type == 3 ? "fire" : "gun_machinegun");

		BulletProjectile bullet = new BulletProjectile();
		bullet.sprite = Graphics.getTexture("bullet-nano");
		bullet.letter = "bullet-nano";
		// bullet.anim = getAnimation().get("projectile");

		float x = (float) (this.x + 8 + Math.cos(this.a) * 8);
		float y = (float) (this.y + 8 + Math.sin(this.a) * 8);

		if (this.type == 3) {
			this.on = true;
			this.t = 0;
		} else {
			bullet.x = x;
			bullet.y = y;
			bullet.damage = 2;
			bullet.w = 12;
			bullet.h = 12;
			bullet.bad = true;

			this.modify(bullet);

			float s = 1.5f * 30f;

			bullet.velocity = new Point(
				(float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s
			);

			bullet.a = a;

			Dungeon.area.add(bullet);
		}
	}

	protected void modify(BulletProjectile entity) {
		switch (this.type) {
			case 1: entity.toApply = PoisonBuff.class; break;
			case 2: entity.toApply = FreezeBuff.class; break;
		}
	}

	private boolean rotated;

	protected void rotate() {

	}
}