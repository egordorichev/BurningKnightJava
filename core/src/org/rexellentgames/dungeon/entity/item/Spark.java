package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

public class Spark extends Entity {
	private static Animation animations = Animation.make("fx-spark");
	private AnimationData animation = animations.get("idle");
	public float a;
	public boolean inUi;

	public static void random(Entity entity) {
		random(entity.x, entity.y, entity.w, entity.h);
	}

	public static void random(float x, float y, float w, float h) {
		random(x, y, w, h, false);
	}

	public static void random(float x, float y, float w, float h, boolean ui) {
		if (Random.newFloat() > 0.97f) {
			Spark spark = new Spark();

			spark.inUi = ui;
			spark.a = Random.newFloat(360);
			spark.x = Random.newFloat(x, x + w) - 3.5f;
			spark.y = Random.newFloat(y, y + h) - 3.5f;

			Dungeon.area.add(spark);
		}
	}

	@Override
	public void init() {
		super.init();

		this.depth = 5;

		// TODO: another tween function?
		Tween.to(new Tween.Task(this.a + Random.newFloat(-90f, 90f), 1f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		if (inUi) {
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		}

		this.animation.render(this.x, this.y, false, false, 3.5f, 3.5f, this.a);

		if (inUi) {
			Graphics.batch.setProjectionMatrix(Camera.game.combined);
		}
	}
}