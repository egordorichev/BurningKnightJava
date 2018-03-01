package org.rexellentgames.dungeon.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Log;

import java.util.ArrayList;

public class LoadState extends State {
	private boolean ready = false;

	@Override
	public void init() {
		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}

		if (Dungeon.world != null) {
		//	Dungeon.world.dispose();
		}

		Dungeon.world = new World(new Vector2(0, 0), true);

		Dungeon.area = new Area();
		Dungeon.area.add(new Camera());

		if (Dungeon.light == null) {
			Dungeon.light = new RayHandler(Dungeon.world);

			Dungeon.light.setBlurNum(10);
			Dungeon.light.setAmbientLight(0f);
		}

		Dungeon.level = new RegularLevel();
		Dungeon.area.add(Dungeon.level);

		new Thread(new Runnable() {
			@Override
			public void run() {
				Dungeon.level.load(Level.DataType.PLAYER);
				Dungeon.level.load(Level.DataType.LEVEL);

				Dungeon.level.loadPassable();
				UiLog.instance.print("[orange]Welcome to level " + (Dungeon.depth + 1) + "!");

				ready = true;
			}
		}).run();
	}

	@Override
	public void update(float dt) {
		if (this.ready) {
			Game.instance.setState(new InGameState());
			Camera.instance.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, "Loading...", 10, 10);
	}
}