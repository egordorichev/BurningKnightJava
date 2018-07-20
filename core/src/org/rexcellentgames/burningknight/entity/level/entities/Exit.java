package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.fx.LadderFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

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
		
		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}

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