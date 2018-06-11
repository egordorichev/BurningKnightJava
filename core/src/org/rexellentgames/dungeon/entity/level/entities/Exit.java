package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.entities.fx.LadderFx;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Exit extends SaveableEntity {
	private Body body;
	private LadderFx fx;
	private static TextureRegion region;


	public static Exit instance;
	private byte type;

	public void setType(byte type) {
		this.type = type;

		if (type == Entrance.NORMAL) {
			instance = this;
		}
	}

	public byte getType() {
		return this.type;
	}

	@Override
	public void init() {
		super.init();

		this.alwaysActive = true;

		this.body = World.createSimpleBody(this, 0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);

		if (Level.GENERATED) {
			this.addSelf();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	private void addSelf() {
		if (Dungeon.loadType == Entrance.LoadType.GO_UP && (Dungeon.ladderId == this.type || Player.ladder == null)) {
			Player.ladder = this;
		}

		if (this.type == Entrance.NORMAL) {
			instance = this;
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0, 0, 0.0f, 0.5f, 3f, false);
		}
	}

	@Override
	public void render() {

	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.type = reader.readByte();

		this.body.setTransform(this.x, this.y, 0);
		this.addSelf();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeByte(this.type);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && this.fx == null) {
			this.fx = new LadderFx(this, this.type == Entrance.ENTRANCE_TUTORIAL ? "tutorial" : "descend");
			this.area.add(this.fx);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Player && this.fx != null) {
			this.fx.remove();
			this.fx = null;
		}
	}
}