package org.rexcellentgames.burningknight.entity.creature.mob.common;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class Fly extends Mob {
	public static Animation animations = Animation.make("actor-fly", "-normal");

	public Animation getAnimation() {
		return animations;
	}

	private AnimationData idle;
	private AnimationData hurt;
	private AnimationData killed;

	{
		hpMax = 1;
		mul = 0.95f;
		flying = true;
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	public void deathEffects() {
		super.deathEffects();

		deathEffect(killed);
		this.playSfx("death_clown");
		body.setLinearVelocity(new Vector2());
		poof();
	}

	@Override
	public void init() {
		super.init();

		w = 16;
		h = 12;
		createBody();

		idle = getAnimation().get("idle");
		idle.randomize();
		animation = idle;

		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
	}

	protected void createBody() {
		body = World.createSimpleBody(this, 3, 3, w - 6, h - 3, BodyDef.BodyType.DynamicBody, false);
		body.setTransform(x, y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!freezed) {
			animation.update(dt * Mob.speedMod);
		}

		common();
	}

	@Override
	public void render() {
		if (invt > 0) {
			animation = hurt;
		} else {
			animation = idle;
		}

		renderWithOutline(animation);
	}

	private AnimationData animation;

	@Override
	public void destroy() {
		super.destroy();
		body = World.removeBody(body);
	}

	@Override
	public float getWeight() {
		return 0.1f;
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x + 3, y, w - 6, h + 4, 4);
	}

	@Override
	protected State getAi(String state) {
		return new IdleState();
	}

	public class IdleState extends Mob.State<Fly> {

	}
}