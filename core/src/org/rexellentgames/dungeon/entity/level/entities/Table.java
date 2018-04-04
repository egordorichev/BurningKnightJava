package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.file.FileReader;

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

		this.body = this.createBody(0, 10, (int) w, (int) h - 14, BodyDef.BodyType.StaticBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void render() {
		super.render();
		this.animation.render(this.x, this.y, false);
	}
}