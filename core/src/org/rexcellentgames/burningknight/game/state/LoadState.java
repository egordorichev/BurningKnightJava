package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.AnswerButton;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.rooms.HandmadeRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.PathFinder;

public class LoadState extends State {
	private boolean ready = false;
	private float alp;
	private String s;

	@Override
	public void init() {
		Audio.stop();

		this.s = "Doing secret stuff...";
		Dungeon.grayscale = 0;

		switch (Dungeon.loadType) {
			case GO_UP: this.s = Locale.get("ascending"); break;
			case GO_DOWN: this.s = Locale.get("descending"); break;
			case LOADING: this.s = Locale.get("loading"); break;
		}

		if (Ui.ui != null) {
			Ui.ui.healthbars.clear();
		}

		Dungeon.setBackground(new Color(0, 0, 0, 1));

		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.dark = 1;
	}

	private boolean second;

	@Override
	public void update(float dt) {
		if (this.ready) {
			Dungeon.darkR = 0;
			Dungeon.game.setState(new InGameState());
		}
	}

	private boolean third;

	private void showError(String err) {
		Log.error(err);
		error = true;
		errorString = err;
		Graphics.layout.setText(Graphics.medium, errorString);
		ew = Graphics.layout.width / 2;
		et = 0.3f;

		Dungeon.ui.add(new UiButton("start_new_game", Display.UI_WIDTH / 2, Display.UI_HEIGHT / 3) {
			@Override
			public void onClick() {
				Dungeon.newGame();
			}
		});
	}

	private void runThread() {
		HandmadeRoom.init();

		Dungeon.speed = 1f;
		Dungeon.dark = 1;
		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.ui.destroy();
		Dungeon.area.destroy();
		Exit.instance = null;

		Player.ladder = null;
		Level.GENERATED = false;
		Shopkeeper.instance = null;

		World.init();

		Player.all.clear();
		Mob.all.clear();
		ItemHolder.getAll().clear();

		Chest.all.clear();
		Mimic.all.clear();
		AnswerButton.all.clear();

		PlayerSave.all.clear();
		LevelSave.all.clear();

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Level lvl = Level.forDepth(Dungeon.depth - 1);

				boolean error;

				try {
					error = !SaveManager.load(SaveManager.Type.GAME);
				} catch (Exception e) {
					Log.report(e);
					error = true;
				}

				if (error) {
					showError("Failed to load game!");
					return;
				}

				try {
					error = !SaveManager.load(SaveManager.Type.LEVEL);
				} catch (Exception e) {
					Log.report(e);
					error = true;
				}

				if (error) {
					showError("Failed to load level!");
					return;
				}

				Dungeon.area.add(new Camera());

				try {
					error = !SaveManager.load(SaveManager.Type.PLAYER);
				} catch (Exception e) {
					Log.report(e);
					error = true;
				}

				if (error) {
					showError("Failed to load player!");
					return;
				}

				Dungeon.level.loadPassable();
				Dungeon.level.addPhysics();

				Audio.play(Dungeon.level.getMusic());

				if (!Dungeon.level.same(lvl)) {
					Audio.reset();
				}

				if (Player.instance == null) {
					showError("Failed to load player!");
					Dungeon.newGame();
					return;
				}

				PathFinder.setMapSize(Level.getWidth(), Level.getHeight());

				Log.info("Loading done!");

				UiBanner banner = new UiBanner();
				banner.text = Dungeon.level.formatDepth();
				Dungeon.ui.add(banner);

				Dungeon.buildDiscordBadge();
				third = true;
			}
		});

		thread.setPriority(1);
		thread.run();
	}

	private boolean error;
	private String errorString;
	private float ew;
	private float et;

	@Override
	public void renderUi() {
		this.alp += ((this.third ? 0 : 1) - this.alp) * Gdx.graphics.getDeltaTime() * 10;

		if (this.alp >= 0.95f && !second) {
			this.second = true;
			runThread();
		}

		if (this.alp <= 0.05f && this.third) {
			this.ready = true;
		}

		Graphics.startShape();

		if (et > 0) {
			Graphics.shape.setColor(1, 1, 1, 1);
		} else {
			Graphics.shape.setColor(0, 0, 0, 1);
		}

		Graphics.shape.rect(0, 0, Display.UI_WIDTH, Display.UI_HEIGHT);
		Graphics.endShape();

		if (et > 0) {
			et -= Gdx.graphics.getDeltaTime();
			return;
		}

		Graphics.medium.setColor(1, 1, 1, this.alp);

		if (error) {
			Graphics.print(this.errorString, Graphics.medium, Display.UI_WIDTH / 2 - ew, (Display.UI_HEIGHT - 16) / 2 - 8);
			Dungeon.ui.render();
			Ui.ui.renderCursor();
		} else {
			Graphics.print(this.s, Graphics.medium, (Display.UI_HEIGHT - 16) / 2 - 8);
		}

		Graphics.medium.setColor(1, 1, 1, 1);
	}
}