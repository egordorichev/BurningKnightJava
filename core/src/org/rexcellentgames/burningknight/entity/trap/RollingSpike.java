package org.rexcellentgames.burningknight.entity.trap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class RollingSpike extends SaveableEntity {
	{
		alwaysActive = true;
	}

	private static AnimationData animations = Animation.make("actor-rolling-spike").get("idle");
	private Body body;
	private TextureRegion region;

	@Override
	public void init() {
		super.init();

		region = animations.getFrames().get(Random.newInt(animations.getFrames().size())).frame;
		body = World.createCircleBody(this, 1, 1, 7, BodyDef.BodyType.DynamicBody, false);
		body.setSleepingAllowed(false);

		MassData data = new MassData();
		data.mass = 1000000000f;
		
		if (this.body != null) {
			this.body.setMassData(data);
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.velocity.x = reader.readFloat();
		this.velocity.y = reader.readFloat();
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeFloat(this.velocity.x);
		writer.writeFloat(this.velocity.y);
	}

	@Override
	public void render() {
		Graphics.render(region, this.x + 8, this.y + 8, this.a, 8, 8, false, false);
	}

	@Override
	public void renderShadow() {
		Graphics.shadowSized(this.x, this.y + 2, this.w, this.h, 6);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof Player && ((Player) entity).isRolling()) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	private float a;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.a += dt * -(this.velocity.x == 0 ? this.velocity.y : this.velocity.x) * 10;

		this.x += this.velocity.x * dt;
		this.y += this.velocity.y * dt;
		this.body.setTransform(x, y, 0);

		for (Player player : colliding) {
			if (player.getInvt() == 0) {
				player.modifyHp(-2, this, true);
				player.knockBackFrom(this, 2f);

				Achievements.unlock(Achievements.UNLOCK_MEATBOY_AXE);
			}
		}
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity == null || entity instanceof Door || entity instanceof SolidProp) {
			this.velocity.x *= -1f;
			this.velocity.y *= -1f;
		} else if (entity instanceof Player && !((Player) entity).isRolling()) {
			this.colliding.add((Player) entity);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Player) {
			this.colliding.remove(entity);
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		this.body = World.removeBody(this.body);
	}

	private ArrayList<Player> colliding = new ArrayList<>();
}