package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
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

public class Entrance extends SaveableEntity {
	private Body body;
	private LadderFx fx;

	public static byte NORMAL = 0;
	public static byte CASTLE_ENTRANCE_OPEN = 1;
	public static byte CASTLE_ENTRANCE_CLOSED = 2;
	public static byte ENTRANCE_TUTORIAL = 3;

	private byte type;

	public void setType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return this.type;
	}

	public enum LoadType {
		GO_UP,
		GO_DOWN,
		FALL_DOWN,
		RUNNING,
		READING
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
	}

	private void add() {
		if (Dungeon.loadType == LoadType.GO_DOWN && Player.instance != null && (Dungeon.ladderId == this.type || !Player.REGISTERED)) {
			Player.instance.tp(this.x, this.y - 2);

			if (Dungeon.ladderId == this.type) {
				Player.REGISTERED = true;
			}
		}
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
	public void render() {
		Graphics.render(Terrain.entrance, this.x, this.y);
	}
	
	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && this.fx == null && !Network.SERVER) {
			this.fx = new LadderFx(this, "Ascend");
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
