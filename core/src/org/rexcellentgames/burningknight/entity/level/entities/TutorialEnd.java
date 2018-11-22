package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;

public class TutorialEnd extends SaveableEntity {
	private Body body;

	@Override
	public void init() {
		super.init();

		body = World.createSimpleBody(this, 0, 0, 64, 16, BodyDef.BodyType.DynamicBody, true);
		body.setTransform(x, y, 0);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		body.setTransform(x, y, 0);
	}

	private boolean did;

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player && !did) {
			did = true;
			Tween.to(new Tween.Task(0, 0.2f) {
				@Override
				public float getValue() {
					return Dungeon.dark;
				}

				@Override
				public void setValue(float value) {
					Dungeon.dark = value;
				}

				@Override
				public void onEnd() {
					Dungeon.goToLevel(-2);
					Dungeon.dark = 1;
				}
			});
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		body = World.removeBody(body);
	}
}