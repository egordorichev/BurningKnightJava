package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Collisions;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.debug.Console;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Tween;

public class InGameState extends State {
	private UiInventory inventory;
	private Console console;
	private TextureRegion blood;
	private float a;
	private Area pauseMenuUi;
	private boolean showFps;
	public static boolean map = false;

	@Override
	public void init() {
		pauseMenuUi = new Area(true);

		blood = Graphics.getTexture("blood_frame");

		Collisions collisions = new Collisions();

		World.world.setContactListener(collisions);
		World.world.setContactFilter(collisions);

		this.setupUi();

		Dungeon.background = Level.colors[Dungeon.level.uid];

		this.console = new Console();

		if (BurningKnight.instance != null && Dungeon.depth > 0) {
			BurningKnight.instance.findStartPoint();
		}

		Dungeon.darkR = 0;

		Tween.to(new Tween.Task(Dungeon.MAX_R, 0.3f) {
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
				super.onEnd();
				Player.instance.setUnhittable(false);
				Camera.follow(Player.instance);
			}
		});
	}

	@Override
	public void destroy() {
		super.destroy();
		this.console.destroy();

		if (Dungeon.reset) {
			Gdx.files.external(".bk/" + SaveManager.slot).deleteDirectory();
			Dungeon.reset = false;
		} else {
			boolean old = Dungeon.game.getState() instanceof LoadState;

			SaveManager.save(SaveManager.Type.GAME, old);
			SaveManager.save(SaveManager.Type.LEVEL, old);
			SaveManager.save(SaveManager.Type.PLAYER, old);
		}

		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}
	}

	private boolean set;
	private float last;


	@Override
	public void update(float dt) {
		if (map) {
			return;
		}

		this.console.update(dt);

		if (Input.instance.wasPressed("reset")) {
			Dungeon.newGame();
		}
		
		if (Input.instance.wasPressed("show_fps")) {
			this.showFps = !this.showFps;
		}

		last += dt;

		if (last >= 1f) {
			last = 0;

			if (Boss.all.size() > 1 && !BurningKnight.instance.getState().equals("unactive")) {
				Audio.play("Rogue");
			} else {
				Audio.play(Dungeon.level.getMusic());
			}
		}

		World.update(dt);

		if (this.isPaused()) {
			pauseMenuUi.update(dt);
		}
			 
		if (Dialog.active != null) {
			Dialog.active.update(dt);
		}

		if (!set) {
			if (Player.instance != null) {
				Camera.follow(Player.instance);
			}

			set = true;
		}

		if (this.a == 0 && Player.instance != null && Player.instance.getInvt() > 0) {
			Tween.to(new Tween.Task(1f, 0.2f) {
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
					Tween.to(new Tween.Task(0f, 0.8f) {
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
			});
		}
	}

	@Override
	public void render() {
		super.render();

		if (isPaused()) {
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);
			pauseMenuUi.render();
		}
	}

	@Override
	public void renderUi() {
		Ui.ui.renderUi();

		Dungeon.ui.render();

		if (this.isPaused()) {
			return;
		}

		if (this.a != 0) {
			Graphics.batch.setColor(1, 1, 1, this.a);
			Graphics.render(blood, 0, 0);
			Graphics.batch.setColor(1, 1, 1, 1);
		}

		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		World.render();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		this.console.render();
		Ui.ui.render();

		if (Dialog.active != null) {
			Dialog.active.render();
		}

		Ui.ui.renderCursor();

		if (this.showFps && !this.isPaused()) {
			Graphics.print(Integer.toString(Gdx.graphics.getFramesPerSecond()), Graphics.medium, 3, Display.GAME_HEIGHT - 20);
		}
	}

	private void setupUi() {
		this.inventory = new UiInventory(Player.instance.getInventory());
		Dungeon.ui.add(this.inventory);

		// Dungeon.ui.add(new UiMap());

		this.pauseMenuUi.add(new UiButton("resume", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				super.onClick();
				setPaused(false);

				Camera.shake(3);
			}
		}.setSparks(true));

		this.pauseMenuUi.add(new UiButton("settings", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();
				transition(() -> {
					Dungeon.game.setState(new SettingsState());
				});
				Camera.shake(3);
			}
		}.setSparks(true));

		this.pauseMenuUi.add(new UiButton("save_and_exit", Display.GAME_WIDTH / 2, 128 - 24 * 3) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.shake(3);
				transition(() -> Dungeon.game.setState(new MainMenuState()));
			}
		});

		for (Entity entity : this.pauseMenuUi.getEntities()) {
			entity.setActive(false);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		for (Entity entity : this.pauseMenuUi.getEntities()) {
			entity.setActive(true);
		}
	}

	@Override
	public void onUnpause() {
		super.onUnpause();

		for (Entity entity : this.pauseMenuUi.getEntities()) {
			entity.setActive(false);
		}
	}
}