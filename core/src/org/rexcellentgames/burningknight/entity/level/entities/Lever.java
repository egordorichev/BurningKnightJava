package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Lever extends SaveableEntity {
	public static Animation animations = Animation.make("lever");
	private static TextureRegion off = animations.getFrames("idle").get(0).frame;
	private static TextureRegion onRegion = animations.getFrames("idle").get(1).frame;

	public boolean on;
	private Body body;
	private boolean colliding;
	private float al;
	public boolean shouldBeOn;
	public boolean did;

	@Override
	public void init() {
		super.init();
		this.body = World.createSimpleBody(this, 0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.on = reader.readBoolean();
		this.shouldBeOn = reader.readBoolean();
		this.did = reader.readBoolean();

		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Player) {
			this.colliding = false;
		}
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player) {
			this.colliding = true;
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.on);
		writer.writeBoolean(this.shouldBeOn);
		writer.writeBoolean(this.did);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	public boolean isOn() {
		return on;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.al += ((this.colliding ? 1 : 0) - this.al) * dt * 10;

		if (this.al >= 0.5f && Input.instance.wasPressed("interact")) {
			this.on = !this.on;
			Camera.shake(8);
			this.onToggle();
		}
	}

	protected void onToggle() {

	}

	@Override
	public void render() {
		TextureRegion region = on ? onRegion : off;

		if (al > 0) {
			Graphics.batch.end();
			Mob.shader.begin();
			Mob.shader.setUniformf("u_color", ColorUtils.WHITE);
			Mob.shader.setUniformf("u_a", al);
			Mob.shader.end();
			Graphics.batch.setShader(Mob.shader);
			Graphics.batch.begin();

			for (int yy = 0; yy < 2; yy++) {
				for (int xx = -1; xx < 2; xx++) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(region, x + xx, y + yy);
					}
				}
			}

			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}

		Graphics.render(region, this.x, this.y);
	}
}