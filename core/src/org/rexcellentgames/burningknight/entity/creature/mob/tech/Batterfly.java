package org.rexcellentgames.burningknight.entity.creature.mob.tech;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.common.DiagonalFly;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.Lighting;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Batterfly extends DiagonalFly {
	public static Animation animations = Animation.make("actor-fly", "-electro");
	public Animation getAnimation() {
		return animations;
	}

	private HashMap<Mob, Lighting> lighting = new HashMap<>();

	@Override
	protected void createBody() {
		w = 19;
		h = 17;
		body = World.createSimpleBody(this, 5, 2, w - 12, h - 4, BodyDef.BodyType.DynamicBody, false);
		body.getFixtureList().get(0).setRestitution(1f);
		body.setTransform(x, y, 0);

		float f = 32;

		this.velocity = new Point(f * (Random.chance(50) ? -1 : 1), f * (Random.chance(50) ? -1 : 1));

		body.setLinearVelocity(this.velocity);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Player.instance.room == room) {
			for (Mob mob : Mob.all) {
				if (mob != this && mob.room == room && !this.lighting.containsKey(mob)) {
					if (mob instanceof Batterfly && ((Batterfly) mob).lighting.containsKey(this)) {
						continue;
					}

					if (this.getDistanceTo(mob.x + mob.w / 2, mob.y + mob.h / 2) < 96) {
						Lighting lighting = new Lighting();

						lighting.x = this.x + w / 2;
						lighting.y = this.y + h / 2;
						lighting.bad = true;

						Dungeon.area.add(lighting);
						this.lighting.put(mob, lighting);
					}
				}
			}
		}

		Iterator<Map.Entry<Mob, Lighting>> it = lighting.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Mob, Lighting> pair = it.next();
			Lighting lighting = pair.getValue();
			Mob mob = pair.getKey();

			lighting.target.x = mob.x + mob.w / 2;
			lighting.target.y = mob.y + mob.h / 2;
			lighting.w = this.getDistanceTo(mob.x + w / 2, mob.y + h / 2);
			lighting.an = (float) (this.getAngleTo(mob.x + w / 2, mob.y + h / 2) - Math.PI / 2);
			lighting.a = (float) Math.toDegrees(lighting.an);
			lighting.x = this.x + w / 2;
			lighting.y = this.y + h / 2;
			lighting.updatePos();


			if (mob.done || this.getDistanceTo(mob.x + w / 2, mob.y + h / 2) > 96) {
				// fixme: not removing???
				lighting.done = true;
				it.remove();
				this.lighting.remove(mob);
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		for (Lighting lighting : lighting.values()) {
			lighting.done = true;
		}
	}
}