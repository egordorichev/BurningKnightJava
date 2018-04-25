package org.rexellentgames.dungeon.entity.level.entities.chest;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Chest extends SaveableEntity {
	private AnimationData data;
	protected Body body;
	protected boolean open;

	@Override
	public void init() {
		super.init();

		this.data = this.getClosedAnim();
		this.data.setAutoPause(true);
		this.body = this.createBody(5, 0, 16, 11, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void onCollision(Entity entity) {
		if (!this.open && entity instanceof Player) {
			this.open = true;
			this.data = this.getOpenAnim();
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		this.body.getWorld().destroyBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.open = reader.readBoolean();
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.open);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.data.update(dt)) {
			if (this.data == this.getOpenAnim()) {
				this.data = this.getOpenedAnim();
			}
		}
	}

	@Override
	public void render() {
		this.data.render(this.x, this.y, false);
	}

	protected AnimationData getClosedAnim() {
		return null;
	}

	protected AnimationData getOpenedAnim() {
		return null;
	}

	protected AnimationData getOpenAnim() {
		return null;
	}
}