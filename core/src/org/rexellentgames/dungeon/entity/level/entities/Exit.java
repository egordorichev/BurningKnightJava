package org.rexellentgames.dungeon.entity.level.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.fx.LadderFx;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class Exit extends SaveableEntity {
	private Body body;
	private PointLight light;
	private LadderFx fx;

	public static int ID = -1;
	private int id;

	@Override
	public void init() {
		super.init();
		this.body = this.createBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.light = new PointLight(Dungeon.light, 128, new Color(1, 0.8f, 0.8f, 0.5f),
			64, this.x + 8, this.y + 8);
		this.body.setTransform(this.x, this.y, 0);

		if (Level.GENERATED) {
			this.add();
		}

		this.depth = -1;
		this.id = ID++;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private void add() {
		if (Dungeon.up && Player.instance != null && (Dungeon.ladderId == this.id || !Player.REGISTERED)) {
			Player.instance.tp(this.x, this.y - 2);

			Log.info("Set player position to " + (int) (this.x / 16) + ":" + (int) (this.y / 16) + ", self id = " + this.id);

			if (BurningKnight.instance != null) {
				BurningKnight.instance.tpToPlayer();
			}

			if (Dungeon.ladderId == this.id) {
				Player.REGISTERED = true;
			}
		}
	}

	@Override
	public void render() {
		Graphics.render(Graphics.tiles, Terrain.EXIT, this.x, this.y);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.light.remove(true);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.body.setTransform(this.x, this.y, 0);
		this.light.setPosition(this.x + 8, this.y + 8);
		this.add();
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && this.fx == null) {
			this.fx = new LadderFx(this, "Descend [X]");

			this.area.add(this.fx);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Player && this.fx != null) {
			this.fx.done = true;
			this.fx = null;
		}
	}
}