package org.rexellentgames.dungeon.entity.creature.buff.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

public class FlameFx extends Entity {
	public static Color red = Color.valueOf("#ac3232");
	public static Color orange = Color.valueOf("#df7126");

	{
		alwaysActive = true;
	}

	private Color color;
	private float t;
	private float size = 1f;
	private float range = 1;
	private float angle;
	private Entity owner;

	public FlameFx(Entity owner) {
		this.owner = owner;
		this.color = Random.newFloat() < 0.7 ? orange : red;
		this.t = Random.newFloat(1024);
		this.depth = 6;
		this.x = owner.x;
		this.y = owner.y;

		Tween.to(new Tween.Task(4, 0.05f) {
			@Override
			public float getValue() {
				return size;
			}

			@Override
			public void setValue(float value) {
				size = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 1f) {
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
		this.t += dt;
		this.y += dt * 15;

		if (this.range < 6) {
			this.range += dt * 15;
		}

		this.x = this.owner.x + (float) (Math.cos(this.t) * this.range);
		this.angle = (float) Math.cos(this.t) * 20;

		if (this.y - this.owner.y > 16) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		float s = this.size / 2;

		Graphics.shape.setColor(this.color.r, this.color.g, this.color.b, 0.8f);
		Graphics.shape.rect(this.x + this.owner.w / 2, this.y + this.owner.h / 5, s, s, this.size,
			this.size, 1, 1, this.angle);

		Graphics.shape.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();
	}
}