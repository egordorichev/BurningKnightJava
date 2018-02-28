package org.rexellentgames.dungeon.entity.level.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class Entrance extends SaveableEntity {
	private Body body;
	private PointLight light;

	@Override
	public void init() {
		super.init();
		this.body = this.createBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.light = new PointLight(this.area.getState().getLight(), 128, new Color(1, 0.8f, 0.8f, 0.5f),
			64, this.x + 8, this.y + 8);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.body.setTransform(this.x, this.y, 0);
		this.light.setPosition(this.x + 8, this.y + 8);
	}

	@Override
	public void render() {
		Graphics.render(Graphics.tiles, Terrain.ENTRANCE, this.x, this.y);
	}
}
