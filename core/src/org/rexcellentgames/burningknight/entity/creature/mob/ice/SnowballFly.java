package org.rexcellentgames.burningknight.entity.creature.mob.ice;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.common.DiagonalFly;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class SnowballFly extends DiagonalFly {
	public static Animation animations = Animation.make("actor-fly", "-snow");
	public Animation getAnimation() {
		return animations;
	}

	@Override
	protected void createBody() {
		w = 23;
		h = 16;

		body = World.createSimpleBody(this, 6, 2, w - 12, h - 8, BodyDef.BodyType.DynamicBody, false);
		body.getFixtureList().get(0).setRestitution(1f);
		body.setTransform(x, y, 0);

		float f = 32;

		this.velocity = new Point(f * (Random.chance(50) ? -1 : 1), f * (Random.chance(50) ? -1 : 1));

		body.setLinearVelocity(this.velocity);
	}

	@Override
	public void deathEffects() {
		super.deathEffects();

		if (touches[Terrain.FLOOR_A] || touches[Terrain.FLOOR_B] || touches[Terrain.FLOOR_C] || touches[Terrain.FLOOR_D]) {
			Snowball snowball = new Snowball();
			snowball.x = this.x;
			snowball.y = this.y;
			Dungeon.area.add(snowball.add());
		}
	}

	@Override
	protected ArrayList<Item> getDrops() {
		return new ArrayList<>();
	}
}