package org.rexellentgames.dungeon.entity.level.entities.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

public class ChasmFx extends Entity {
	private static Color color = Color.valueOf("#696a6a");

	private float size;
	private float a;
	private float vx;
	private float vy;

	public ChasmFx(float x, float y) {
		this.x = x;
		this.y = y;
		this.vx = Random.newFloat(-3f, 3f);
		this.vy = Random.newFloat(2.5f, 3f);

		this.size = Random.newFloat(2f, 4f);

		Tween.to(new Tween.Task(1, 0.5f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 3f) {
					@Override
					public float getValue() {
						return a;
					}

					@Override
					public void setValue(float value) {
						a = value;
					}

					@Override
					public void onEnd() {
						done = true;
					}
				});

				Tween.to(new Tween.Task(1, 3f) {
					@Override
					public float getValue() {
						return size;
					}

					@Override
					public void setValue(float value) {
						size = value;
					}
				});
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.y += dt * this.vy;
		this.x -= dt * this.vx;
	}

	@Override
	public void render() {
		Graphics.batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		float s = this.size / 2;

		Graphics.shape.setColor(this.color.r, this.color.g, this.color.b, this.a);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, 0);

		Graphics.shape.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();
	}
}