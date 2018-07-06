package org.rexcellentgames.burningknight.entity.level.entities.chest;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Mimic extends Mob {
	private static Animation animations = Animation.make("chest", "-wooden");
	private static AnimationData closed = animations.get("idle");
	private static AnimationData open = animations.get("opening_mimic");
	private static AnimationData hurt = animations.get("hurt");
	private AnimationData animation = closed;
	private boolean found;

	{
		hpMax = 30;
		h = 13;

		open.setAutoPause(true);
	}

	@Override
	public void initStats() {
		super.initStats();
		setStat("knockback", 0);
	}

	@Override
	public void init() {
		super.init();
		this.body = World.createSimpleBody(this, 1, 0, 14, 13, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		this.playSfx("death_clown");
		this.done = true;

		Chest chest = new WoodenChest();

		chest.x = this.x;
		chest.y = this.y;

		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);

		chest.open();

		for (int i = 0; i < 10; i++) {
			PoofFx fx = new PoofFx();

			fx.x = this.x + this.w / 2;
			fx.y = this.y + this.h / 2;

			Dungeon.area.add(fx);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		found = reader.readBoolean();

		if (found) {
			this.become("found");
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(found);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.renderWithOutline(this.invt > 0 ? hurt : this.animation);

		Graphics.print(this.state, Graphics.small, this.x, this.y - 16);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		if (this.animation != null) {
			if (this.animation.update(dt * speedMod)) {
				if (this.animation == open) {
					this.become(this.animation.isGoingBack() ? "wait" : "attack");
				}
			}
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
				this.found = true;
				this.become("found");
			}
		}
	}

	@Override
	protected void onHurt(float a, Creature from) {
		super.onHurt(a, from);

		if (!this.found) {
			this.found = true;
			this.become("found");
		}
	}

	public class MimicState extends Mob.State<Mimic> {

	}

	/*
	 * AI plan (todo):
	 * + Stands still till you hit it or touch it, then does following:
	 *  - Opens, then throws a bottle with poison creep, looks at you for a bit
	 *  - Closes, becomes unhittable
	 *
	 * + On death becomes pure chest with open state, generates drop and drops it, as well as some money
	 */

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
			animation = open;
			animation.setBack(false);
			animation.setPaused(false);
			animation.setFrame(0);
		}
	}

	public class AttackState extends MimicState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 5f) {
				// todo: attack
				self.become("close");
			}
		}
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

	public class WaitState extends MimicState {
		@Override
		public void onEnter() {
			super.onEnter();
			animation = closed;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 3f) {
				self.become("found");
			}
		}
	}

	@Override
	public boolean rollBlock() {
		return !this.state.equals("attack") || super.rollBlock();
	}
}