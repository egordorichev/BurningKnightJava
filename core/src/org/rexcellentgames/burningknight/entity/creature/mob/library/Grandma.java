package org.rexcellentgames.burningknight.entity.creature.mob.library;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.fx.Zzz;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Grandma extends Mob {
	public static Animation animations = Animation.make("actor-grandma", "-normal");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData tss;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 10;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		tss = getAnimation().get("tss");
		animation = idle;
		w = 16;
		ignoreRooms = true;
	}

	private PointLight light;

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 0, 12, 14, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);


		light = World.newLight(32, new Color(1, 1, 1, 1f), 64, x, y);
		light.setIgnoreAttachedBody(true);
	}

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (Random.chance(0.1f)) {
			flipped = !flipped;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("tss")) {
			this.animation = tss;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		light.setActive(true);
		light.attachToBody(body, 8, 8, 0);
		light.setPosition(x + 8, y + 8);
		light.setDistance(64);

		if (!state.equals("tss")) {
			animation.update(dt);
		}

		super.common();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();

		this.playSfx("death_clown");
		deathEffect(killed);
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": case "alerted": return new IdleState();
			case "tss": return new TssState();
		}

		return super.getAi(state);
	}

	public class GrandmaState extends Mob.State<Grandma> {

	}

	@Override
	public void renderShadow() {

	}

	@Override
	public float getOx() {
		return 8;
	}

	public class IdleState extends GrandmaState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= 10f) {
				t = Random.newFloat(0, 5f);
				addZ();
			}

			if (self.target != null && self.target.room == self.room && UiInventory.justUsed > 0) {
				self.become("tss");
			}
		}

		private void addZ() {
			for (int i = 0; i < 3; i++) {
				Zzz z = new Zzz();

				z.x = self.x + 6;
				z.y = self.y + 12;
				z.delay = i * 0.25f;

				Dungeon.area.add(z);
			}
		}
	}

	public class TssState extends GrandmaState {
		@Override
		public void onEnter() {
			super.onEnter();

			if (self.noticeSignT == 0) {
				self.noticeSignT = 1f;
			}

			BulletProjectile bullet = new BulletProjectile();

			bullet.letter = "bullet-book";
			bullet.bad = true;
			bullet.owner = self;
			bullet.x = self.x + self.w / 2;
			bullet.y = self.y + self.h / 2;

			float a = self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2);
			float d = 60f;

			bullet.velocity.x = (float) (Math.cos(a) * d);
			bullet.velocity.y = (float) (Math.sin(a) * d);

			Dungeon.area.add(bullet);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t < 1f) {
				tss.setFrame((int) (t * 5));
			} else {
				self.become("idle");
			}
		}
	}
}