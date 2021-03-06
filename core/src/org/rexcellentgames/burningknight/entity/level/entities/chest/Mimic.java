package org.rexcellentgames.burningknight.entity.level.entities.chest;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonedBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.*;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.NanoBullet;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pattern.BulletPattern;
import org.rexcellentgames.burningknight.entity.pattern.CircleBulletPattern;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Mimic extends Mob {
	public static float chance = 10;
	public static ArrayList<Mimic> all = new ArrayList<>();
	private AnimationData closed;
	private AnimationData open;
	private AnimationData hurt;
	private AnimationData animation;
	private boolean found;
	public boolean weapon;
	public boolean locked = true;

	{
		hpMax = 30;
		h = 13;
		w = 18;
		knockbackMod = 0;
	}

	private int type = -1;

	@Override
	public void init() {
		super.init();

		all.add(this);

		this.body = World.createSimpleBody(this, 1, 0, 14, 13, BodyDef.BodyType.DynamicBody, true);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_clown");
		this.done = true;

		Dungeon.area.add(new Explosion(this.x + this.w / 2, this.y + this.h / 2));
		Dungeon.area.add(new Smoke(this.x + this.w / 2, this.y + this.h / 2));

		this.playSfx("explosion");

		Chest chest = null;

		if (this.type == 1) {
			chest = new IronChest();
		} else if (type == 2) {
			chest = new GoldenChest();
		} else {
			chest = new WoodenChest();
		}

		chest.x = this.x;
		chest.y = this.y;
		chest.weapon = weapon;
		chest.locked = false;

		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);

		chest.open();

		Achievements.unlock(Achievements.UNLOCK_MIMIC_TOTEM);
		Achievements.unlock(Achievements.UNLOCK_MIMIC_SUMMONER);
	}

	public void toChest() {
		Chest chest;

		if (this.type == 1) {
			chest = new IronChest();
		} else if (type == 2) {
			chest = new GoldenChest();
		} else {
			chest = new WoodenChest();
		}

		chest.x = this.x;
		chest.y = this.y;
		chest.locked = this.locked;
		chest.weapon = this.weapon;

		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);

		this.done = true;
	}

	@Override
	public void destroy() {
		super.destroy();

		all.remove(this);
		this.body = World.removeBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		found = reader.readBoolean();
		this.type = reader.readByte();
		weapon = reader.readBoolean();
		locked = reader.readBoolean();

		if (found) {
			this.become("found");
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(found);
		writer.writeByte((byte) this.type);
		writer.writeBoolean(weapon);
		writer.writeBoolean(locked);
	}

	private void readAnim() {
		if (this.animation == null) {
			Animation animations = WoodenChest.animation;

			if (this.type == -1) {
				Chest chest = Chest.random();

				if (chest instanceof IronChest) {
					this.type = 1;
				} else if (chest instanceof GoldenChest) {
					this.type = 2;
				} else if (chest instanceof WoodenChest) {
					this.type = 0;
				}
			}

			if (this.type == 1) {
				animations = IronChest.animation;
			} else if (type == 2) {
				animations = GoldenChest.animation;
			}

			closed = animations.get("idle");
			open = animations.get("opening_mimic");
			open.setAutoPause(true);

			hurt = animations.get("hurt");

			this.animation = this.closed;
		}
	}

	@Override
	public void render() {
		readAnim();


		Graphics.batch.setColor(1, 1, 1, this.a);
		this.renderWithOutline((this.invt > 0 && this.animation == open) ? hurt : this.animation);

		if (!this.found) {
			float x = this.x + (w - Chest.idleLock.getRegionWidth()) / 2;
			float y = this.y + (h - Chest.idleLock.getRegionHeight()) / 2 +
				(float) Math.sin(this.t) * 1.8f;

			Graphics.render(Chest.idleLock, x, y);
		}

		renderStats();
	}

	{
		ignoreRooms = true;
	}

	@Override
	public void knockBackFrom(Entity from, float force) {

	}

	@Override
	public void update(float dt) {
		this.velocity.x = 0;
		this.velocity.y = 0;

		super.update(dt);

		for (Player player : colliding) {
			player.modifyHp(-1, this, true);
		}

		this.saw = true;

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.animation != null) {
			if (this.animation.update(dt * speedMod)) {
				if (this.animation == open) {
					this.become(this.animation.isGoingBack() ? "wait" : "attack");
				}
			}
		}

		if (this.hurt != null) {
			hurt.update(dt);
		}

		if (this.dead) {
			super.common();
			return;
		}

		super.common();
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player) {
			if (!this.found) {
				Achievements.unlock(Achievements.FIND_MIMIC);
				this.found = true;
				this.become("found");
			}
		}
	}

	@Override
	protected void onHurt(int a, Entity from) {
		super.onHurt(a, from);

		this.playSfx("damage_clown");

		if (!this.found) {
			Achievements.unlock(Achievements.FIND_MIMIC);
			this.found = true;
			this.become("found");
		}
	}

	public class MimicState extends Mob.State<Mimic> {

	}

	@Override
	protected State getAiWithLow(String state) {
		return getAi(state);
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "chase": case "idle": case "roam": return new IdleState();
			case "alerted": case "found": return new FoundState();
			case "attack": return new AttackState();
			case "close": return new CloseState();
			case "wait": return new WaitState();
		}

		return super.getAi(state);
	}

	public class IdleState extends MimicState {

	}

	public class FoundState extends MimicState {
		@Override
		public void onEnter() {
			super.onEnter();
			readAnim();

			animation = open;
			self.playSfx("chest_open");
			animation.setBack(false);
			animation.setPaused(false);
			animation.setFrame(0);
		}
	}

	@Override
	protected boolean canHaveBuff(Buff buff) {
		return !(buff instanceof PoisonedBuff) && super.canHaveBuff(buff);
	}

	public class AttackState extends MimicState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 3f) {
				self.become("close");
			}
		}
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		for (int i = 0; i < Random.newInt(3, 8); i++) {
			ItemHolder item = new ItemHolder(new Gold());
			
			item.getItem().generate();

			Dungeon.area.add(item);
			LevelSave.add(item);
		}

		return drops;
	}

	public class CloseState extends MimicState {
		@Override
		public void onEnter() {
			super.onEnter();

			animation = open;
			open.setBack(true);
			open.setPaused(false);
			open.setFrame(4);
		}
	}

	private int numAttack;

	public class WaitState extends MimicState {
		@Override
		public void onEnter() {
			super.onEnter();
			animation = closed;
		}

		private int num;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= (numAttack % 2 == 1 ? 0.5f : 2f)) {

				if (num == (numAttack % 2 == 1 ? 6 : 3)) {
					numAttack ++;
					self.become("found");
					return;
				}


				Tween.to(new Tween.Task(0.5f, 0.1f) {
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

						if (numAttack % 2 == 1) {
							BulletProjectile bullet = new NanoBullet();

							bullet.bad = true;
							bullet.owner = self;
							bullet.x = self.x + self.w / 2;
							bullet.y = self.y + self.h / 2;

							self.playSfx("gun_machinegun");

							float a = self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2);
							float d = 60f;

							bullet.velocity.x = (float) (Math.cos(a) * d);
							bullet.velocity.y = (float) (Math.sin(a) * d);

							Dungeon.area.add(bullet);

						} else {
							CircleBulletPattern pattern = new CircleBulletPattern();

							self.playSfx("gun_machinegun");
							pattern.radius = 8f;

							for (int i = 0; i < 5; i++) {
								pattern.addBullet(newProjectile());
							}

							BulletPattern.fire(pattern, self.x + 10, self.y + 8, self.getAngleTo(self.target.x + 8, self.target.y + 8), 40f);
						}

						Tween.to(new Tween.Task(1f, 0.1f) {
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

							}
						});
					}
				});

				Tween.to(new Tween.Task(1.2f, 0.2f) {
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
						Tween.to(new Tween.Task(1f, 0.3f) {
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

				t = 0;
				num++;
			}

			if (this.t >= 3f) {
				self.become("found");
			}
		}
	}

	public BulletProjectile newProjectile() {
		BulletProjectile bullet = new NanoBullet();

		bullet.damage = 1;
		bullet.owner = this;
		bullet.bad = true;

		float a = 0; // getAngleTo(target.x + 8, target.y + 8);

		bullet.x = x;
		bullet.y = y;
		bullet.velocity.x = (float) (Math.cos(a));
		bullet.velocity.y = (float) (Math.sin(a));

		return bullet;
	}

	@Override
	public boolean rollBlock() {
		if (!this.found) {
			this.found = true;
			this.become("found");
			this.playSfx("damage_clown");
		}

		return !this.state.equals("attack") || super.rollBlock();
	}
}