package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Button extends SaveableEntity {
	private static Animation animations = Animation.make("button");
	private static TextureRegion off = animations.getFrames("idle").get(0).frame;
	private static TextureRegion on = animations.getFrames("idle").get(1).frame;

	private Body body;
	private boolean down;

	{
		depth = -1;
	}

	@Override
	public void init() {
		super.init();
		this.body = World.createSimpleBody(this, 0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);
		if (this.down) {
			return;
		}

		if (entity instanceof Player) {
			this.down = true;
			Camera.shake(4);
			this.onPress();
			// todo: sfx
		}
	}

	public void onPress() {

	}

	public boolean isDown() {
		return down;
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
		Graphics.render(down ? on : off, this.x, this.y);
	}
}