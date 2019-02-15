package org.rexcellentgames.burningknight.entity.creature.mob.ice;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Gift extends Mob {
	public static Animation animations = Animation.make("actor-gift", "-normal");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 8;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		animation = idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
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

		animation.update(dt);
		super.common();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();

		this.playSfx("death_clown");
		deathEffect(killed);

		for (int i = 0; i < Random.newInt(4, 8); i++) {
			Mob mob = Random.chance(25) ? (Random.chance(50) ? new SnowballFly() : new Snowball()) : (Random.chance(60) ? new Snowflake() : new Roller());

			mob.x = this.x + Random.newFloat(16);
			mob.y = this.y + Random.newFloat(16);

			Dungeon.area.add(mob.add());
		}
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	protected Mob.State getAi(String state) {
		return new IdleState();
	}

	public class IdleState extends Mob.State<Gift> {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.dd) {
				return;
			}

			if (Player.instance.room == self.room && self.getDistanceTo(Player.instance.x + 8, Player.instance.y + 8) < 32) {
				self.die();
			}
		}
	}
}