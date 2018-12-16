package org.rexcellentgames.burningknight.entity.creature.fx;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class ManaFx extends SaveableEntity {
	public boolean half;
	private Body body;
	private PointLight light;

	private static TextureRegion star = Graphics.getTexture("ui-mana_star");
	private static TextureRegion halfStar = Graphics.getTexture("ui-half_star");

	@Override
	public void init() {
		super.init();

		this.w = half ? halfStar.getRegionWidth() : star.getRegionWidth();
		this.h = star.getRegionHeight();

		body = World.createSimpleBody(this, 0, 0, w, h, BodyDef.BodyType.DynamicBody, false);
		body.setTransform(this.x, this.y, 0);
		light = World.newLight(32, new Color(0, 1, 1, 1), 64, 0, 0);
		light.setPosition(this.x + w / 2, this.y + h / 2);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(half);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		half = reader.readBoolean();
		body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void render() {
		Graphics.render(half ? halfStar : star, this.x, this.y);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		light.setPosition(this.x + w / 2, this.y + h / 2);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (player.getManaMax() - player.getMana() > 1) {
				player.modifyMana(half ? 1 : 2);
				done = true;
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);
		body = World.removeBody(body);
	}
}