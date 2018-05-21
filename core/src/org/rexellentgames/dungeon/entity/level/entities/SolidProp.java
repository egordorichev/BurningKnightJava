package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class SolidProp extends Prop {
	protected Body body;
	protected Rectangle collider;

	@Override
	public void init() {
		super.init();

		if (this.collider != null) {
			this.body = World.createSimpleBody(this, collider.x, collider.y, collider.width, collider.height, BodyDef.BodyType.StaticBody, false);
			this.body.setTransform(this.x, this.y, 0);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}
	}

	@Override
	public void destroy() {
		this.body = World.removeBody(this.body);
	}
}