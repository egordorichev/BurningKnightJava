package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.fx.LadderFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Entrance extends SaveableEntity {
	private Body body;
	private LadderFx fx;

	public static byte NORMAL = 0;
	public static byte ENTRANCE_TUTORIAL = 1;

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

		this.body = World.createSimpleBody(this, 0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		
		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}

		if (Level.GENERATED) {
			this.addSelf();
		}

		RegularLevel.ladder = this;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	private void addSelf() {
		if (Dungeon.loadType == LoadType.GO_DOWN && (Dungeon.ladderId == this.type || Player.ladder == null)) {
			Player.ladder = this;
		}
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
	public void render() {
		Graphics.render(Terrain.entrance, this.x, this.y);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y + 4, 16, 32);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && this.fx == null && Dungeon.depth > 0) {
			this.fx = new LadderFx(this, "ascend");
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
