package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;

public class RollTrigger extends SaveableEntity {
	private Body body;
	private float t;

	@Override
	public void init() {
		super.init();

		body = World.createSimpleBody(this, 0, 0, 16, 5 * 16, BodyDef.BodyType.DynamicBody, true);
		body.setTransform(x, y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		t += dt;
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		body.setTransform(x, y, 0);
	}

	private boolean fired;

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (!fired && this.t >= 0.1f && entity instanceof Player) {
			fired = true;

			Ui.ui.addControl("[white]" + Input.instance.getMapping("roll") + " [gray]" + Locale.get("roll"));
			Player.instance.tt = 0;
			Player.instance.step = 3;
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		body = World.removeBody(body);
	}
}