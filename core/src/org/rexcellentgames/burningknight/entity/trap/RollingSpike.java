package org.rexcellentgames.burningknight.entity.trap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
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

		MassData data = new MassData();
		data.mass = 1000000000f;
		
		if (this.body != null) {
			this.body.setMassData(data);
			this.body.setTransform(this.x, this.y, 0);
			this.body.setLinearVelocity(this.vel);
		}
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
		Graphics.shadowSized(this.x, this.y, this.w, this.h, 6);
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
				player.knockBackFrom(this, 400f);
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