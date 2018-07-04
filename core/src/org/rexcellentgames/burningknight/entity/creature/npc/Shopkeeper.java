package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.BronzeShotgun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.Shotgun;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Shopkeeper extends Npc {
	private static Animation animations = Animation.make("actor-trader", "-green");
	private AnimationData idle = animations.get("idle");
	private AnimationData run = animations.get("run");
	private AnimationData hurt = animations.get("hurt");
	private AnimationData death = animations.get("death");
	private AnimationData animation = idle;

	{
		w = 15;
		h = 15;
	}

	public boolean enranged;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		enranged = reader.readBoolean();

		if (enranged) {
			this.become("alerted");
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(enranged);
	}

	@Override
	public void init() {
		super.init();
		this.body = World.createSimpleBody(this, 4, 0, 8, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.vel.len2() >= 20f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		animation.update(dt);

		if (this.shotgun != null) {
			this.shotgun.update(dt);
		}
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		this.playSfx("death_towelknight");
		this.done = true;

		deathEffect(death);
	}

	@Override
	public void render() {
		animation.render(this.x, this.y, this.flipped);

		if (this.shotgun != null) {
			this.shotgun.render(this.x, this.y, this.w, this.h, this.flipped);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + 2, this.y, this.w - 4, this.h);
	}

	public class SKState extends Mob.State<Shopkeeper> {

	}

	public class IdleState extends SKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (Player.instance.room == self.room) {
				self.become("help");
			}
		}
	}

	public class HelpState extends SKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.room != Player.instance.room) {
				self.become("idle");
			} else if (this.moveTo(new Point(Player.instance.x + Player.instance.w / 2, Player.instance.y + Player.instance.h / 2), 5f, 32f)) {

			}
		}
	}

	public class HanaState extends SKState {
		@Override
		public void onEnter() {
			super.onEnter();

			enranged = true;

			self.shotgun = new BronzeShotgun();
			self.shotgun.modifyUseTime(2f);
			self.shotgun.setOwner(self);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.shotgun.getDelay() == 0) {
				self.shotgun.use();
			}
		}
	}

	public Shotgun shotgun;

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": return new IdleState();
			case "help": return new HelpState();
			// tmp
			case "alerted": case "chase": return new IdleState(); // return new HanaState();
		}

		return super.getAi(state);
	}
}