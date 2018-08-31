package org.rexcellentgames.burningknight.entity.level.entities.fx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;

public class Table extends SaveableEntity {
	private static Animation animations = Animation.make("prop-throne", "-desk");
	private AnimationData animation;
	private Body body;

	{
		w = 42;
		h = 28;
	}

	@Override
	public void init() {
		super.init();
		this.animation = animations.get("idle");

		this.body = World.createSimpleBody(this, 0, 10, (int) w, (int) h - 14, BodyDef.BodyType.StaticBody, false);
		
		if (this.body != null) {
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void render() {
		super.render();
		this.animation.render(this.x, this.y, false);
	}
}