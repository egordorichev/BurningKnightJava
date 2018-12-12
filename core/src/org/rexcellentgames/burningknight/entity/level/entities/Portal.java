package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.fx.LadderFx;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.game.state.InventoryState;
import org.rexcellentgames.burningknight.game.state.MainMenuState;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Portal extends SaveableEntity {
	private Body body;
	private LadderFx fx;
	private static TextureRegion region;
	public static LadderFx exitFx;
	public static float al;

	private byte type;

	public void setType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return this.type;
	}

	@Override
	public void init() {
		super.init();

		this.body = World.createSimpleBody(this, 4, 4, 8, 8, BodyDef.BodyType.DynamicBody, true);

		if (this.body != null) {
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		}

		if (Level.GENERATED) {
			this.addSelf();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	private void addSelf() {
		Log.info("Checking for exit ladder");

		if (Dungeon.loadType != Entrance.LoadType.GO_DOWN && (Dungeon.ladderId == this.type || Player.ladder == null)) {
			Player.ladder = this;
			Log.info("Set exit ladder!");
		}
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();
		float dt = Gdx.graphics.getDeltaTime();

		if (!Dungeon.game.getState().isPaused()) {
			for (int i = parts.size() - 1; i >= 0; i--) {
				Particle p = parts.get(i);
				p.a += p.av * dt * 1.5f;
				p.av += dt * 3;

				p.d -= p.junk ? dt * 15 : dt * 10;
				p.rad -= dt * 1f;
				p.t += dt;

				if (p.rad <= 0 || p.d <= 0) {
					parts.remove(i);
				}

				p.readPosition();
				p.al = Math.min(0.6f, p.al + dt);
			}
		}

		for (Particle p : parts) {
			if (!p.junk) {
				if (p.black) {
					Graphics.shape.setColor(0, 0, 0, p.al);
				} else {
					Color cl = ColorUtils.HSV_to_RGB(Dungeon.time * 20 % 360 - p.d, 360, 360);
					float v = Math.max(0.1f, 1.2f - p.t * 0.7f);
					Graphics.shape.setColor(cl.r * v, cl.g * v, cl.b * v, p.al);
				}

				Graphics.shape.circle(p.x, p.y, p.rad);
			}
		}

		for (Particle p : parts) {
			if (p.junk) {
				float size = p.rad;
				float a = p.a * 10f;
				Graphics.shape.setColor(1, 1, 1, p.al * 0.5f);
				Graphics.shape.rect(p.x - size / 2, p.y - size / 2, size / 2, size / 2, size, size, 1, 1, a);
				Graphics.shape.setColor(1, 1, 1, p.al);
				Graphics.shape.rect(p.x - size / 2, p.y - size / 2, size / 2, size / 2, size, size, 0.5f, 0.5f, a);
			}
		}

		Graphics.endAlphaShape();
	}

	private float last;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;
		depth = -9;

		last += dt;

		if (last >= 0.01f) {
			last = 0;
			parts.add(new Particle(this));
			parts.add(new Particle(this));
			parts.add(new Particle(this));
		}

		float dx = Player.instance.x - this.x - 2;
		float dy = Player.instance.y - this.y - 4;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		float dd = 64f;

		if (d < dd) {
			MainMenuState.voidMusic.play();
			float f = (dd - d) / dd;
			MainMenuState.voidMusic.setVolume(f * Settings.music);
			Player.instance.velocity.x -= dx / d * dt * 4000 * f;
			Player.instance.velocity.y -= dy / d * dt * 4000 * f;
		} else {
			MainMenuState.voidMusic.setVolume(0);
			MainMenuState.voidMusic.pause();
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.type = reader.readByte();

		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		this.addSelf();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeByte(this.type);
	}

	@Override
	public void onCollision(Entity entity) {
		if (this.t >= 0.3f && entity instanceof Player && !Player.sucked) {
			Player.sucked = true;
			Player.instance.setUnhittable(true);

			Camera.shake(3);

			Dungeon.darkR = Dungeon.MAX_R;
			Player.instance.rotating = true;
			Player.instance.setUnhittable(true);
			Camera.follow(null);

			Vector3 vec = Camera.game.project(new Vector3(Player.instance.x, Player.instance.y + 8, 0));
			Camera.noMove = true;
			vec = Camera.ui.unproject(vec);
			vec.y = Display.GAME_HEIGHT - vec.y / Display.UI_SCALE;

			Dungeon.darkX = vec.x / Display.UI_SCALE;
			Dungeon.darkY = vec.y;

			Tween.to(new Tween.Task(Dungeon.MAX_R * 0.25f, 0.3f, Tween.Type.QUAD_OUT) {
				@Override
				public float getValue() {
					return Dungeon.darkR;
				}

				@Override
				public void setValue(float value) {
					Dungeon.darkR = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(0, 0.3f, Tween.Type.QUAD_OUT) {
						@Override
						public float getValue() {
							return Dungeon.darkR;
						}

						@Override
						public void setValue(float value) {
							Dungeon.darkR = value;
						}

						@Override
						public void onEnd() {
							if (Dungeon.depth == -2) {
								Dungeon.goToSelect = true;
							} else {
								GameSave.inventory = true;
								Dungeon.toInventory = true;
								Player.instance.rotating = false;
								Dungeon.loadType = Entrance.LoadType.GO_DOWN;
								InventoryState.depth = Dungeon.depth + 1;
							}

							Camera.noMove = false;

							Dungeon.setBackground2(new Color(0, 0, 0, 1));
							Player.sucked = false;
						}
					}).delay(1f);
				}
			}).delay(1f);
		}
	}

	/*
		ideas:
		box shadow under menu logo so it looks glowing
		logo changing color with the bg?
		things in portal
		sx scale tweening instead of player/move flipping
	 */

	private ArrayList<Particle> parts = new ArrayList<>();

	private class Particle {
		public float x;
		public float y;
		public float rad;
		public float sx;
		public float sy;
		public float a;
		public float d;
		public float av;
		public boolean black;
		public float al;
		public float t;
		public boolean junk;

		public Particle(Portal portal) {
			al = 0;
			a = Random.newFloat((float) (Math.PI * 2));
			float v = (float) (Math.PI);
			black = a % v > (v * 0.5f);

			junk = Random.chance(1f);
			d = junk ? Random.newFloat(16, 32f) : 16f;
			rad = junk ? 6f : 3f;
			sx = portal.x;
			sy = portal.y;

			readPosition();
		}

		public void readPosition() {
			x = (float) (sx + 8 + Math.cos(a) * d);
			y = (float) (sy + 8 + Math.sin(a) * d);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Player && this.fx != null) {
			this.fx.remove();
			this.fx = null;
			exitFx = null;
		}
	}
}