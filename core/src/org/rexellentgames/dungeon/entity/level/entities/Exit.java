package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.fx.LadderFx;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Exit extends SaveableEntity {
	private Body body;
	private LadderFx fx;
	private static TextureRegion region;

	private byte type;

	public void setType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return this.type;
	}

	@Override
	public void init() {
		super.init();

		this.alwaysActive = true;

		this.body = this.createBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);

		if (Level.GENERATED) {
			this.add();
		}

		this.depth = -1;
	}

	private void add() {
		if (Dungeon.loadType == Entrance.LoadType.GO_UP && Player.instance != null && (Dungeon.ladderId == this.type || !Player.REGISTERED)) {
			Player.instance.tp(this.x, this.y - 2);

			if (BurningKnight.instance != null) {
				BurningKnight.instance.findStartPoint();
			}

			if (Dungeon.ladderId == this.type) {
				Player.REGISTERED = true;
			}
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
		Graphics.render(Terrain.exit, this.x, this.y);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.type = reader.readByte();

		this.body.setTransform(this.x, this.y, 0);
		this.add();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeByte(this.type);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && this.fx == null && !Network.SERVER) {
			this.fx = new LadderFx(this, "Descend");

			this.area.add(this.fx);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Player && this.fx != null) {
			this.fx.done = true;
			this.fx = null;
		}
	}
}