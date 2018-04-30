package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.buff.BrokenArmorBuff;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;

public class IchorFx extends Entity implements WormholeFx.Suckable {
	private static Animation animations = Animation.make("ichor");
	private AnimationData animation = animations.get("idle");
	private float t;
	public Vector2 vel;
	private Body body;
	private boolean bad;

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public void init() {
		super.init();

		WormholeFx.suck.add(this);

		this.y -= 4;

		this.body = World.createSimpleBody(this, 0, 0, 10, 10, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Mob && !this.bad) {
			((Mob) entity).addBuff(new BrokenArmorBuff());
			this.done = true;
		} else if (entity instanceof Player && this.bad) {
			((Player) entity).addBuff(new BrokenArmorBuff());
			this.done = true;
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.t += dt;
		this.animation.update(dt);

		if (this.t >= 5f) {
			this.done = true;
		}

		this.body.setLinearVelocity(this.vel);
	}

	@Override
	public void destroy() {
		super.destroy();
		WormholeFx.suck.remove(this);
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		animation.render(this.x, this.y, false);
	}
}