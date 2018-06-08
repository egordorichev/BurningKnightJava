package org.rexellentgames.dungeon.entity.trap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.entity.level.entities.SolidProp;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

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

		body = World.createCircleBody(this, 0, 0, 8, BodyDef.BodyType.DynamicBody, false);

		MassData data = new MassData();
		data.mass = 1000000000f;
		this.body.setMassData(data);

		body.setTransform(this.x, this.y, 0);
		body.setLinearVelocity(this.vel);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.vel.x = reader.readFloat();
		this.vel.y = reader.readFloat();
		body.setLinearVelocity(this.vel);
		body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeFloat(this.vel.x);
		writer.writeFloat(this.vel.y);
	}

	@Override
	public void render() {
		Graphics.render(region, this.x + 8, this.y + 8, this.a, 8, 8, false, false);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, 16, 16);
	}

	private float a;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.a += dt * -(this.vel.x == 0 ? this.vel.y : this.vel.x) * 10;

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;
		body.setLinearVelocity(this.vel);


		for (Player player : colliding) {
			if (player.getInvt() == 0) {
				player.modifyHp(-2, null);
				player.knockBack(this, 400f);
			}
		}
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity == null || entity instanceof Door || entity instanceof SolidProp) {
			this.vel.x *= -1f;
			this.vel.y *= -1f;

			if (this.body != null) {
				body.setLinearVelocity(this.vel);
			}
		} else if (entity instanceof Player) {
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