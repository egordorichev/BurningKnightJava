package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
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
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Button extends SaveableEntity {
	private static Animation animations = Animation.make("button");
	private static TextureRegion tile = animations.getFrames("idle").get(0).frame;
	private static TextureRegion off = animations.getFrames("idle").get(1).frame;
	private static TextureRegion on = animations.getFrames("idle").get(2).frame;

	private Body body;
	private boolean down;

	{
		depth = -1;
	}

	@Override
	public void init() {
		super.init();
		this.body = World.createSimpleBody(this, 2, 2, 12, 12, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);
		if (this.down) {
			return;
		}

		if (entity instanceof Player) {
			this.colliding = true;
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Player) {
			this.colliding = false;
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.al += (((this.colliding && !this.down) ? 1 : 0) - this.al) * dt * 8;

		if (!this.down && this.al >= 0.5f && Input.instance.wasPressed("interact")) {
			this.down = true;
			Camera.shake(8);
			this.onPress();
			// todo: sfx
		}
	}

	private float al;

	public void onPress() {

	}

	public boolean isDown() {
		return down;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.down = reader.readBoolean();
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.down);
	}

	@Override
	public void render() {
		Graphics.render(tile, this.x, this.y);
		TextureRegion region = down ? on : off;

		if (al > 0) {
			Graphics.batch.end();
			Mob.shader.begin();
			Mob.shader.setUniformf("u_color", new Vector3(1, 1, 1));
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

	boolean colliding;
}