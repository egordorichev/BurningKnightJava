package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.physics.World;

public class FireFxPhysic extends FireFx {
	private Body body;

	@Override
	public void init() {
		super.init();

		body = World.createCircleCentredBody(this, 0, 0, 3, BodyDef.BodyType.DynamicBody, true);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();

		body = World.removeBody(this.body);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Creature) {
			((Creature) entity).addBuff(new BurningBuff());
		} else if (entity instanceof Door) {
			((Door) entity).burning = true;
		}
	}
}