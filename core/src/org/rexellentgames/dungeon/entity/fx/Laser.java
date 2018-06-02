package org.rexellentgames.dungeon.entity.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.HpFx;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Laser extends Entity {
	public static TextureRegion start = Graphics.getTexture("laser (start)");
	public static TextureRegion startOverlay = Graphics.getTexture("laser (start_over)");
	public static TextureRegion end = Graphics.getTexture("laser (end)");
	public static TextureRegion endOverlay = Graphics.getTexture("laser (end_over)");
	public static TextureRegion mid = Graphics.getTexture("laser (mid)");
	public static TextureRegion midOverlay = Graphics.getTexture("laser (mid_over)");
	public static TextureRegion circ = Graphics.getTexture("laser (circ)");
	public static TextureRegion circOverlay = Graphics.getTexture("laser (circ_over)");

	private Body body;
	public float a;
	private float al = 0.3f;
	public int damage;
	public boolean crit;
	public Color shade = new Color(1, 0, 0, 1);
	public Color color = new Color(1, 0.6f, 0.6f, 1);
	public Creature owner;

	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 1;
	}

	@Override
	public void init() {
		super.init();

		Tween.to(new Tween.Task(1, 0.05f) {
			@Override
			public float getValue() {
				return al;
			}

			@Override
			public void setValue(float value) {
				al = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.3f) {
					@Override
					public float getValue() {
						return al;
					}

					@Override
					public void setValue(float value) {
						al = value;
					}

					@Override
					public void onEnd() {
						setDone(true);
					}
				});
			}
		});

		Log.physics("Creating centred body for laser");

		if (World.world.isLocked()) {
			Log.physics("World is locked! Failed to create body");
			return;
		}

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;

		body = World.world.createBody(def);
		PolygonShape poly = new PolygonShape();

		float x = 0f;
		float w = 6f;
		float h = this.w - 8;
		float y = 0f;

		poly.set(new Vector2[]{
			new Vector2(x - w / 2, y), new Vector2(x + w / 2, y),
			new Vector2(x - w / 2, y + h), new Vector2(x + w / 2, y + h)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = true;

		body.createFixture(fixture);
		body.setUserData(this);
		poly.dispose();

		this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	public void render() {
		start.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		int v = (int) (Math.ceil(this.w / 16) - 1);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		double a = Math.toRadians(this.a + 90);

		shade.a = this.al;
		color.a = this.al;

		for (int i = 0; i < v + 1; i++) {
			if (i == 0) {
				Graphics.batch.setColor(shade);
				Graphics.render(start, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
				Graphics.batch.setColor(color);
				Graphics.render(startOverlay, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
			} else if (i == v) {
				Graphics.batch.setColor(shade);
				Graphics.render(end, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
				Graphics.batch.setColor(color);
				Graphics.render(endOverlay, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
			} else {
				Graphics.batch.setColor(shade);
				Graphics.render(mid, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
				Graphics.batch.setColor(color);
				Graphics.render(midOverlay, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
			}
		}

		Graphics.batch.setColor(1, 1, 1, this.al);

		for (int i = 0; i < v + 1; i++) {
			if (i == 0) {
				Graphics.render(startOverlay, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
			} else if (i == v) {
				Graphics.render(endOverlay, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
			} else {
				Graphics.render(midOverlay, this.x + (float) Math.cos(a) * i * 16, this.y + (float) Math.sin(a) * i * 16, this.a, 8, 8, false, false);
			}
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		start.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void renderFrom(Point from, Point to) {
		float dx = to.x - from.x;
		float dy = to.y - from.y;
		double d = Math.sqrt(dx * dx + dy * dy);
		double a = Math.atan2(dy, dx);

		start.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		int v = (int) (Math.ceil(d / 16) - 1);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		double aa = a;

		float an = (float) Math.toDegrees(aa - Math.PI / 2);

		shade.a = this.al;
		color.a = this.al;

		float s = (float) (d / 16f);

		Graphics.batch.setColor(shade);
		Graphics.render(mid, from.x, from.y, an, 8, 8, false, false, 1, s);
		Graphics.render(circ, to.x - (float) Math.cos(aa) * 8, to.y - (float) Math.sin(aa) * 8, an, 8, 8, false, false);
		Graphics.batch.setColor(color);
		Graphics.render(midOverlay, from.x, from.y, an, 8, 8, false, false, 1, s);
		Graphics.render(circOverlay, to.x - (float) Math.cos(aa) * 8, to.y - (float) Math.sin(aa) * 8, an, 8, 8, false, false);

		Graphics.batch.setColor(1, 1, 1, this.al);
		Graphics.render(midOverlay, from.x, from.y, an, 8, 8, false, false);
		Graphics.render(circOverlay, to.x - (float) Math.cos(aa) * 8, to.y - (float) Math.sin(aa) * 8, an, 8, 8, false, false);

		Graphics.batch.setColor(1, 1, 1, 1);
		start.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Mob) {
			HpFx fx = ((Mob) entity).modifyHp(-this.damage, this.owner);

			if (fx != null) {
				fx.crit = this.crit;
			}
		}
	}
}